package com.konvert.app.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.South
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
import com.konvert.app.admin.JarTopUpTransactionAdminConfig
import com.konvert.app.admin.LocalAppAdmin

/** Дані для екрана деталей однієї транзакції (відкривається зі списку). */
internal data class JarTxnDetailPayload(
    val fromLabel: String,
    val badge: String,
    val dateTime: String,
    val amount: String,
    val balance: String
)

private val JarTxnScreenBg = Color(0xFF121212)
private val JarTxnDateMuted = Color(0xFF8E8E93)
private val JarTxnAmountGreen = Color(0xFF4CD964)
private val JarTxnBadgeBg = Color.Black
private const val JarTxnOperationsLogosPath = "operations_logos"
private const val JarTxnOneTimeAsset = "$JarTxnOperationsLogosPath/transfer_purple.png"
private const val JarTxnRoundBalanceAsset = "$JarTxnOperationsLogosPath/split.png"
private const val JarTxnRoundExpenseAsset = "$JarTxnOperationsLogosPath/repeat.png"
private const val JarTxnCatAsset = "$JarTxnOperationsLogosPath/cat_icon.png"
private const val JarTxnLinkCircleAsset = "$JarTxnOperationsLogosPath/Link.png"
private const val JarTxnCatTransferAsset = "$JarTxnOperationsLogosPath/cat_transfer.png"

