package com.konvert.app.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R

private val JarTxnDetailBg = Color(0xFF121212)
private val JarTxnDetailHeaderBlue = Color(0xFF7DAFFE)
private val JarTxnDetailCardBg = Color(0xFF262626)
private val JarTxnDetailMuted = Color(0xFF8E8E93)
private val JarTxnDetailIconGradStart = Color(0xFF7469FB)
private val JarTxnDetailIconGradEnd = Color(0xFF7BADFC)
private val JarTxnDetailWalletCircle = Color(0xFF8B6BFF)
private const val JarTxnTransferAsset = "operations_logos/transfer_purple.png"
private const val JarTxnCatTransferAsset = "operations_logos/cat_transfer.png"
private const val JarTxnWalletAsset = "operations_logos/Wallet2.png"
private const val JarTxnLinkAsset = "operations_logos/Link.png"
private const val JarTxnQuestionAsset = "operations_logos/Question.png"
private const val JarTxnHeaderCoinsAsset = "operations_logos/coins_bg1.png"
private const val JarTxnHeaderJarAsset = "operations_logos/jar_bg1.png"

@Composable
private fun rememberJarTransactionAssetBitmap(assetPath: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(assetPath) {
        runCatching {
            context.assets.open(assetPath).use { input ->
                BitmapFactory.decodeStream(input)?.asImageBitmap()
            }
        }.getOrNull()
    }
}

@Composable
internal fun JarTransactionDetailScreen(
    payload: JarTxnDetailPayload,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    val transferBitmap = rememberJarTransactionAssetBitmap(JarTxnTransferAsset)
    val catTransferBitmap = rememberJarTransactionAssetBitmap(JarTxnCatTransferAsset)
    val walletBitmap = rememberJarTransactionAssetBitmap(JarTxnWalletAsset)
    val linkBitmap = rememberJarTransactionAssetBitmap(JarTxnLinkAsset)
    val questionBitmap = rememberJarTransactionAssetBitmap(JarTxnQuestionAsset)
    val amountDisplay = remember(payload.amount) {
        if (payload.amount.any { it == '₴' || it == '$' || it == '€' }) payload.amount else "${payload.amount} ₴"
    }
    val isLinkTransaction = payload.badge.contains("посил", ignoreCase = true)
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
            ) {
                if (isLinkTransaction) {
                    JarTransactionHeaderPattern(modifier = Modifier.fillMaxSize())
                } else {
                    JarTransactionDotPattern(modifier = Modifier.fillMaxSize())
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = sheetInset)
                    .clip(RoundedCornerShape(topStart = sheetRadius, topEnd = sheetRadius))
                    .background(JarTxnDetailBg)
            ) {
                JarTransactionMainInfoBlock(
                    payload = payload,
                    amountDisplay = amountDisplay,
                    fromIcon = if (isLinkTransaction) catTransferBitmap else transferBitmap,
                    badgeBrush = badgeBrush,
                    showSideLines = isLinkTransaction
                )
                Spacer(modifier = Modifier.height(if (isLinkTransaction) 22.dp else 28.dp))
                JarTransactionBalanceCard(
                    walletBitmap = walletBitmap,
                    balance = payload.balance,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(if (isLinkTransaction) 18.dp else 0.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(JarTxnDetailCardBg)
                        .padding(bottom = 160.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { }
                            )
                            .padding(horizontal = 28.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (questionBitmap != null) {
                            Image(
                                bitmap = questionBitmap,
                                contentDescription = null,
                                modifier = Modifier.size(42.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                                contentDescription = null,
                                tint = Color.White.copy(0.9f),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = stringResource(R.string.jar_detail_ask_question),
                            color = Color.White,
                            fontSize = if (isLinkTransaction) 18.sp else 16.sp,
                            fontWeight = if (isLinkTransaction) FontWeight.Normal else FontWeight.Medium
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.padding(start = if (isLinkTransaction) 126.dp else 16.dp)
                    )
                }
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
                if (linkBitmap != null) {
                    Image(
                        bitmap = linkBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
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
}

@Composable
private fun JarTransactionMainInfoBlock(
    payload: JarTxnDetailPayload,
    amountDisplay: String,
    fromIcon: ImageBitmap?,
    badgeBrush: Brush,
    showSideLines: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JarTxnDetailCardBg)
            .padding(top = 44.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = payload.fromLabel,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(6.dp))
            if (fromIcon != null) {
                Image(
                    bitmap = fromIcon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        JarTransactionBadgeRow(
            text = payload.badge,
            brush = badgeBrush,
            showSideLines = showSideLines
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = payload.dateTime,
            color = JarTxnDetailMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = amountDisplay,
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun JarTransactionBadgeRow(
    text: String,
    brush: Brush,
    showSideLines: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (showSideLines) 28.dp else 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showSideLines) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.14f)
            )
            Spacer(modifier = Modifier.width(14.dp))
        }
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(brush)
                .padding(horizontal = 18.dp, vertical = if (showSideLines) 4.dp else 8.dp)
        )
        if (showSideLines) {
            Spacer(modifier = Modifier.width(14.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.14f)
            )
        }
    }
}

@Composable
private fun JarTransactionBalanceCard(
    walletBitmap: ImageBitmap?,
    balance: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(JarTxnDetailCardBg)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(JarTxnDetailWalletCircle),
            contentAlignment = Alignment.Center
        ) {
            if (walletBitmap != null) {
                Image(
                    bitmap = walletBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.jar_detail_balance_label),
                color = JarTxnDetailMuted,
                fontSize = 13.sp,
                lineHeight = 15.sp
            )
            Text(
                text = balance,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun JarTransactionHeaderPattern(modifier: Modifier = Modifier) {
    val coinsBitmap = rememberJarTransactionAssetBitmap(JarTxnHeaderCoinsAsset)
    val jarBitmap = rememberJarTransactionAssetBitmap(JarTxnHeaderJarAsset)
    if (coinsBitmap == null || jarBitmap == null) return

    BoxWithConstraints(modifier = modifier.clipToBounds()) {
        val iconSize = 16.dp
        val horizontalStep = 52.dp
        val verticalStep = 30.dp
        val columns = (maxWidth.value / horizontalStep.value).toInt() + 4
        val rows = (maxHeight.value / verticalStep.value).toInt() + 4

        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val bitmap = if ((row + column) % 2 == 0) coinsBitmap else jarBitmap
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .offset(
                            x = horizontalStep * column.toFloat() - 18.dp +
                                if (row % 2 == 0) 0.dp else horizontalStep / 2,
                            y = verticalStep * row.toFloat() - 16.dp
                        )
                        .size(iconSize)
                        .graphicsLayer(rotationZ = -45f, alpha = 0.22f),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun JarTransactionDotPattern(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.clipToBounds()) {
        val step = 32.dp
        val columns = (maxWidth.value / step.value).toInt() + 3
        val rows = (maxHeight.value / (step.value * 0.5f)).toInt() + 3
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = step * column.toFloat() - 4.dp +
                                if (row % 2 == 0) 0.dp else step / 2,
                            y = step * row.toFloat() * 0.5f + 8.dp
                        )
                        .size(5.6.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.09f))
                )
            }
        }
    }
}
