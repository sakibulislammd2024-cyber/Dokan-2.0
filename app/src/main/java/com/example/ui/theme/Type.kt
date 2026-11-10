package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

// Modern geometric sans-serif typography with tabular numerals and clean proportions
val DokanFontFamily = FontFamily.SansSerif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        )
    ),
    displayMedium = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.25).sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    displaySmall = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    headlineLarge = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp
    ),
    bodySmall = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.8.sp
    ),
    labelSmall = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        letterSpacing = 1.2.sp // Uppercase letter-spaced tag labels
    )
)

// Helper style tokens for Dokan design language
object DokanTypographyTokens {
    // Large bold numbers for statistics and amounts (e.g. ৳25,000, ৳1,050)
    val StatNumberLarge = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp
    )

    val StatNumberMedium = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.3).sp
    )

    val StatNumberSmall = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    )

    // Small uppercase letter-spaced section and card labels (e.g. "BOUGHT", "SOLD", "PROFIT", "TEAM (3)")
    val SectionLabel = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.5.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.2.sp
    )

    val CardSubLabel = TextStyle(
        fontFamily = DokanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.6.sp
    )
}

object AppTypographyScale {
    val screenTitle: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DokanDarkTextPrimary)

    val sectionHeading: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DokanDarkTextPrimary)

    val normalText: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal, color = DokanDarkTextPrimary)

    val hintText: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Normal, color = DokanDarkTextSecondary)

    val buttonText: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DokanDarkTextPrimary)

    val smallTag: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DokanDarkTextSecondary)

    val moneyNumber: TextStyle
        get() = TextStyle(fontFamily = DokanFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DokanVividGreen)
}

