package com.konvert.app.ui.home

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private tailrec fun ViewParent?.findDialogWindow(): Window? = when (this) {
    null -> null
    is DialogWindowProvider -> window
    else -> parent.findDialogWindow()
}

private fun View.activeWindows(): List<Window> {
    val dialogWindow = parent.findDialogWindow()
    val activityWindow = context.findActivity()?.window
    return listOfNotNull(dialogWindow, activityWindow).distinct()
}

private fun View.disableFitsSystemWindowsDeep() {
    fitsSystemWindows = false
    if (this is ViewGroup) {
        for (index in 0 until childCount) {
            getChildAt(index).disableFitsSystemWindowsDeep()
        }
    }
}

@Composable
internal fun SystemBarsColorEffect(
    statusBarColor: Color,
    navigationBarColor: Color = statusBarColor,
    decorBackgroundColor: Color = navigationBarColor
) {
    val view = LocalView.current
    DisposableEffect(view, statusBarColor, navigationBarColor, decorBackgroundColor) {
        val windows = view.activeWindows()
        val previousColors = windows.map { target ->
            target to Triple(
                target.statusBarColor,
                target.navigationBarColor,
                target.decorView.systemUiVisibility
            )
        }

        windows.forEach { target ->
            WindowCompat.setDecorFitsSystemWindows(target, false)
            target.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            target.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            target.setBackgroundDrawable(ColorDrawable(decorBackgroundColor.toArgb()))
            target.decorView.setBackgroundColor(decorBackgroundColor.toArgb())
            target.decorView.disableFitsSystemWindowsDeep()
            target.decorView.systemUiVisibility =
                target.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            target.statusBarColor = statusBarColor.toArgb()
            target.navigationBarColor = navigationBarColor.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                target.attributes = target.attributes.apply {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
            if (target == view.parent.findDialogWindow()) {
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
            previousColors.forEach { (target, previous) ->
                target.statusBarColor = previous.first
                target.navigationBarColor = previous.second
                target.decorView.systemUiVisibility = previous.third
            }
        }
    }
}
