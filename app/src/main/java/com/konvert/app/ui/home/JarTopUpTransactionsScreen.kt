package com.konvert.app.ui.home

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R

/** Дані для екрана деталей однієї транзакції (відкривається зі списку). */
internal data class JarTxnDetailPayload(
    @StringRes val fromRes: Int,
    @StringRes val badgeRes: Int,
    @StringRes val dateTimeRes: Int,
    @StringRes val amountRes: Int,
    @StringRes val balanceRes: Int
)

private val JarTxnScreenBg = Color(0xFF121212)
private val JarTxnDateMuted = Color(0xFF8E8E93)
private val JarTxnAmountGreen = Color(0xFF4CD964)
private val JarTxnBadgeBg = Color.Black

private sealed class JarStubTxnItem {
    data class DateHeader(@StringRes val textRes: Int) : JarStubTxnItem()
    data class Line(
        val fromWhiteCard: Boolean,
        @StringRes val amountRes: Int,
        @StringRes val dateTimeRes: Int,
        @StringRes val balanceRes: Int,
        @StringRes val badgeRes: Int
    ) : JarStubTxnItem()
}

@Composable
internal fun JarTopUpTransactionsScreen(
    jarIndex: Int,
    category: JarTopUpCategory,
    onBack: () -> Unit,
    onOpenTransaction: (JarTxnDetailPayload) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = remember(category) { stubItemsForCategory(category) }
    val (circleColor, rowIcon) = categoryVisual(category)
    val subtitleRes = categorySubtitleRes(category)
    val showHeaderDivider = category == JarTopUpCategory.OneTime

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
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
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
                        is JarStubTxnItem.Line ->
                            "l_${item.amountRes}_${item.dateTimeRes}_${item.balanceRes}_$index"
                    }
                }
            ) { _, item ->
                when (item) {
                    is JarStubTxnItem.DateHeader -> {
                        JarTxnDateHeader(text = stringResource(item.textRes))
                    }
                    is JarStubTxnItem.Line -> {
                        JarTxnRow(
                            primaryLabel = stringResource(
                                if (item.fromWhiteCard) {
                                    R.string.jar_txn_from_white_card
                                } else {
                                    R.string.jar_txn_from_black_card
                                }
                            ),
                            subtitle = stringResource(subtitleRes),
                            amount = stringResource(item.amountRes),
                            circleColor = circleColor,
                            icon = rowIcon,
                            onClick = {
                                onOpenTransaction(
                                    JarTxnDetailPayload(
                                        fromRes = R.string.jar_detail_from_sender,
                                        badgeRes = item.badgeRes,
                                        dateTimeRes = item.dateTimeRes,
                                        amountRes = item.amountRes,
                                        balanceRes = item.balanceRes
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

@Composable
private fun JarTxnDateHeader(text: String) {
    Text(
        text = text,
        color = JarTxnDateMuted,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp)
    )
}

@Composable
private fun JarTxnRow(
    primaryLabel: String,
    subtitle: String,
    amount: String,
    circleColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
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
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
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
                Text(
                    text = "₴",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = (-0.5).dp)
                )
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
}

@StringRes
private fun categorySubtitleRes(c: JarTopUpCategory): Int = when (c) {
    JarTopUpCategory.OneTime -> R.string.jar_topup_onetime_title
    JarTopUpCategory.RoundBalance -> R.string.jar_topup_round_balance_title
    JarTopUpCategory.RoundExpense -> R.string.jar_topup_round_expense_title
}

private fun categoryVisual(c: JarTopUpCategory): Pair<Color, ImageVector> = when (c) {
    JarTopUpCategory.OneTime -> Color(0xFF0A84FF) to Icons.Outlined.South
    JarTopUpCategory.RoundBalance -> Color(0xFFD97B67) to Icons.Outlined.CreditCard
    JarTopUpCategory.RoundExpense -> Color(0xFFAF52DE) to Icons.Outlined.FastForward
}

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
    }
}
