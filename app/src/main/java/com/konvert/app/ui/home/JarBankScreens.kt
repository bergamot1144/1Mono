package com.konvert.app.ui.home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FastForward
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.South
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
import com.konvert.app.admin.JarAdminConfig
import com.konvert.app.admin.LocalAppAdmin

/** Категорія поповнення в блоці «Ваші поповнення». */
internal enum class JarTopUpCategory {
    OneTime,
    RoundBalance,
    RoundExpense
}

/** Повноекранний флоу «Банка» з вкладки Накопичення. */
internal sealed class SavingsJarFlow {
    data object None : SavingsJarFlow()
    data class Detail(val jarIndex: Int) : SavingsJarFlow()
    data class Share(val jarIndex: Int) : SavingsJarFlow()
    data class TopUpTransactions(val jarIndex: Int, val category: JarTopUpCategory) : SavingsJarFlow()
    data class TransactionDetail(
        val jarIndex: Int,
        val category: JarTopUpCategory,
        val payload: JarTxnDetailPayload
    ) : SavingsJarFlow()
}

private val JarScreenBg = Color(0xFF121212)
private val JarHeaderPink = Color(0xFFB34A66)
private val JarIconCircle = Color(0xFF7A2F45)
private val JarCaptionMuted = Color(0xFF8E8E93)
private val JarActionCircleBg = Color(0xFF2C2C2E)
private val JarSquircleStart = Color(0xFFD64085)
private val JarSquircleEnd = Color(0xFFFCA471)
private val JarDividerLine = Color.White.copy(alpha = 0.12f)
private val JarMiniStatCardBg = Color(0xFF1C1C1E)
private val JarMonoLinkRed = Color(0xFFFF6568)

private val ShareScreenBg = Color(0xFF121212)
private val ShareCardBg = Color(0xFF1C1C1E)
private val ShareMuted = Color(0xFF8E8E93)
private val ShareInnerButtonBg = Color(0xFF2C2C2E)

private const val OperationsLogosPath = "operations_logos"
private val MonoLogoCandidates = listOf(
    "$OperationsLogosPath/monobank.png",
    "$OperationsLogosPath/monobank_negate (1).png"
)

@Composable
private fun rememberFirstJarAssetBitmap(paths: List<String>): ImageBitmap? {
    val context = LocalContext.current
    val key = paths.joinToString("\u241e")
    return remember(key) {
        for (path in paths) {
            val bmp = runCatching {
                context.assets.open(path).use { BitmapFactory.decodeStream(it)?.asImageBitmap() }
            }.getOrNull()
            if (bmp != null) return@remember bmp
        }
        null
    }
}

@Composable
internal fun JarBankDetailScreen(
    jarIndex: Int,
    onBack: () -> Unit,
    onShareJar: () -> Unit,
    onOpenTopUpCategory: (JarTopUpCategory) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    val monoLogo = rememberFirstJarAssetBitmap(MonoLogoCandidates)
    val admin = LocalAppAdmin.current
    val jar = admin?.state?.jarOrDefault(jarIndex) ?: JarAdminConfig()
    val pinkHeaderHeight = 210.dp
    val darkSheetTopInset = 78.dp
    val sheetTopRadius = 28.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(JarScreenBg)
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
                    .height(pinkHeaderHeight)
                    .background(JarHeaderPink)
                    .jarHeaderWatermarkPattern()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = darkSheetTopInset)
                    .clip(RoundedCornerShape(topStart = sheetTopRadius, topEnd = sheetTopRadius))
                    .background(JarScreenBg)
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = jar.name,
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(1.dp)
                            .background(JarDividerLine)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = jar.balanceDisplay,
                        color = Color.White,
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.jar_bank_withdrawn_fmt, jar.withdrawnDisplay),
                        color = JarCaptionMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    JarHeroSquircleWithGraphic(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                JarActionRow(
                    onShareJar = onShareJar,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(36.dp))

                JarStatisticsBlock(
                    monoLogo = monoLogo,
                    monoAmount = jar.statMonoDisplay,
                    flagAmount = jar.statFlagDisplay,
                    globeAmount = jar.statGlobeDisplay
                )

                Spacer(modifier = Modifier.height(22.dp))

                JarStatsMiniCardsRow()

                Spacer(modifier = Modifier.height(28.dp))

                JarYourTopUpsSection(onCategoryClick = onOpenTopUpCategory)

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
                    .offset(y = darkSheetTopInset - 28.dp)
                    .size(56.dp)
                    .shadow(8.dp, CircleShape, ambientColor = Color.Black.copy(0.35f))
                    .clip(CircleShape)
                    .background(JarIconCircle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Savings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

private fun Modifier.jarHeaderWatermarkPattern(): Modifier = drawWithContent {
    drawContent()
    val step = 34.dp.toPx()
    val jarR = 3.2.dp.toPx()
    val coinR = 2.4.dp.toPx()
    var y = step * 0.35f
    while (y < size.height + step) {
        var x = -step * 0.2f
        var row = 0
        while (x < size.width + step) {
            val ox = if (row % 2 == 0) 0f else step * 0.5f
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = jarR,
                center = Offset(x + ox, y)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.045f),
                radius = coinR,
                center = Offset(x + ox + step * 0.22f, y + step * 0.18f)
            )
            x += step
            row++
        }
        y += step * 0.55f
    }
}

