package com.example.data.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseManager {
    private const val TAG = "FirebaseManager"

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _isCloudSyncEnabled = MutableStateFlow(true)
    val isCloudSyncEnabled: StateFlow<Boolean> = _isCloudSyncEnabled.asStateFlow()

    private var firestoreInstance: FirebaseFirestore? = null
    private var isAuthListenerAttached = false

    fun isAvailable(context: Context): Boolean {
        return try {
            if (com.google.firebase.FirebaseApp.getApps(context).isEmpty()) {
                com.google.firebase.FirebaseApp.initializeApp(context) != null
            } else {
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    fun init(context: Context) {
        if (!isAvailable(context)) {
            Log.w(TAG, "FirebaseApp is not available on this device/configuration. Running in offline/local mode.")
            return
        }

        try {
            val auth = Firebase.auth
            _currentUser.value = auth.currentUser

            if (!isAuthListenerAttached) {
                auth.addAuthStateListener { firebaseAuth ->
                    _currentUser.value = firebaseAuth.currentUser
                    Log.d(TAG, "Auth state changed: user=${firebaseAuth.currentUser?.email}")
                }
                isAuthListenerAttached = true
            }

            getFirestore(context)
        } catch (e: Exception) {
            Log.w(TAG, "Firebase initialization error: ${e.message}")
        }
    }

    fun getFirestore(context: Context): FirebaseFirestore? {
        if (!isAvailable(context)) return null
        return firestoreInstance ?: synchronized(this) {
            try {
                val dbId = try {
                    context.getString(R.string.firestore_database_id)
                } catch (e: Exception) {
                    ""
                }
                val db = if (dbId.isNotBlank()) {
                    FirebaseFirestore.getInstance(dbId)
                } else {
                    Firebase.firestore
                }

                try {
                    val settings = firestoreSettings {
                        setLocalCacheSettings(persistentCacheSettings { })
                    }
                    db.firestoreSettings = settings
                    Log.d(TAG, "Firestore initialized with offline cache and db: $dbId")
                } catch (e: Exception) {
                    Log.w(TAG, "Firestore settings already applied or failed: ${e.message}")
                }

                firestoreInstance = db
                db
            } catch (e: Exception) {
                Log.w(TAG, "Failed to initialize Firestore: ${e.message}")
                null
            }
        }
    }

    fun getStorage(): FirebaseStorage? {
        return try {
            Firebase.storage
        } catch (e: Exception) {
            null
        }
    }

    fun setCloudSyncEnabled(enabled: Boolean) {
        _isCloudSyncEnabled.value = enabled
    }

    fun signInWithGoogle(
        activity: Activity,
        scope: CoroutineScope,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit,
        onCancelled: () -> Unit = {}
    ) {
        val resId = activity.resources.getIdentifier("default_web_client_id", "string", activity.packageName)
        val clientId = if (resId != 0) {
            try { activity.getString(resId) } catch (e: Exception) { null }
        } else null

        if (clientId.isNullOrBlank()) {
            onError("Google Sign-In is not configured yet (missing google-services.json).")
            return
        }

        val credentialManager = CredentialManager.create(activity)
        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(activity, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    val authResult = Firebase.auth.signInWithCredential(authCredential).await()
                    val user = authResult.user
                    if (user != null) {
                        _currentUser.value = user
                        onSuccess(user)
                    } else {
                        onError("User was null after sign-in")
                    }
                } else {
                    onError("Unsupported credential returned from Credential Manager")
                }
            } catch (e: GetCredentialCancellationException) {
                Log.w(TAG, "Google Sign-In cancelled: ${e.message}", e)
                onCancelled()
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In error", e)
                onError(e.localizedMessage ?: "Sign-in failed")
            }
        }
    }

    fun attemptSilentSignIn(
        context: Context,
        scope: CoroutineScope,
        onSuccess: (FirebaseUser) -> Unit,
        onFailed: () -> Unit
    ) {
        val authUser = Firebase.auth.currentUser
        if (authUser != null) {
            _currentUser.value = authUser
            onSuccess(authUser)
            return
        }

        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        val clientId = if (resId != 0) {
            try { context.getString(resId) } catch (e: Exception) { null }
        } else null

        if (clientId.isNullOrBlank()) {
            onFailed()
            return
        }

        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(clientId)
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    val authResult = Firebase.auth.signInWithCredential(authCredential).await()
                    val user = authResult.user
                    if (user != null) {
                        _currentUser.value = user
                        onSuccess(user)
                    } else {
                        onFailed()
                    }
                } else {
                    onFailed()
                }
            } catch (e: Exception) {
                onFailed()
            }
        }
    }

    fun signOut(
        context: Context,
        scope: CoroutineScope,
        onComplete: () -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)
        scope.launch {
            withContext(NonCancellable) {
                try {
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                } catch (e: Exception) {
                    Log.w(TAG, "Failed clearing credential manager state: ${e.message}")
                } finally {
                    Firebase.auth.signOut()
                    _currentUser.value = null
                    onComplete()
                }
            }
        }
    }
}
