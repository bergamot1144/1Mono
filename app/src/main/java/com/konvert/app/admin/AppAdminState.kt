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

data class CardOperationAdminConfig(
    val title: String = "Steam",
    val amount: String = "-100 ₴",
    val dateLabel: String = "Сьогодні",
    val hasCommission: Boolean = false,
    val commissionAmount: String = "0 ₴"
)

data class CardAdminConfig(
    val name: String,
    val balanceDisplay: String,
    val cardNumber: String,
    val operations: List<CardOperationAdminConfig>
)

data class AppAdminState(
    val mainFirstName: String = "Андрій",
    val accountFullName: String = "Андрій Коваленко",
    val profileAvatarPath: String? = null,
    val balanceMain: String = "102 144 ₴",
    val balanceWallet: String = "2 144 ₴",
    val balanceCredit: String = "100 000 ₴",
    val cards: List<CardAdminConfig> = listOf(
        CardAdminConfig(
            name = "Чорна картка",
            balanceDisplay = "102 144 ₴",
            cardNumber = "4874 1000 2512 5553",
            operations = listOf(
                CardOperationAdminConfig(title = "Steam", amount = "-100 ₴", dateLabel = "Сьогодні"),
                CardOperationAdminConfig(title = "537552****2293", amount = "-26 500 ₴", dateLabel = "Вчора"),
                CardOperationAdminConfig(title = "McDonald's", amount = "-556.00 ₴", dateLabel = "14 квітня 2026")
            )
        ),
        CardAdminConfig(
            name = "Біла картка",
            balanceDisplay = "41 220 ₴",
            cardNumber = "4444 2222 1111 0001",
            operations = emptyList()
        ),
        CardAdminConfig(
            name = "Третя картка",
            balanceDisplay = "1 240 $",
            cardNumber = "5555 3333 1111 0002",
            operations = emptyList()
        ),
        CardAdminConfig(
            name = "Четверта картка",
            balanceDisplay = "980 €",
            cardNumber = "6666 4444 1111 0003",
            operations = emptyList()
        )
    ),
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
    fun cardOrDefault(index: Int): CardAdminConfig =
        cards.getOrNull(index)
            ?: CardAdminConfig(
                name = "Картка ${index + 1}",
                balanceDisplay = balanceMain,
                cardNumber = "4874 1000 2512 5553",
                operations = emptyList()
            )
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

    fun updateProfileAvatar(path: String?) {
        state = state.copy(profileAvatarPath = path)
    }

    fun updateJar(index: Int, jar: JarAdminConfig) {
        if (index !in state.jars.indices) return
        state = state.copy(
            jars = state.jars.mapIndexed { i, j -> if (i == index) jar else j }
        )
    }

    fun updateCard(index: Int, card: CardAdminConfig) {
        if (index !in state.cards.indices) return
        state = state.copy(
            cards = state.cards.mapIndexed { i, c -> if (i == index) card else c }
        )
    }
}