@Composable
private fun JarHeroSquircleWithGraphic(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(38.dp)
    Box(
        modifier = modifier
            .shadow(20.dp, shape, ambientColor = Color.Black.copy(0.45f), spotColor = Color.Black.copy(0.5f))
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(JarSquircleStart, JarSquircleEnd),
                    start = Offset.Zero,
                    end = Offset(320f, 320f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        StylizedJarIllustration(
            modifier = Modifier
                .fillMaxWidth(0.62f)
                .height(200.dp)
        )
    }
}

@Composable
private fun StylizedJarIllustration(modifier: Modifier = Modifier) {
    val glass = Color.White.copy(alpha = 0.22f)
    val glassEdge = Color.White.copy(alpha = 0.45f)
    val lid = Color(0xFFD63D4A)
    val lidDark = Color(0xFF9E2A34)
    val coin = Color(0xFFE8C547)
    val coinShine = Color(0xFFFFF3C4)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val bodyW = w * 0.72f
            val bodyH = h * 0.62f
            val cx = w / 2f
            val neckW = bodyW * 0.38f
            val neckH = h * 0.08f
            val lidH = h * 0.12f
            val topY = h * 0.12f
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(lid, lidDark)),
                topLeft = Offset(cx - neckW / 2f - 4f, topY - 6f),
                size = Size(neckW + 8f, lidH + 6f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = glass,
                topLeft = Offset(cx - bodyW / 2f, topY + lidH),
                size = Size(bodyW, bodyH),
                cornerRadius = CornerRadius(18f, 18f)
            )
            drawRoundRect(
                color = glassEdge,
                topLeft = Offset(cx - bodyW / 2f + 2f, topY + lidH + 2f),
                size = Size(bodyW - 4f, bodyH * 0.35f),
                cornerRadius = CornerRadius(14f, 14f),
                alpha = 0.12f
            )
            drawRoundRect(
                color = Color.White.copy(0.08f),
                topLeft = Offset(cx - neckW / 2f, topY),
                size = Size(neckW, neckH),
                cornerRadius = CornerRadius(5f, 5f)
            )
            val coinY = topY + lidH + bodyH - this.size.height * 0.08f
            val coinR = size.minDimension * 0.055f
            listOf(-1.1f, 0f, 1.15f).forEachIndexed { i, xf ->
                val px = cx + xf * coinR * 2.1f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(coinShine, coin, Color(0xFFB8860B)),
                        center = Offset(px - coinR * 0.3f, coinY - coinR * 0.3f),
                        radius = coinR * 1.4f
                    ),
                    radius = coinR,
                    center = Offset(px, coinY + i * 0.5f)
                )
            }
        }
    }
}

private data class JarActionSpec(
    val labelRes: Int,
    val icon: ImageVector,
    val highlighted: Boolean,
    val onClick: () -> Unit
)

@Composable
private fun JarActionRow(
    onShareJar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentBrush = remember {
        Brush.linearGradient(listOf(JarSquircleStart, JarSquircleEnd))
    }
    val actions = remember(onShareJar) {
        listOf(
            JarActionSpec(R.string.jar_action_share_bank, Icons.Outlined.Share, true, onShareJar),
            JarActionSpec(R.string.jar_action_top_up, Icons.Outlined.FileDownload, false) {},
            JarActionSpec(R.string.jar_action_deductions, Icons.Outlined.CreditCard, false) {},
            JarActionSpec(R.string.jar_action_favorites, Icons.Outlined.StarOutline, false) {},
            JarActionSpec(R.string.jar_action_statement, Icons.AutoMirrored.Outlined.ViewList, false) {},
            JarActionSpec(R.string.jar_action_settings, Icons.Outlined.Settings, false) {},
            JarActionSpec(R.string.jar_action_withdraw_jar, Icons.Outlined.South, false) {},
            JarActionSpec(R.string.jar_action_break_jar, Icons.Outlined.Construction, false) {}
        )
    }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        items(
            items = actions,
            key = { it.labelRes }
        ) { spec ->
            JarActionCell(spec = spec, accentBrush = accentBrush)
        }
    }
}

