package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// --- Core Palette Tokens ---
val DokanVividGreen = Color(0xFF5DD62C)
val DokanDeepGreen = Color(0xFF337418)
val DokanEmeraldGlow = Color(0x335DD62C)
val DokanEmeraldSubtle = Color(0x185DD62C)

// Dark Mode Tokens
val DokanDarkBackground = Color(0xFF0F0F0F)
val DokanDarkSurface = Color(0xFF161616)
val DokanDarkCardGlass = Color(0xA6202020) // ~65% opacity on #202020
val DokanDarkCardElevated = Color(0xCC242424)
val DokanDarkTextPrimary = Color(0xFFF8F8F8)
val DokanDarkTextSecondary = Color(0x8CFFFFFF) // 55% white
val DokanDarkTextMuted = Color(0x59FFFFFF) // 35% white
val DokanDarkBorderTop = Color(0x38FFFFFF) // 22% white
val DokanDarkBorderBottom = Color(0x0AFFFFFF) // 4% white
val DokanDarkDivider = Color(0x1AFFFFFF)

// Light Mode Tokens (matching light version using #F8F8F8 as background)
val DokanLightBackground = Color(0xFFF8F8F8)
val DokanLightSurface = Color(0xFFFFFFFF)
val DokanLightCardGlass = Color(0xBFFFFFFF) // 75% white glass
val DokanLightCardElevated = Color(0xFFFFFFFF)
val DokanLightTextPrimary = Color(0xFF121212)
val DokanLightTextSecondary = Color(0x8C121212) // 55% dark
val DokanLightTextMuted = Color(0x59121212)
val DokanLightBorderTop = Color(0x33000000)
val DokanLightBorderBottom = Color(0x0D000000)
val DokanLightDivider = Color(0x14000000)

// Semantic Accents
val DokanAmber = Color(0xFFFFB020) // Warnings / money owed / baki
val DokanAmberSubtle = Color(0x26FFB020)
val DokanRed = Color(0xFFFF453A) // Danger only / alert / delete
val DokanRedSubtle = Color(0x26FF453A)
val DokanBlue = Color(0xFF2997FF)
val DokanBlueSubtle = Color(0x262997FF)

// Legacy compatibility aliases
val Purple80 = DokanVividGreen
val PurpleGrey80 = DokanDeepGreen
val Pink80 = DokanAmber
val Purple40 = DokanDeepGreen
val PurpleGrey40 = DokanVividGreen
val Pink40 = DokanRed

val DarkSlateBackground = DokanDarkBackground
val DarkSlateSurface = DokanDarkSurface
val DarkSlateSurfaceVariant = DokanDarkCardGlass
val DarkSlateCardBorder = DokanDarkBorderTop
val LowStockRed = DokanRed
val LowStockRedContainer = Color(0x33FF453A)
val TextPrimary = DokanDarkTextPrimary
val TextSecondary = DokanDarkTextSecondary
val TextMuted = DokanDarkTextMuted
val TextDark = Color(0xFF0F0F0F)
val VividEmeraldPrimary = DokanVividGreen
val VividEmeraldAccent = DokanVividGreen

@Immutable
data class DokanColorPalette(
    val background: Color,
    val surface: Color,
    val cardGlass: Color,
    val cardElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderGradient: Brush,
    val heroBorderGradient: Brush,
    val heroCardGradient: Brush,
    val divider: Color,
    val vividGreen: Color = DokanVividGreen,
    val deepGreen: Color = DokanDeepGreen,
    val emeraldGlow: Color = DokanEmeraldGlow,
    val amber: Color = DokanAmber,
    val amberSubtle: Color = DokanAmberSubtle,
    val red: Color = DokanRed,
    val redSubtle: Color = DokanRedSubtle,
    val isDark: Boolean = true
)

val DarkDokanPalette = DokanColorPalette(
    background = DokanDarkBackground,
    surface = DokanDarkSurface,
    cardGlass = DokanDarkCardGlass,
    cardElevated = DokanDarkCardElevated,
    textPrimary = DokanDarkTextPrimary,
    textSecondary = DokanDarkTextSecondary,
    textMuted = DokanDarkTextMuted,
    borderGradient = Brush.verticalGradient(
        listOf(DokanDarkBorderTop, DokanDarkBorderBottom)
    ),
    heroBorderGradient = Brush.verticalGradient(
        listOf(DokanVividGreen.copy(alpha = 0.85f), DokanDeepGreen.copy(alpha = 0.40f))
    ),
    heroCardGradient = Brush.linearGradient(
        listOf(
            Color(0x385DD62C),
            Color(0x14337418),
            Color(0xCC1A1A1A)
        )
    ),
    divider = DokanDarkDivider,
    isDark = true
)

val LightDokanPalette = DokanColorPalette(
    background = DokanLightBackground,
    surface = DokanLightSurface,
    cardGlass = DokanLightCardGlass,
    cardElevated = DokanLightCardElevated,
    textPrimary = DokanLightTextPrimary,
    textSecondary = DokanLightTextSecondary,
    textMuted = DokanLightTextMuted,
    borderGradient = Brush.verticalGradient(
        listOf(DokanLightBorderTop, DokanLightBorderBottom)
    ),
    heroBorderGradient = Brush.verticalGradient(
        listOf(DokanVividGreen, DokanDeepGreen.copy(alpha = 0.60f))
    ),
    heroCardGradient = Brush.linearGradient(
        listOf(
            Color(0x265DD62C),
            Color(0x10337418),
            Color(0xEEFFFFFF)
        )
    ),
    divider = DokanLightDivider,
    isDark = false
)

val LocalDokanColors = staticCompositionLocalOf { DarkDokanPalette }
