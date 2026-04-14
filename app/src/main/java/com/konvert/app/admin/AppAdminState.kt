package com.konvert.app.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class JarAdminConfig(
    val name: String = "На авто",
    val balanceDisplay: String = "6.67 ₴",
    val withdrawnDisplay: String = "51 922.58 ₴",
    val targetDisplay: String = "500 000 ₴",
    val accumulatedDisplay: String = "Накопичено 6.67 ₴",
    val cardNumber: String = "4874 1000 2512 5553",
    val jarLink: String = "send.monobank.ua/jar/8tuBPMjbFA",
    val statPersonalCard: String = "10",
    val statOtherUa: String = "4604.43",
    val statAbroad: String = "0",
    val statByNumber: String = "4447.43",
    val statByLink: String = "167",
    /** Рядки статистики на екрані банки (mono / прапор / глобус). */
    val statMonoDisplay: String = "29 261.18",
    val statFlagDisplay: String = "21 488 ₴",
    val statGlobeDisplay: String = "1 014 ₴"
)

data class AppAdminState(
    val mainFirstName: String = "Андрій",
    val accountFullName: String = "Андрій Коваленко",
    val balanceMain: String = "102 144 ₴",
    val balanceWallet: String = "2 144 ₴",
    val balanceCredit: String = "100 000 ₴",
    val jars: List<JarAdminConfig> = listOf(
        JarAdminConfig(),
        JarAdminConfig(
            name = "Банка 2",
            balanceDisplay = "0 ₴",
            withdrawnDisplay = "0 ₴",
            targetDisplay = "10 000 ₴",
            accumulatedDisplay = "Накопичено 0 ₴"
        ),
        JarAdminConfig(
            name = "Банка 3",
            balanceDisplay = "0 ₴",
            withdrawnDisplay = "0 ₴",
            targetDisplay = "10 000 ₴",
            accumulatedDisplay = "Накопичено 0 ₴"
        )
    )
) {
    fun jarOrDefault(index: Int): JarAdminConfig = jars.getOrNull(index) ?: JarAdminConfig()
}

class AppAdminController(initial: AppAdminState = AppAdminState()) {
    var state by mutableStateOf(initial)

    fun updateMain(
        mainFirstName: String,
        accountFullName: String,
        balanceMain: String,
        balanceWallet: String,
        balanceCredit: String
    ) {
        state = state.copy(
            mainFirstName = mainFirstName,
            accountFullName = accountFullName,
            balanceMain = balanceMain,
            balanceWallet = balanceWallet,
            balanceCredit = balanceCredit
        )
    }

    fun updateJar(index: Int, jar: JarAdminConfig) {
        if (index !in state.jars.indices) return
        state = state.copy(
            jars = state.jars.mapIndexed { i, j -> if (i == index) jar else j }
        )
    }
}
