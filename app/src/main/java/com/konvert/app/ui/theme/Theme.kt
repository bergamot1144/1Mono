package com.konvert.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.konvert.app.R

/** Локальні файли Roboto (regular / medium / bold). */
private val AppRobotoFontFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.roboto_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold, FontStyle.Normal)
)

private val KonvertTypography = run {
    val base = Typography()
    Typography(
        displayLarge = base.displayLarge.copy(fontFamily = AppRobotoFontFamily),
        displayMedium = base.displayMedium.copy(fontFamily = AppRobotoFontFamily),
        displaySmall = base.displaySmall.copy(fontFamily = AppRobotoFontFamily),
        headlineLarge = base.headlineLarge.copy(fontFamily = AppRobotoFontFamily),
        headlineMedium = base.headlineMedium.copy(fontFamily = AppRobotoFontFamily),
        headlineSmall = base.headlineSmall.copy(fontFamily = AppRobotoFontFamily),
        titleLarge = base.titleLarge.copy(fontFamily = AppRobotoFontFamily),
        titleMedium = base.titleMedium.copy(fontFamily = AppRobotoFontFamily),
        titleSmall = base.titleSmall.copy(fontFamily = AppRobotoFontFamily),
        bodyLarge = base.bodyLarge.copy(fontFamily = AppRobotoFontFamily),
        bodyMedium = base.bodyMedium.copy(fontFamily = AppRobotoFontFamily),
        bodySmall = base.bodySmall.copy(fontFamily = AppRobotoFontFamily),
        labelLarge = base.labelLarge.copy(fontFamily = AppRobotoFontFamily),
        labelMedium = base.labelMedium.copy(fontFamily = AppRobotoFontFamily),
        labelSmall = base.labelSmall.copy(fontFamily = AppRobotoFontFamily)
    )
}

private val KonvertColorScheme = darkColorScheme(
    primary = TextPrimary,
    onPrimary = Background,
    background = Background,
    onBackground = TextPrimary,
    surface = KeypadButton,
    onSurface = TextPrimary,
    error = ErrorTint,
    onError = Color.White
)

@Composable
fun KonvertTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KonvertColorScheme,
        typography = KonvertTypography,
        content = content
    )
}