@Composable
private fun JarActionCell(
    spec: JarActionSpec,
    accentBrush: Brush,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(76.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val innerCircle =
            Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(JarActionCircleBg)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = spec.onClick
                )
        if (spec.highlighted) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .border(width = 2.dp, brush = accentBrush, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = innerCircle, contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = spec.icon,
                        contentDescription = null,
                        tint = Color.White.copy(0.92f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        } else {
            Box(modifier = innerCircle, contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = spec.icon,
                    contentDescription = null,
                    tint = Color.White.copy(0.92f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(spec.labelRes),
            color = Color.White.copy(0.92f),
            fontSize = 11.sp,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun JarStatisticsBlock(
    monoLogo: ImageBitmap?,
    monoAmount: String,
    flagAmount: String,
    globeAmount: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.jar_stats_title),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            JarStatMono(amount = monoAmount, monoLogo = monoLogo)
            JarStatDivider()
            JarStatFlag(amount = flagAmount)
            JarStatDivider()
            JarStatGlobe(amount = globeAmount)
        }
    }
}

private data class JarMiniStatCardSpec(
    val icon: ImageVector,
    val iconCircle: Color,
    val titleRes: Int,
    val amountRes: Int,
    val width: Dp = 152.dp
)

@Composable
private fun JarStatsMiniCardsRow(modifier: Modifier = Modifier) {
    val cards = remember {
        listOf(
            JarMiniStatCardSpec(
                icon = Icons.Outlined.Link,
                iconCircle = Color(0xFF0A84FF),
                titleRes = R.string.jar_stats_card_link,
                amountRes = R.string.jar_stats_card_link_amount
            ),
            JarMiniStatCardSpec(
                icon = Icons.Outlined.Link,
                iconCircle = Color(0xFFE5656A),
                titleRes = R.string.jar_stats_card_regular,
                amountRes = R.string.jar_stats_card_regular_amount,
                width = 168.dp
            ),
            JarMiniStatCardSpec(
                icon = Icons.Outlined.Savings,
                iconCircle = Color(0xFFFFCC00),
                titleRes = R.string.jar_stats_card_other,
                amountRes = R.string.jar_stats_card_other_amount,
                width = 96.dp
            )
        )
    }
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        items(
            items = cards,
            key = { it.titleRes }
        ) { spec ->
            JarMiniStatCard(spec = spec)
        }
    }
}

@Composable
private fun JarMiniStatCard(spec: JarMiniStatCardSpec) {
    Column(
        modifier = Modifier
            .width(spec.width)
            .clip(RoundedCornerShape(16.dp))
            .background(JarMiniStatCardBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { }
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(spec.iconCircle),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = spec.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(spec.titleRes),
            color = JarCaptionMuted,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(spec.amountRes),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
private fun JarYourTopUpsSection(
    onCategoryClick: (JarTopUpCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.jar_topups_title),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.jar_topups_see_all),
                color = JarMonoLinkRed,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { }
                    )
                    .padding(vertical = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.jar_topups_subtitle),
            color = JarCaptionMuted,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        JarTopupRow(
            circleColor = Color(0xFF0A84FF),
            icon = Icons.Outlined.South,
            title = stringResource(R.string.jar_topup_onetime_title),
            subtitle = stringResource(R.string.jar_topup_onetime_sub),
            amount = stringResource(R.string.jar_topup_onetime_amount),
            onClick = { onCategoryClick(JarTopUpCategory.OneTime) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        JarTopupRow(
            circleColor = Color(0xFFFF9500),
            icon = Icons.Outlined.CreditCard,
            title = stringResource(R.string.jar_topup_round_balance_title),
            subtitle = stringResource(R.string.jar_topup_round_balance_sub),
            amount = stringResource(R.string.jar_topup_round_balance_amount),
            onClick = { onCategoryClick(JarTopUpCategory.RoundBalance) }
        )
        Spacer(modifier = Modifier.height(4.dp))
        JarTopupRow(
            circleColor = Color(0xFFAF52DE),
            icon = Icons.Outlined.FastForward,
            title = stringResource(R.string.jar_topup_round_expense_title),
            subtitle = stringResource(R.string.jar_topup_round_expense_sub),
            amount = stringResource(R.string.jar_topup_round_expense_amount),
            onClick = { onCategoryClick(JarTopUpCategory.RoundExpense) }
        )
    }
}

@Composable
private fun JarTopupRow(
    circleColor: Color,
    icon: ImageVector,
    title: String,
    subtitle: String,
    amount: String,
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
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
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
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = JarCaptionMuted,
                fontSize = 13.sp
            )
        }
        Text(
            text = amount,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun JarStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(28.dp)
            .background(Color.White.copy(0.12f))
    )
}

@Composable
private fun JarStatMono(amount: String, monoLogo: ImageBitmap?) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (monoLogo != null) {
                Image(
                    bitmap = monoLogo,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            } else {
                Text(
                    text = "m",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = amount,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun JarStatFlag(amount: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF005BBB))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color(0xFFFFD500))
            )
        }
        Text(
            text = amount,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun JarStatGlobe(amount: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = Icons.Outlined.Language,
            contentDescription = null,
            tint = Color.White.copy(0.85f),
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = amount,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
internal fun JarBankShareScreen(
    jarIndex: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()
    val admin = LocalAppAdmin.current
    val jar = admin?.state?.jarOrDefault(jarIndex) ?: JarAdminConfig()
    val subtitle = stringResource(R.string.jar_share_subtitle_fmt, jar.name, jar.targetDisplay)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ShareScreenBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scroll)
            .padding(bottom = 24.dp)
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
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.jar_share_title),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = ShareMuted,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShareTopCard(
                modifier = Modifier.weight(1f),
                iconBrush = Brush.linearGradient(
                    listOf(Color(0xFFFFA040), Color(0xFFFF3070), Color(0xFF7B5CFF)),
                    start = Offset.Zero,
                    end = Offset(120f, 120f)
                ),
                icon = { Icon(Icons.Outlined.Add, null, tint = Color.White, modifier = Modifier.size(26.dp)) },
                label = stringResource(R.string.jar_share_story_template)
            )
            ShareTopCard(
                modifier = Modifier.weight(1f),
                iconBrush = Brush.linearGradient(listOf(Color(0xFF0A84FF), Color(0xFF0A84FF))),
                icon = {
                    Icon(
                        Icons.Outlined.QrCode2,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = stringResource(R.string.jar_share_qr)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ShareRequisitesCard(
            linkValue = jar.jarLink,
            cardValue = jar.cardNumber,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        ShareDocRow(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ShareTopCard(
    iconBrush: Brush,
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(ShareCardBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { }
            )
            .padding(horizontal = 14.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(iconBrush),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun ShareRequisitesCard(
    linkValue: String,
    cardValue: String,
    modifier: Modifier = Modifier
) {
    val linkBrush = remember { Brush.linearGradient(listOf(Color(0xFF6B5CFF), Color(0xFF5B4DFF))) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ShareCardBg)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp)
    ) {
        Text(
            text = stringResource(R.string.jar_share_req_section),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        ShareReqRow(
            iconBg = linkBrush,
            icon = {
                Icon(Icons.Outlined.Link, null, tint = Color.White, modifier = Modifier.size(22.dp))
            },
            title = stringResource(R.string.jar_share_link_title),
            value = linkValue
        )
        Spacer(modifier = Modifier.height(18.dp))
        ShareReqRow(
            iconBg = Brush.linearGradient(listOf(Color(0xFF34C759), Color(0xFF28A745))),
            icon = {
                Icon(Icons.Outlined.CreditCard, null, tint = Color.White, modifier = Modifier.size(22.dp))
            },
            title = stringResource(R.string.jar_share_card_title),
            value = cardValue
        )
        Spacer(modifier = Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ShareInnerButtonBg)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { }
                )
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = stringResource(R.string.jar_share_button),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ShareReqRow(
    iconBg: Brush,
    icon: @Composable () -> Unit,
    title: String,
    value: String
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = stringResource(R.string.jar_share_copy_cd),
                    tint = ShareMuted,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = value,
                    color = ShareMuted,
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ShareDocRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ShareCardBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { }
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFB8A3E0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = stringResource(R.string.jar_share_doc_row),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
