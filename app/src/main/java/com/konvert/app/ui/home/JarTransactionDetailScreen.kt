package com.konvert.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R

private val JarTxnDetailBg = Color(0xFF121212)
private val JarTxnDetailHeaderBlue = Color(0xFF6BA8E8)
private val JarTxnDetailCardBg = Color(0xFF1C1C1E)
private val JarTxnDetailMuted = Color(0xFF8E8E93)
private val JarTxnDetailIconGradStart = Color(0xFF4A7FE8)
private val JarTxnDetailIconGradEnd = Color(0xFF9B6DFF)
private val JarTxnDetailWalletCircle = Color(0xFF8B6BFF)

@Composable
internal fun JarTransactionDetailScreen(
    payload: JarTxnDetailPayload,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    val headerBlue = JarTxnDetailHeaderBlue
    val pinkH = 210.dp
    val sheetInset = 78.dp
    val sheetRadius = 28.dp
    val badgeBrush = remember {
        Brush.linearGradient(
            colors = listOf(JarTxnDetailIconGradStart, JarTxnDetailIconGradEnd),
            start = Offset.Zero,
            end = Offset(120f, 40f)
        )
    }
    val iconRingBrush = remember {
        Brush.linearGradient(
            colors = listOf(JarTxnDetailIconGradStart, JarTxnDetailIconGradEnd),
            start = Offset(0f, 0f),
            end = Offset(80f, 80f)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JarTxnDetailBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scroll)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pinkH)
                    .background(headerBlue)
                    .jarDetailHeaderBluePattern()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = sheetInset)
                    .clip(RoundedCornerShape(topStart = sheetRadius, topEnd = sheetRadius))
                    .background(JarTxnDetailBg)
            ) {
                Spacer(modifier = Modifier.height(44.dp))
                Text(
                    text = stringResource(payload.fromRes),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(payload.badgeRes),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(badgeBrush)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(payload.dateTimeRes),
                    color = JarTxnDetailMuted,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(payload.amountRes),
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(28.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(JarTxnDetailCardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(JarTxnDetailWalletCircle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.jar_detail_balance_label),
                                color = JarTxnDetailMuted,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(payload.balanceRes),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { }
                            )
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                            contentDescription = null,
                            tint = Color.White.copy(0.9f),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = stringResource(R.string.jar_detail_ask_question),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 4.dp, top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.jar_bank_back_cd),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = sheetInset - 28.dp)
                    .size(56.dp)
                    .shadow(10.dp, CircleShape, ambientColor = Color.Black.copy(0.35f))
                    .clip(CircleShape)
                    .background(iconRingBrush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Link,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

private fun Modifier.jarDetailHeaderBluePattern(): Modifier = drawWithContent {
    drawContent()
    val step = 32.dp.toPx()
    val dotR = 2.8.dp.toPx()
    var y = step * 0.4f
    while (y < size.height + step) {
        var x = -step * 0.15f
        var row = 0
        while (x < size.width + step) {
            val ox = if (row % 2 == 0) 0f else step * 0.5f
            drawCircle(
                color = Color.White.copy(alpha = 0.09f),
                radius = dotR,
                center = Offset(x + ox, y)
            )
            x += step
            row++
        }
        y += step * 0.5f
    }
}
