package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppSettings
import com.example.data.local.NotificationManager
import com.example.data.local.PinLockManager
import com.example.data.local.ProfileManager
import com.example.data.local.StaffSessionManager
import com.example.data.local.ViewMode
import com.example.data.local.entity.Product
import com.example.data.local.entity.SaleOrder
import com.example.ui.components.DokanBackground
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.GlassCard
import com.example.ui.components.HeroGlassCard
import com.example.ui.components.LetterInitialAvatar
import com.example.ui.components.NotificationsBottomSheet
import com.example.ui.components.PinVerifyDialog
import com.example.ui.components.ProgressRing
import com.example.ui.components.ProfileSwitcherBottomSheet
import com.example.ui.components.SlimProgressBar
import com.example.ui.components.StatusTag
import com.example.ui.components.StatusTagType
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.formatTaka
import com.example.ui.screens.home.CashflowState
import com.example.ui.screens.home.HomeSummaryMetrics
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.home.QuickAddPartyDialog
import com.example.ui.screens.home.QuickPaymentInDialog
import com.example.ui.screens.home.QuickQuotationDialog
import com.example.ui.screens.home.QuickSalesReturnDialog
import com.example.ui.theme.DokanAmber
import com.example.ui.theme.DokanDeepGreen
import com.example.ui.theme.DokanTheme
import com.example.ui.theme.DokanTypographyTokens
import com.example.ui.theme.DokanVividGreen
import java.util.Calendar

