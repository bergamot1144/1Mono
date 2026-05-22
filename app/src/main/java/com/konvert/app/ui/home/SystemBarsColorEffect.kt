package com.konvert.app.ui.home

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

@Composable
internal fun SystemBarsColorEffect(
    statusBarColor: Color,
    navigationBarColor: Color = statusBarColor,
    decorBackgroundColor: Color = navigationBarColor
) {
    val view = LocalView.current
    DisposableEffect(view, statusBarColor, navigationBarColor, decorBackgroundColor) {
        val dialogWindow = (view.parent as? DialogWindowProvider)?.window
        val activityWindow = (view.context as? Activity)?.window
        val window = dialogWindow ?: activityWindow
        val previousStatusBarColor = window?.statusBarColor
        val previousNavigationBarColor = window?.navigationBarColor

        window?.let { target ->
            WindowCompat.setDecorFitsSystemWindows(target, false)
            target.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            target.setBackgroundDrawable(ColorDrawable(decorBackgroundColor.toArgb()))
            target.decorView.setBackgroundColor(decorBackgroundColor.toArgb())
            target.statusBarColor = statusBarColor.toArgb()
            target.navigationBarColor = navigationBarColor.toArgb()
            if (dialogWindow != null) {
                target.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                target.isStatusBarContrastEnforced = false
                target.isNavigationBarContrastEnforced = false
            }
        }

        onDispose {
            window?.let { target ->
                if (previousStatusBarColor != null) {
                    target.statusBarColor = previousStatusBarColor
                }
                if (previousNavigationBarColor != null) {
                    target.navigationBarColor = previousNavigationBarColor
                }
            }
        }
    }
}
