package com.konvert.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.konvert.app.R
import com.konvert.app.ui.home.HomeScreen
import com.konvert.app.ui.lock.PinLockScreen
import kotlinx.coroutines.delay

private const val BankingLoaderAsset = "animations/loader.json"

/**
 * Підготовка сесії банкінгу після вірного PIN.
 * Зараз — мінімальна затримка; згодом — вибір профілю, мережа, кеш, префетч даних.
 */
private suspend fun prepareBankingSession() {
    delay(450)
}

/**
 * PIN → поверх нього лоадер [BankingLoaderAsset] → [HomeScreen].
 */
@Composable
fun BankingEntryHost() {
    var unlocked by remember { mutableStateOf(false) }
    var bankingSessionReady by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!bankingSessionReady) {
            PinLockScreen(onUnlocked = { unlocked = true })
        }
        if (unlocked && !bankingSessionReady) {
            BankingSessionLoaderOverlay(onPrepared = { bankingSessionReady = true })
        }
        if (bankingSessionReady) {
            HomeScreen()
        }
    }
}

@Composable
private fun BankingSessionLoaderOverlay(onPrepared: () -> Unit) {
    val cd = stringResource(R.string.banking_session_loading_cd)
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(BankingLoaderAsset))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = composition != null
    )

    LaunchedEffect(Unit) {
        prepareBankingSession()
        onPrepared()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.52f))
            .semantics { contentDescription = cd }
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .align(Alignment.Center)
                .size(88.dp),
            enableMergePaths = true
        )
    }
}