@Composable
fun HomeScreen(
    currentLanguage: AppLanguage,
    onNavigateToSell: () -> Unit = {},
    onNavigateToStock: () -> Unit = {},
    onNavigateToBaki: () -> Unit = {},
    onNavigateToSuppliers: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToBusiness: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToMarketplace: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToMore: () -> Unit = {},
    onNavigateToPurchases: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val todayMetrics by viewModel.todayMetrics.collectAsStateWithLifecycle()
    val customerBaki by viewModel.customerBaki.collectAsStateWithLifecycle()
    val supplierDue by viewModel.supplierDue.collectAsStateWithLifecycle()
    val totalBalance by viewModel.totalBalance.collectAsStateWithLifecycle()
    val cashflowState by viewModel.cashflow7Days.collectAsStateWithLifecycle()
    val lowStockList by viewModel.lowStockProducts.collectAsStateWithLifecycle()
    val recentSalesList by viewModel.recentSales.collectAsStateWithLifecycle()
    val allCustomers by viewModel.repository.allCustomers.collectAsStateWithLifecycle(emptyList())
    val allProducts by viewModel.repository.allProducts.collectAsStateWithLifecycle(emptyList())
    val isBangla = currentLanguage == AppLanguage.BANGLA
    val todayDateFormatted = viewModel.getFormattedDate(currentLanguage)

    val activeProfile = ProfileManager.activeProfile

    var showProfileSwitcher by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showPinDialogForReveal by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }

    // Quick Action Dialogs for Shortcuts
    var showQuickAddParty by remember { mutableStateOf(false) }
    var showQuickPaymentIn by remember { mutableStateOf(false) }
    var showQuickSalesReturn by remember { mutableStateOf(false) }
    var showQuickQuotation by remember { mutableStateOf(false) }

    if (showProfileSwitcher) {
        ProfileSwitcherBottomSheet(
            isBangla = isBangla,
            onDismiss = { showProfileSwitcher = false }
        )
    }

    if (showEditProfileDialog && activeProfile != null) {
        EditProfileDialog(
            profile = activeProfile,
            isBangla = isBangla,
            onDismiss = { showEditProfileDialog = false },
            onSave = { updated ->
                ProfileManager.updateProfile(context, updated)
                showEditProfileDialog = false
                Toast.makeText(
                    context,
                    if (isBangla) "প্রোফাইল তথ্য আপডেট হয়েছে!" else "Profile updated successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    if (showPinDialogForReveal) {
        PinVerifyDialog(
            title = if (isBangla) "গোপন সংখ্যা দেখার জন্য পিন দিন" else "Enter PIN to Reveal Numbers",
            isBangla = isBangla,
            onDismiss = { showPinDialogForReveal = false },
            onPinSuccess = {
                showPinDialogForReveal = false
                AppSettings.togglePrivacyMode(context)
            }
        )
    }

    if (showNotificationsSheet) {
        NotificationsBottomSheet(
            isBangla = isBangla,
            onDismiss = { showNotificationsSheet = false }
        )
    }

    val onRevealRequest = {
        if (AppSettings.isPrivacyModeEnabled) {
            if (PinLockManager.isPinSet) {
                showPinDialogForReveal = true
            } else {
                AppSettings.togglePrivacyMode(context)
            }
        }
    }

    val currentViewMode = AppSettings.viewMode

    DokanBackground(modifier = modifier) {
        if (currentViewMode == ViewMode.SIMPLE) {
            // ==========================================
            // 3. SIMPLE-MODE HOME
            // ==========================================
            SimpleHomeScreenContent(
                todayMetrics = todayMetrics,
                customerBaki = customerBaki,
                totalProductsCount = allProducts.size,
                debtorsCount = allCustomers.count { it.totalBaki > 0 },
                todayDateFormatted = todayDateFormatted,
                isBangla = isBangla,
                storeName = activeProfile?.name?.ifBlank { "Sakibul Traders" } ?: "Sakibul Traders",
                onToggleViewMode = {
                    AppSettings.setViewMode(ViewMode.DETAILED, context)
                    Toast.makeText(context, if (isBangla) "বিস্তারিত মোড চালু হয়েছে" else "Switched to Detail mode", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = { showProfileSwitcher = true },
                onNotificationsClick = { showNotificationsSheet = true },
                onNavigateToSell = onNavigateToSell,
                onNavigateToStock = onNavigateToStock,
                onNavigateToBaki = onNavigateToBaki,
                onNavigateToHistory = onNavigateToHistory,
                onRevealRequest = onRevealRequest
            )
        } else {
            // ==========================================
            // 2. DETAIL-MODE HOME
            // ==========================================
            DetailHomeScreenContent(
                todayMetrics = todayMetrics,
                customerBaki = customerBaki,
                supplierDue = supplierDue,
                totalBalance = totalBalance,
                cashflowState = cashflowState,
                lowStockList = lowStockList,
                recentSalesList = recentSalesList,
                customersWithDues = allCustomers.filter { it.totalBaki > 0 },
                profileCompletion = activeProfile?.getCompletionPercentage() ?: 60,
                storeName = activeProfile?.name?.ifBlank { "Sakibul Traders" } ?: "Sakibul Traders",
                todayDateFormatted = todayDateFormatted,
                isBangla = isBangla,
                onTogglePrivacy = onRevealRequest,
                onNotificationsClick = { showNotificationsSheet = true },
                onOpenMore = onNavigateToMore,
                onProfileClick = { showProfileSwitcher = true },
                onEditProfileClick = { showEditProfileDialog = true },
                onNavigateToSell = onNavigateToSell,
                onNavigateToStock = onNavigateToStock,
                onNavigateToBaki = onNavigateToBaki,
                onNavigateToSuppliers = onNavigateToSuppliers,
                onNavigateToAccounts = onNavigateToAccounts,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToBusiness = onNavigateToBusiness,
                onQuickPaymentIn = { showQuickPaymentIn = true },
                onQuickAddParty = { showQuickAddParty = true }
            )
        }
    }

    // Quick Action Dialogs
    if (showQuickAddParty) {
        QuickAddPartyDialog(
            isBangla = isBangla,
            onDismiss = { showQuickAddParty = false },
            onSaveCustomer = { name, phone, address, initialBaki ->
                viewModel.insertCustomer(name, phone, address, initialBaki)
                showQuickAddParty = false
                Toast.makeText(context, if (isBangla) "গ্রাহক সফলভাবে যুক্ত হয়েছে" else "Customer added successfully", Toast.LENGTH_SHORT).show()
            },
            onSaveSupplier = { name, company, phone, amountOwed ->
                viewModel.insertSupplier(name, company, phone, amountOwed)
                showQuickAddParty = false
                Toast.makeText(context, if (isBangla) "মহাজন সফলভাবে যুক্ত হয়েছে" else "Supplier added successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showQuickPaymentIn) {
        QuickPaymentInDialog(
            customers = allCustomers,
            isBangla = isBangla,
            currentLanguage = currentLanguage,
            onDismiss = { showQuickPaymentIn = false },
            onConfirm = { custId, amt, note ->
                viewModel.recordCustomerPayment(custId, amt, note)
                showQuickPaymentIn = false
                Toast.makeText(context, if (isBangla) "টাকা জমা সম্পন্ন হয়েছে" else "Payment received successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showQuickSalesReturn) {
        QuickSalesReturnDialog(
            products = allProducts,
            customers = allCustomers,
            isBangla = isBangla,
            onDismiss = { showQuickSalesReturn = false },
            onConfirm = { _, _, _, _, _ ->
                showQuickSalesReturn = false
                Toast.makeText(context, if (isBangla) "বিক্রি ফেরত রেকর্ড হয়েছে" else "Sales return saved", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showQuickQuotation) {
        QuickQuotationDialog(
            products = allProducts,
            isBangla = isBangla,
            onDismiss = { showQuickQuotation = false },
            onConvertToSale = { _, _, _, _ ->
                showQuickQuotation = false
                onNavigateToSell()
            }
        )
    }
}

// =========================================================================
// DETAIL-MODE HOME CONTENT
// =========================================================================
@Composable
private fun DetailHomeScreenContent(
    todayMetrics: HomeSummaryMetrics,
    customerBaki: Double,
    supplierDue: Double,
    totalBalance: Double,
    cashflowState: CashflowState,
    lowStockList: List<Product>,
    recentSalesList: List<SaleOrder>,
    customersWithDues: List<com.example.data.local.entity.Customer>,
    profileCompletion: Int,
    storeName: String,
    todayDateFormatted: String,
    isBangla: Boolean,
    onTogglePrivacy: () -> Unit,
    onNotificationsClick: () -> Unit,
    onOpenMore: () -> Unit,
    onProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onNavigateToSell: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToBaki: () -> Unit,
    onNavigateToSuppliers: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToBusiness: () -> Unit,
    onQuickPaymentIn: () -> Unit,
    onQuickAddParty: () -> Unit
) {
    val colors = DokanTheme.colors
    val isPrivacy = AppSettings.isPrivacyModeEnabled
    val unreadCount = NotificationManager.unreadCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("screen_home_detail"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Safe inset space at top for status bar
        Spacer(modifier = Modifier.height(32.dp))

        // 1. Header: Store logo, Greeting, Store name with live sync dot, Eye, Bell, More Grid button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onProfileClick() }
                    .padding(vertical = 4.dp)
            ) {
                // Store logo circular glass
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .shadow(4.dp, CircleShape)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF202020)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Store Logo",
                        tint = DokanVividGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    val greeting = getGreetingText(isBangla)
                    Text(
                        text = greeting,
                        style = DokanTypographyTokens.CardSubLabel,
                        color = colors.textSecondary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = storeName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Small live green sync dot
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(DokanVividGreen)
                        )
                    }
                }
            }

            // Top action buttons (Eye, Bell, More Grid)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Eye button (Privacy)
                IconButton(
                    onClick = onTogglePrivacy,
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0x55202020))
                ) {
                    Icon(
                        imageVector = if (isPrivacy) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Privacy Toggle",
                        tint = if (isPrivacy) DokanVividGreen else colors.textPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Bell button (Notifications)
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0x55202020))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = colors.textPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(DokanVividGreen)
                            )
                        }
                    }
                }

                // More round grid icon
                IconButton(
                    onClick = onOpenMore,
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0x55202020))
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "More Hub",
                        tint = colors.textPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }

        // 2. Slim profile-progress pill that disappears at 100%
        if (profileCompletion < 100) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .border(1.dp, colors.borderGradient, RoundedCornerShape(17.dp))
                    .clip(RoundedCornerShape(17.dp))
                    .clickable { onEditProfileClick() },
                color = Color(0x661E1E1E),
                shape = RoundedCornerShape(17.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile $profileCompletion%",
                        color = colors.textSecondary,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    SlimProgressBar(
                        progress = profileCompletion / 100f,
                        modifier = Modifier.weight(1f),
                        height = 4.dp
                    )
                }
            }
        }

        // 3. Hero Card with today's sales as a giant glowing number, sparkline, Profit, Orders, Items sold
        HeroGlassCard(
            onClick = onNavigateToHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusTag(text = "TODAY", type = StatusTagType.SUCCESS)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = DokanVividGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "12% vs yesterday",
                            fontSize = 11.sp,
                            color = DokanVividGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = "↑ 12% vs expecting",
                        fontSize = 11.sp,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Giant glowing sales number
            Text(
                text = if (isPrivacy) "৳ •••••" else formatTaka(todayMetrics.totalSales),
                style = DokanTypographyTokens.StatNumberLarge.copy(fontSize = 38.sp),
                color = DokanVividGreen
            )

            // Smooth green sparkline wave
            SparklineWave(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom figures row (Profit, Orders, Items Sold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "PROFIT",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Text(
                        text = if (isPrivacy) "৳ •••" else formatTaka(todayMetrics.totalProfit),
                        style = DokanTypographyTokens.StatNumberSmall,
                        color = colors.textPrimary
                    )
                }

                Column {
                    Text(
                        text = "ORDERS",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Text(
                        text = "${todayMetrics.ordersCount}",
                        style = DokanTypographyTokens.StatNumberSmall,
                        color = colors.textPrimary
                    )
                }

                Column {
                    Text(
                        text = "ITEMS SOLD",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Text(
                        text = "${todayMetrics.itemsSoldCount}",
                        style = DokanTypographyTokens.StatNumberSmall,
                        color = colors.textPrimary
                    )
                }
            }
        }

        // 4. Bento Grid: Left (To Receive) & Right (Cash & Bank, To Give)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Left Tall Card: TO RECEIVE (Customer Baki + debtor avatars)
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(184.dp),
                onClick = onNavigateToBaki
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TO RECEIVE",
                                style = DokanTypographyTokens.SectionLabel,
                                color = colors.textSecondary
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = colors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isPrivacy) "৳ •••••" else formatTaka(customerBaki),
                            style = DokanTypographyTokens.StatNumberMedium,
                            color = colors.textPrimary
                        )

                        Text(
                            text = "Baki · ${customersWithDues.size} customers",
                            style = DokanTypographyTokens.CardSubLabel,
                            color = colors.textSecondary
                        )
                    }

                    // Debtor initial avatars row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-6).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val previewDebtors = customersWithDues.take(3)
                        if (previewDebtors.isEmpty()) {
                            LetterInitialAvatar(name = "K", size = 32.dp)
                            LetterInitialAvatar(name = "H", size = 32.dp)
                            LetterInitialAvatar(name = "A", size = 32.dp)
                        } else {
                            previewDebtors.forEach { customer ->
                                LetterInitialAvatar(name = customer.name, size = 32.dp)
                            }
                        }
                    }
                }
            }

            // Right Stack: Cash & Bank (Top) and To Give (Bottom)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(184.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Cash & Bank
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onNavigateToAccounts,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CASH & BANK",
                            style = DokanTypographyTokens.SectionLabel,
                            color = colors.textSecondary
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPrivacy) "৳ •••••" else formatTaka(totalBalance),
                        style = DokanTypographyTokens.StatNumberSmall.copy(fontSize = 20.sp),
                        color = colors.textPrimary
                    )
                }

                // To Give (Suppliers Due)
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onNavigateToSuppliers,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TO GIVE",
                            style = DokanTypographyTokens.SectionLabel,
                            color = colors.textSecondary
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isPrivacy) "৳ •••••" else formatTaka(supplierDue),
                        style = DokanTypographyTokens.StatNumberSmall.copy(fontSize = 19.sp),
                        color = colors.textPrimary
                    )
                    Text(
                        text = "suppliers",
                        style = DokanTypographyTokens.CardSubLabel,
                        color = colors.textSecondary
                    )
                }
            }
        }

        // 5. Low Stock with slim progress bars
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToStock
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left bold number
                Column(
                    modifier = Modifier.width(96.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LOW STOCK",
                            style = DokanTypographyTokens.SectionLabel,
                            color = colors.textSecondary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${lowStockList.size}",
                        style = DokanTypographyTokens.StatNumberLarge.copy(fontSize = 32.sp),
                        color = DokanAmber
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Right items with slim progress bars
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val previewItems = lowStockList.take(2)
                    if (previewItems.isEmpty()) {
                        Text(
                            text = "All inventory levels are healthy",
                            color = colors.textSecondary,
                            fontSize = 12.sp
                        )
                    } else {
                        previewItems.forEach { item ->
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${item.name} ${item.stockQuantity.toInt()} pcs",
                                        color = colors.textPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    StatusTag(text = "Low", type = StatusTagType.WARNING)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                val progress = (item.stockQuantity.toFloat() / (item.lowStockThreshold.coerceAtLeast(1) * 2)).coerceIn(0.1f, 1f)
                                SlimProgressBar(
                                    progress = progress,
                                    trackColor = Color(0x33FFB020),
                                    progressColor = DokanAmber,
                                    height = 4.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. 7-Day Cashflow as a smooth two-line chart with day labels
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToBusiness
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "7-DAY CASHFLOW",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(DokanVividGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "In ${formatTaka(cashflowState.totalCashIn)}",
                            fontSize = 11.sp,
                            color = colors.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF888888))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Out ${formatTaka(cashflowState.totalCashOut)}",
                            fontSize = 11.sp,
                            color = colors.textSecondary,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Two-Line Smooth Bézier Curve Chart
            TwoLineCashflowChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Day Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("M", "T", "W", "T", "F", "S", "S")
                days.forEach { day ->
                    Text(
                        text = day,
                        color = colors.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 7. 5 Round Shortcut Buttons (Payment In, Payment Out, Purchase, Expense, Add Party)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShortcutRoundButton(
                label = if (isBangla) "টাকা জমা" else "Payment In",
                icon = Icons.AutoMirrored.Filled.CallReceived,
                onClick = onQuickPaymentIn,
                testTag = "shortcut_payment_in"
            )
            ShortcutRoundButton(
                label = if (isBangla) "টাকা প্রদান" else "Payment Out",
                icon = Icons.AutoMirrored.Filled.CallMade,
                onClick = onNavigateToSuppliers,
                testTag = "shortcut_payment_out"
            )
            ShortcutRoundButton(
                label = if (isBangla) "ক্রয়" else "Purchase",
                icon = Icons.Default.ShoppingBag,
                onClick = onNavigateToStock,
                testTag = "shortcut_purchase"
            )
            ShortcutRoundButton(
                label = if (isBangla) "খরচ" else "Expense",
                icon = Icons.Default.AttachMoney,
                onClick = onNavigateToAccounts,
                testTag = "shortcut_expense"
            )
            ShortcutRoundButton(
                label = if (isBangla) "পার্টি +" else "Add Party",
                icon = Icons.Default.PersonAdd,
                onClick = onQuickAddParty,
                testTag = "shortcut_add_party"
            )
        }

        // 8. Recent Sales with letter-initial avatars
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToHistory
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RECENT SALES",
                    style = DokanTypographyTokens.SectionLabel,
                    color = colors.textSecondary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "View All",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val previewSales = recentSalesList.take(3)
            if (previewSales.isEmpty()) {
                Text(
                    text = "No sales recorded yet today",
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    previewSales.forEach { sale ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val partyName = sale.customerName.ifBlank { "Walk-in" }
                            LetterInitialAvatar(name = partyName, size = 36.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = partyName,
                                    color = colors.textPrimary,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "INV-${sale.invoiceNumber} · ${sale.totalItemsCount} items",
                                    color = colors.textSecondary,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = if (isPrivacy) "৳ •••" else formatTaka(sale.totalAmount),
                                style = DokanTypographyTokens.StatNumberSmall,
                                color = colors.textPrimary
                            )
                        }
                    }
                }
            }
        }

        // Space to ensure clean clearance above the floating pill dock
        Spacer(modifier = Modifier.height(100.dp))
    }
}

// =========================================================================
// SIMPLE-MODE HOME CONTENT
// =========================================================================
@Composable
private fun SimpleHomeScreenContent(
    todayMetrics: HomeSummaryMetrics,
    customerBaki: Double,
    totalProductsCount: Int,
    debtorsCount: Int,
    todayDateFormatted: String,
    isBangla: Boolean,
    storeName: String,
    onToggleViewMode: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onNavigateToSell: () -> Unit,
    onNavigateToStock: () -> Unit,
    onNavigateToBaki: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onRevealRequest: () -> Unit
) {
    val colors = DokanTheme.colors
    val isPrivacy = AppSettings.isPrivacyModeEnabled
    val unreadCount = NotificationManager.unreadCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("screen_home_simple"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // 1. Header: Store logo, store name, date, Eye, Bell
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onProfileClick() }
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF202020)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "Store",
                        tint = DokanVividGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = storeName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = todayDateFormatted,
                        style = DokanTypographyTokens.CardSubLabel,
                        color = colors.textSecondary,
                        maxLines = 1
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onRevealRequest,
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0x55202020))
                ) {
                    Icon(
                        imageVector = if (isPrivacy) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Privacy Toggle",
                        tint = if (isPrivacy) DokanVividGreen else colors.textPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.dp, colors.borderGradient, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0x55202020))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = colors.textPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(DokanVividGreen)
                            )
                        }
                    }
                }
            }
        }

        // 2. Mode pill switcher: "Simple mode" (tappable to switch to Detail mode)
        Surface(
            modifier = Modifier
                .border(1.dp, colors.borderGradient, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .clickable { onToggleViewMode() },
            shape = RoundedCornerShape(20.dp),
            color = Color(0x66222222)
        ) {
            Text(
                text = if (isBangla) "সহজ মোড (পরিবর্তন করতে চাপুন)" else "Simple mode",
                color = colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // 3. Big progress ring with today's sales
        Box(
            modifier = Modifier
                .size(240.dp)
                .clip(CircleShape)
                .clickable { onNavigateToHistory() },
            contentAlignment = Alignment.Center
        ) {
            val progress = (todayMetrics.totalSales / 50000.0).toFloat().coerceIn(0.25f, 1f)
            ProgressRing(
                progress = progress,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 14.dp,
                trackColor = Color(0x24242424),
                progressColor = DokanVividGreen
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "TODAY'S SALES",
                        style = DokanTypographyTokens.SectionLabel,
                        color = colors.textSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isPrivacy) "৳ •••••" else formatTaka(todayMetrics.totalSales),
                        style = DokanTypographyTokens.StatNumberLarge.copy(fontSize = 36.sp),
                        color = DokanVividGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${todayMetrics.ordersCount} orders",
                        style = DokanTypographyTokens.CardSubLabel,
                        color = colors.textSecondary
                    )
                }
            }
        }

        // 4. One wide Sell Products card
        HeroGlassCard(
            onClick = onNavigateToSell,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x33282828)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = colors.textPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isBangla) "পণ্য বিক্রি করুন" else "Sell Products",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Sell",
                    tint = colors.textPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 5. Two square cards (Stock & Products, Customer Baki)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stock & Products square card
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                onClick = onNavigateToStock
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x33282828)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = colors.textPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (isBangla) "স্টক ও পণ্য" else "Stock & Products",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary
                        )
                        Text(
                            text = "$totalProductsCount items",
                            style = DokanTypographyTokens.CardSubLabel,
                            color = colors.textSecondary
                        )
                    }
                }
            }

            // Customer Baki square card
            GlassCard(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp),
                onClick = onNavigateToBaki
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isBangla) "বাকির খাতা" else "Customer Baki",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary
                    )

                    Column {
                        Text(
                            text = if (isPrivacy) "৳ •••" else formatTaka(customerBaki),
                            style = DokanTypographyTokens.StatNumberMedium.copy(fontSize = 22.sp),
                            color = DokanVividGreen
                        )
                        Text(
                            text = "$debtorsCount customers",
                            style = DokanTypographyTokens.CardSubLabel,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

// =========================================================================
// HELPER DRAWING & SHORTCUT COMPONENTS
// =========================================================================

@Composable
private fun ShortcutRoundButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    val colors = DokanTheme.colors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 28.dp),
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(4.dp, CircleShape)
                .border(1.dp, colors.borderGradient, CircleShape)
                .clip(CircleShape)
                .background(Color(0xFF222222)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SparklineWave(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(0f, h * 0.75f)
            cubicTo(w * 0.15f, h * 0.95f, w * 0.25f, h * 0.25f, w * 0.40f, h * 0.35f)
            cubicTo(w * 0.55f, h * 0.45f, w * 0.65f, h * 0.85f, w * 0.80f, h * 0.20f)
            cubicTo(w * 0.90f, h * 0.05f, w * 0.95f, h * 0.35f, w, h * 0.10f)
        }

        drawPath(
            path = path,
            color = DokanVividGreen,
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun TwoLineCashflowChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Green Curve: Cash In
        val inPath = Path().apply {
            moveTo(0f, h * 0.70f)
            cubicTo(w * 0.2f, h * 0.85f, w * 0.35f, h * 0.40f, w * 0.50f, h * 0.45f)
            cubicTo(w * 0.65f, h * 0.50f, w * 0.75f, h * 0.10f, w * 0.88f, h * 0.35f)
            lineTo(w, h * 0.55f)
        }

        // Grey/Amber Curve: Cash Out
        val outPath = Path().apply {
            moveTo(0f, h * 0.85f)
            cubicTo(w * 0.2f, h * 0.75f, w * 0.35f, h * 0.65f, w * 0.50f, h * 0.70f)
            cubicTo(w * 0.65f, h * 0.75f, w * 0.80f, h * 0.60f, w * 0.90f, h * 0.70f)
            lineTo(w, h * 0.78f)
        }

        drawPath(
            path = inPath,
            color = DokanVividGreen,
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        drawPath(
            path = outPath,
            color = Color(0xFF888888),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

private fun getGreetingText(isBangla: Boolean): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour in 4..11 -> if (isBangla) "শুভ সকাল" else "Good morning"
        hour in 12..16 -> if (isBangla) "শুভ অপরাহ্ন" else "Good afternoon"
        else -> if (isBangla) "শুভ সন্ধ্যা" else "Good evening"
    }
}
