package com.konvert.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.konvert.app.ui.home.HomeScreen
import com.konvert.app.ui.lock.PinLockScreen
import com.konvert.app.ui.theme.KonvertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KonvertTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var unlocked by remember { mutableStateOf(false) }
                    if (!unlocked) {
                        PinLockScreen(onUnlocked = { unlocked = true })
                    } else {
                        HomeScreen()
                    }
                }
            }
        }
    }
}