@Composable
private fun rememberJarTxnAssetBitmap(assetPath: String?): ImageBitmap? {
    val context = LocalContext.current
    return remember(assetPath) {
        assetPath?.let { path ->
            runCatching {
                context.assets.open(path).use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }
}

private sealed class JarStubTxnItem {
    data class DateHeader(@StringRes val textRes: Int) : JarStubTxnItem()
    data class DateHeaderText(val text: String) : JarStubTxnItem()
    data class Line(
        val fromWhiteCard: Boolean,
        @StringRes val amountRes: Int,
        @StringRes val dateTimeRes: Int,
        @StringRes val balanceRes: Int,
        @StringRes val badgeRes: Int
    ) : JarStubTxnItem()
    data class AdminLine(val transaction: JarTopUpTransactionAdminConfig) : JarStubTxnItem()
}

@Composable
internal fun JarTopUpTransactionsScreen(
    jarIndex: Int,
    category: JarTopUpCategory,
    onBack: () -> Unit,
    onOpenTransaction: (JarTxnDetailPayload) -> Unit,
    modifier: Modifier = Modifier
) {
    val admin = LocalAppAdmin.current
    val jar = admin?.state?.jarOrDefault(jarIndex)
    val items = remember(category, jar?.linkTransactions, jar?.cardNumberTransactions) {
        when (category) {
            JarTopUpCategory.Link ->
                adminItemsForCategory(normalizedLinkTransactions(jar?.linkTransactions.orEmpty()), stubLinkTransactions())
            JarTopUpCategory.CardNumber ->
                adminItemsForCategory(jar?.cardNumberTransactions.orEmpty(), stubCardNumberTransactions())
            else -> stubItemsForCategory(category)
        }
    }
    val (circleColor, rowIcon) = categoryVisual(category)
    val rowIconAsset = categoryAssetPath(category)
    val catTransferAsset = if (category == JarTopUpCategory.Link) JarTxnCatTransferAsset else null
    val subtitleRes = categorySubtitleRes(category)
    val showHeaderDivider = category == JarTopUpCategory.OneTime || category == JarTopUpCategory.Link
    val fromLabel = stringResource(R.string.jar_detail_from_sender)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JarTxnScreenBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.jar_bank_back_cd),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = stringResource(categoryTitleRes(category)),
            color = Color.White,
            fontSize = if (category == JarTopUpCategory.Link) 24.sp else 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = if (category == JarTopUpCategory.Link) 18.dp else 4.dp
            )
        )
        if (showHeaderDivider) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 0.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.10f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { index, item ->
                    when (item) {
                        is JarStubTxnItem.DateHeader -> "d_${item.textRes}_$index"
                        is JarStubTxnItem.DateHeaderText -> "dt_${item.text}_$index"
                        is JarStubTxnItem.Line ->
                            "l_${item.amountRes}_${item.dateTimeRes}_${item.balanceRes}_$index"
                        is JarStubTxnItem.AdminLine ->
                            "a_${item.transaction.dateLabel}_${item.transaction.amount}_$index"
                    }
                }
            ) { _, item ->
                when (item) {
                    is JarStubTxnItem.DateHeader -> {
                        JarTxnDateHeader(text = stringResource(item.textRes), large = category == JarTopUpCategory.Link)
                    }
                    is JarStubTxnItem.DateHeaderText -> {
                        JarTxnDateHeader(text = item.text, large = category == JarTopUpCategory.Link)
                    }
                    is JarStubTxnItem.Line -> {
                        val primaryLabel = stringResource(
                            if (item.fromWhiteCard) {
                                R.string.jar_txn_from_white_card
                            } else {
                                R.string.jar_txn_from_black_card
                            }
                        )
                        val subtitle = stringResource(subtitleRes)
                        val amount = stringResource(item.amountRes)
                        val badge = stringResource(item.badgeRes)
                        val dateTime = stringResource(item.dateTimeRes)
                        val balance = stringResource(item.balanceRes)
                        JarTxnRow(
                            primaryLabel = primaryLabel,
                            subtitle = subtitle,
                            amount = amount,
                            circleColor = circleColor,
                            icon = rowIcon,
                            iconAssetPath = rowIconAsset,
                            onClick = {
                                onOpenTransaction(
                                    JarTxnDetailPayload(
                                        fromLabel = fromLabel,
                                        badge = badge,
                                        dateTime = dateTime,
                                        amount = amount,
                                        balance = balance
                                    )
                                )
                            }
                        )
                    }
                    is JarStubTxnItem.AdminLine -> {
                        val txn = item.transaction
                        val badge = stringResource(categoryTitleRes(category))
                        if (category == JarTopUpCategory.Link) {
                            JarTxnLinkRow(
                                signature = txn.signature,
                                amount = txn.amount,
                                iconAssetPath = rowIconAsset,
                                catAssetPath = catTransferAsset,
                                onClick = {
                                    onOpenTransaction(
                                        JarTxnDetailPayload(
                                            fromLabel = fromLabel,
                                            badge = badge,
                                            dateTime = txn.dateTimeDisplay,
                                            amount = txn.amount,
                                            balance = txn.balanceAfter
                                        )
                                    )
                                }
                            )
                        } else {
                            JarTxnRow(
                                primaryLabel = txn.senderTitle,
                                subtitle = txn.signature,
                                amount = txn.amount,
                                circleColor = circleColor,
                                icon = rowIcon,
                                iconAssetPath = rowIconAsset,
                                onClick = {
                                    onOpenTransaction(
                                        JarTxnDetailPayload(
                                            fromLabel = fromLabel,
                                            badge = badge,
                                            dateTime = txn.dateTimeDisplay,
                                            amount = txn.amount,
                                            balance = txn.balanceAfter
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JarTxnDateHeader(text: String, large: Boolean = false) {
    Text(
        text = text,
        color = JarTxnDateMuted,
        fontSize = if (large) 18.sp else 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = if (large) 28.dp else 18.dp,
                bottom = if (large) 22.dp else 18.dp
            )
    )
}

@Composable
private fun JarTxnLinkRow(
    signature: String,
    amount: String,
    iconAssetPath: String?,
    catAssetPath: String?,
    onClick: () -> Unit
) {
    val iconBitmap = rememberJarTxnAssetBitmap(iconAssetPath)
    val catBitmap = rememberJarTxnAssetBitmap(catAssetPath)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Link,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Від:",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (catBitmap != null) {
                Image(
                    bitmap = catBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }
            if (signature.isNotBlank() && signature != "cat_transfer.png") {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = signature,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Text(
            text = amount,
            color = JarTxnAmountGreen,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun JarTxnRow(
    primaryLabel: String,
    subtitle: String,
    amount: String,
    circleColor: Color,
    icon: ImageVector,
    iconAssetPath: String? = null,
    badgeAssetPath: String? = null,
    onClick: () -> Unit
) {
    val iconBitmap = rememberJarTxnAssetBitmap(iconAssetPath)
    val badgeBitmap = rememberJarTxnAssetBitmap(badgeAssetPath)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(circleColor),
                contentAlignment = Alignment.Center
            ) {
                if (iconBitmap != null) {
                    Image(
                        bitmap = iconBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(JarTxnBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                if (badgeBitmap != null) {
                    Image(
                        bitmap = badgeBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                    text = "₴",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-0.5).dp)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = primaryLabel,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = JarTxnDateMuted,
                fontSize = 14.sp
            )
        }
        Text(
            text = amount,
            color = JarTxnAmountGreen,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@StringRes
private fun categoryTitleRes(c: JarTopUpCategory): Int = when (c) {
    JarTopUpCategory.OneTime -> R.string.jar_topup_onetime_title
    JarTopUpCategory.RoundBalance -> R.string.jar_topup_round_balance_title
    JarTopUpCategory.RoundExpense -> R.string.jar_topup_round_expense_title
    JarTopUpCategory.Link -> R.string.jar_stats_card_link
    JarTopUpCategory.CardNumber -> R.string.jar_stats_card_direct
}

@StringRes
private fun categorySubtitleRes(c: JarTopUpCategory): Int = when (c) {
    JarTopUpCategory.OneTime -> R.string.jar_topup_onetime_title
    JarTopUpCategory.RoundBalance -> R.string.jar_topup_round_balance_title
    JarTopUpCategory.RoundExpense -> R.string.jar_topup_round_expense_title
    JarTopUpCategory.Link -> R.string.jar_stats_card_link
    JarTopUpCategory.CardNumber -> R.string.jar_share_card_title
}

private fun categoryVisual(c: JarTopUpCategory): Pair<Color, ImageVector> = when (c) {
    JarTopUpCategory.OneTime -> Color(0xFF0A84FF) to Icons.Outlined.South
    JarTopUpCategory.RoundBalance -> Color(0xFFD97B67) to Icons.Outlined.CreditCard
    JarTopUpCategory.RoundExpense -> Color(0xFFAF52DE) to Icons.Outlined.FastForward
    JarTopUpCategory.Link -> Color.Transparent to Icons.Outlined.Link
    JarTopUpCategory.CardNumber -> Color.Transparent to Icons.Outlined.CreditCard
}

private fun categoryAssetPath(c: JarTopUpCategory): String = when (c) {
    JarTopUpCategory.OneTime -> JarTxnOneTimeAsset
    JarTopUpCategory.RoundBalance -> JarTxnRoundBalanceAsset
    JarTopUpCategory.RoundExpense -> JarTxnRoundExpenseAsset
    JarTopUpCategory.Link -> JarTxnLinkCircleAsset
    JarTopUpCategory.CardNumber -> JarTxnCatAsset
}

private fun adminItemsForCategory(
    transactions: List<JarTopUpTransactionAdminConfig>,
    fallback: List<JarTopUpTransactionAdminConfig>
): List<JarStubTxnItem> {
    val source = transactions.ifEmpty { fallback }
    return source
        .groupBy { it.dateLabel.ifBlank { "Сьогодні" } }
        .flatMap { (date, rows) ->
            listOf(JarStubTxnItem.DateHeaderText(date)) + rows.map { JarStubTxnItem.AdminLine(it) }
        }
}

private fun normalizedLinkTransactions(
    transactions: List<JarTopUpTransactionAdminConfig>
): List<JarTopUpTransactionAdminConfig> {
    val only = transactions.singleOrNull() ?: return transactions
    val legacyDefault = only.amount.filter(Char::isDigit).startsWith("46501") &&
        only.signature != "cat_transfer.png"
    return if (legacyDefault) emptyList() else transactions
}

private fun stubLinkTransactions(): List<JarTopUpTransactionAdminConfig> = listOf(
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "15.00",
        dateLabel = "7 травня",
        dateTimeDisplay = "7 травня 2026, 13:13",
        balanceAfter = "46 501 ₴"
    ),
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "100.00",
        dateLabel = "7 травня",
        dateTimeDisplay = "7 травня 2026, 13:13",
        balanceAfter = "46 486 ₴"
    ),
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "15.00",
        dateLabel = "21 квітня",
        dateTimeDisplay = "21 квітня 2026, 13:13",
        balanceAfter = "46 386 ₴"
    ),
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "12.00",
        dateLabel = "9 квітня",
        dateTimeDisplay = "9 квітня 2026, 13:13",
        balanceAfter = "46 371 ₴"
    ),
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "12.00",
        dateLabel = "6 квітня",
        dateTimeDisplay = "6 квітня 2026, 13:13",
        balanceAfter = "46 359 ₴"
    ),
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "15.00",
        dateLabel = "6 квітня",
        dateTimeDisplay = "6 квітня 2026, 13:13",
        balanceAfter = "46 347 ₴"
    ),
    JarTopUpTransactionAdminConfig(
        senderTitle = "Від:",
        signature = "cat_transfer.png",
        amount = "15.00",
        dateLabel = "6 квітня",
        dateTimeDisplay = "6 квітня 2026, 13:13",
        balanceAfter = "46 332 ₴"
    )
)

private fun stubCardNumberTransactions(): List<JarTopUpTransactionAdminConfig> = listOf(
    JarTopUpTransactionAdminConfig(
        senderTitle = "З Білої картки",
        signature = "Номер картки банки",
        amount = "10 370.00 ₴",
        dateLabel = "Сьогодні",
        dateTimeDisplay = "12 травня 2026, 13:13",
        balanceAfter = "10 370.00 ₴"
    )
)

private fun stubItemsForCategory(category: JarTopUpCategory): List<JarStubTxnItem> {
    val link = R.string.jar_detail_badge_link
    val rb = R.string.jar_topup_round_balance_title
    val re = R.string.jar_topup_round_expense_title
    val ot = R.string.jar_topup_onetime_title
    return when (category) {
        JarTopUpCategory.OneTime -> listOf(
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_feb17_2021),
            JarStubTxnItem.Line(
                fromWhiteCard = false,
                amountRes = R.string.jar_txn_amt_15000,
                dateTimeRes = R.string.jar_detail_dt_feb17_1105,
                balanceRes = R.string.jar_detail_balance_166,
                badgeRes = ot
            )
        )
        JarTopUpCategory.RoundBalance -> listOf(
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_apr12),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_667, R.string.jar_detail_dt_apr12_1015, R.string.jar_detail_balance_154, link),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_058, R.string.jar_detail_dt_apr12_0922, R.string.jar_detail_balance_147, link),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_600, R.string.jar_detail_dt_apr12_0922, R.string.jar_detail_balance_145, link),
            JarStubTxnItem.Line(false, R.string.jar_txn_amt_196, R.string.jar_detail_dt_apr12_0922, R.string.jar_detail_balance_143, rb),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_014, R.string.jar_detail_dt_apr12_0922, R.string.jar_detail_balance_141, rb),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_034, R.string.jar_detail_dt_apr12_0922, R.string.jar_detail_balance_139, rb),
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_apr9),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_1200, R.string.jar_detail_dt_apr9_1344, R.string.jar_detail_balance_166, link),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_041, R.string.jar_detail_dt_apr9_1800, R.string.jar_detail_balance_137, rb),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_020, R.string.jar_detail_dt_apr9_1800, R.string.jar_detail_balance_135, rb),
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_apr8),
            JarStubTxnItem.Line(false, R.string.jar_txn_amt_112, R.string.jar_detail_dt_apr8_1200, R.string.jar_detail_balance_133, rb),
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_apr7),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_089, R.string.jar_detail_dt_apr7_0830, R.string.jar_detail_balance_131, rb)
        )
        JarTopUpCategory.RoundExpense -> listOf(
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_mar15),
            JarStubTxnItem.Line(false, R.string.jar_txn_amt_045, R.string.jar_detail_dt_mar15_1430, R.string.jar_detail_balance_166, re),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_034, R.string.jar_detail_dt_mar15_1430, R.string.jar_detail_balance_154, re),
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_apr9),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_020, R.string.jar_detail_dt_apr9_1800, R.string.jar_detail_balance_147, re),
            JarStubTxnItem.Line(false, R.string.jar_txn_amt_196, R.string.jar_detail_dt_apr9_1800, R.string.jar_detail_balance_145, re),
            JarStubTxnItem.DateHeader(R.string.jar_txn_date_apr8),
            JarStubTxnItem.Line(true, R.string.jar_txn_amt_058, R.string.jar_detail_dt_apr8_1200, R.string.jar_detail_balance_143, re)
        )
        JarTopUpCategory.Link -> adminItemsForCategory(emptyList(), stubLinkTransactions())
        JarTopUpCategory.CardNumber -> adminItemsForCategory(emptyList(), stubCardNumberTransactions())
    }
}
