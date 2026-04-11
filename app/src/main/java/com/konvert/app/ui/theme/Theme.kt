package com.konvert.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
        content = content
    )
}
