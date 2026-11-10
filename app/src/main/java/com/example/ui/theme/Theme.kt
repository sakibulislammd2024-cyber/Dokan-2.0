package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DokanVividGreen,
    onPrimary = Color(0xFF0A1805),
    primaryContainer = DokanDeepGreen,
    onPrimaryContainer = DokanDarkTextPrimary,
    secondary = DokanDeepGreen,
    onSecondary = DokanDarkTextPrimary,
    tertiary = DokanAmber,
    onTertiary = Color(0xFF1E1400),
    background = DokanDarkBackground,
    onBackground = DokanDarkTextPrimary,
    surface = DokanDarkSurface,
    onSurface = DokanDarkTextPrimary,
    surfaceVariant = Color(0xFF202020),
    onSurfaceVariant = DokanDarkTextSecondary,
    outline = DokanDarkBorderTop,
    outlineVariant = DokanDarkBorderBottom,
    error = DokanRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = DokanVividGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2F8D5),
    onPrimaryContainer = DokanDeepGreen,
    secondary = DokanDeepGreen,
    onSecondary = Color.White,
    tertiary = DokanAmber,
    onTertiary = Color.White,
    background = DokanLightBackground,
    onBackground = DokanLightTextPrimary,
    surface = DokanLightSurface,
    onSurface = DokanLightTextPrimary,
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = DokanLightTextSecondary,
    outline = DokanLightBorderTop,
    outlineVariant = DokanLightBorderBottom,
    error = DokanRed,
    onError = Color.White
)

object DokanTheme {
    val colors: DokanColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalDokanColors.current

    val typography: androidx.compose.material3.Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

@Composable
fun HardwareManagerTheme(
    darkTheme: Boolean = true, // Default to rich dark mode as specified in the style guide
    content: @Composable () -> Unit
) {
    val dokanPalette = if (darkTheme) DarkDokanPalette else LightDokanPalette
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalDokanColors provides dokanPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
