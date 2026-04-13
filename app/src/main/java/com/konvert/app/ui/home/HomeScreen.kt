package com.konvert.app.ui.home

import android.graphics.BitmapFactory
import kotlin.math.floor
import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.lerp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
import com.konvert.app.ui.home.tabs.CardsTabScreen
import com.konvert.app.ui.home.tabs.CreditsTabScreen
import com.konvert.app.ui.home.tabs.MarketTabScreen
import com.konvert.app.ui.home.tabs.MoreTabScreen
import com.konvert.app.ui.home.tabs.SavingsTabScreen
import com.konvert.app.ui.theme.AccentRed
import com.konvert.app.ui.theme.AvatarPlaceholder
import com.konvert.app.ui.theme.HomeBalanceBarTint
import com.konvert.app.ui.theme.HomeUsefulCardColor
import com.konvert.app.ui.theme.HomeUsefulTileFill
import com.konvert.app.ui.theme.HomeNavIconInactive
import com.konvert.app.ui.theme.KeypadButton
import com.konvert.app.ui.theme.LimitsGradientEnd
import com.konvert.app.ui.theme.LimitsGradientStart
import com.konvert.app.ui.theme.HomeBottomBarBorder
import com.konvert.app.ui.theme.HomeBottomBarFill
import com.konvert.app.ui.theme.HomeOperationsAllChipBackground
import com.konvert.app.ui.theme.HomeOperationsAllChipText
import com.konvert.app.ui.theme.HomeQuickActionCaptionColor
import com.konvert.app.ui.theme.OperationsBlockColor
import com.konvert.app.ui.theme.PinPromptText
import com.konvert.app.ui.theme.QuickActionCircleFill
import com.konvert.app.ui.theme.QuickActionIconTint
import com.konvert.app.ui.theme.TextPrimary
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition

private val CardShape = RoundedCornerShape(24.dp)
private val ChipShape = RoundedCornerShape(20.dp)
/** Капсула «Усі ›» — великий радіус (візуально як на референсі). */
private val OperationsAllChipShape = RoundedCornerShape(999.dp)
private val ActionCircleSize = 56.dp

/** Висота нижньої «пігулки» та Маркету — радіус = половина висоти (капсула / коло). */
private val BottomBarHeight = 76.dp
private val BottomBarPillRadius = 38.dp
private val BottomBarGap = 4.dp

/** Lottie в меню; статична іконка «Маркет» трохи менша. */
private val BottomBarNavLottieIconSize = 36.dp
private val BottomBarNavStaticIconSize = 28.dp

/** Відступ підпису від нижнього краю комірки (малий — текст ближче до іконки). */
private val BottomBarNavLabelBottomPadding = 2.dp

/** Підписи в нижній панелі: `roboto_bold` на всю ширину гліфа (див. [BottomBarNavLabelFontFamily]). */
private val BottomBarNavLabelFontSize = 11.75.sp
private val BottomBarNavLabelLineHeight = 14.25.sp
private val BottomBarNavLabelLetterSpacing = (-0.1).sp

private val BottomBarNavLabelFontFamily = FontFamily(
    Font(R.font.roboto_bold, FontWeight.Normal, FontStyle.Normal)
)

/** Запас під нижній бар + системна навігація (узгоджено з [BottomBarHeight]). */
private val HomeListBottomReserveBelowBars = 104.dp + (BottomBarHeight - 56.dp)

/** Слот каруселі картки + [HomeCardPlaceholder] — менша висота прибирає «повітря» під картою. */
private val HomeCardCarouselSlotHeight = 232.dp

/** Між каруселлю картки та чипом «Усі картки» — менше, щоб блок піднявся ближче до пластини. */
private val HomeSectionGapCarouselToAllCards = 8.dp
/** Між «Усі картки» та швидкими діями. */
private val HomeSectionGapAllCardsToQuick = 18.dp

/** Колір великого балансу [R.string.home_balance_main] — кешбек у топ-барі та сусідні іконки. */
private val HomeBalanceMainAmountColor = Color(0xFFFFFFFF)

/** `assets/kreditfont/Kredit Front.otf` — текст на картці (не логотип VISA). */
@Composable
private fun rememberKreditFrontFontFamily(): FontFamily {
    val assets = LocalContext.current.assets
    return remember(assets) {
        FontFamily(
            Font(
                path = "kreditfont/Kredit Front.otf",
                assetManager = assets,
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            )
        )
    }
}

private const val OperationsLogosPath = "operations_logos"

private const val CardVisaLogoAsset = "$OperationsLogosPath/visa_negate.png"
private const val CardMonobankNegateAsset = "$OperationsLogosPath/monobank_negate (1).png"
/** Іконка чипа «гаманець» біля [R.string.home_balance_wallet] (2 144 ₴). */
private const val CardWalletNegateAsset = "$OperationsLogosPath/wallet_negate.png"
private const val QuickActionBankCardNegateAsset = "$OperationsLogosPath/bank_card_negate.png"
/** Іконка чипа кредитного ліміту біля [R.string.home_balance_credit]. */
private const val CardAddDebitNegateAsset = "$OperationsLogosPath/add_debit_card_negate.png"
/** Іконка чипа «Усі картки» під каруселлю. */
private const val HomeAllCardsChipIconAsset = "$OperationsLogosPath/card_negate.png"

private const val HomeGraphNegateAsset = "graph_negate.png"
private const val HomeGraphNegateInOperationsLogos = "$OperationsLogosPath/graph_negate.png"

private const val HomeGiftBoxNegateAsset = "gift-box_negate.png"
private const val HomeGiftBoxNegateInOperationsLogos = "$OperationsLogosPath/gift-box_negate.png"

/** Шляхи до іконки кешбеку біля суми [R.string.home_bonus_amount] (різні імена файлів у збірках). */
private val HomeGiftBoxNegateAssetPaths = listOf(
    HomeGiftBoxNegateInOperationsLogos,
    HomeGiftBoxNegateAsset,
    "$OperationsLogosPath/gift_box_negate.png"
)

@Composable
private fun rememberAssetImageBitmap(assetPath: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(assetPath) {
        if (assetPath.isBlank()) null
        else runCatching {
            context.assets.open(assetPath).use { input ->
                BitmapFactory.decodeStream(input)?.asImageBitmap()
            }
        }.getOrNull()
    }
}

@Composable
private fun rememberFirstSuccessfulAssetBitmap(paths: List<String>): ImageBitmap? {
    val context = LocalContext.current
    val key = paths.joinToString("\u241e")
    return remember(key) {
        for (path in paths) {
            if (path.isBlank()) continue
            val bmp = runCatching {
                context.assets.open(path).use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            }.getOrNull()
            if (bmp != null) return@remember bmp
        }
        null
    }
}

/** Lottie «Картки» / «Кредити»: сегмент по тапу або після повернення — кадри 0…9 (10 кадрів). */
private const val NavLottieTapMaxFrameInclusive = 15

/** Lottie «Ще»: при тапі — кадри 0…14 (15 кадрів). */
private const val MoreNavTapMaxFrameInclusive = 16

/** Lottie «Накопичення»: при тапі — кадри 0…15 (16 кадрів). */
private const val SavingsNavTapMaxFrameInclusive = 15

private const val CardsNavLottieAsset = "animations/cards_icon.json"
private const val CreditsNavLottieAsset = "animations/credits_icon.json"
private const val MoreNavLottieAsset = "animations/more_icon.json"
private const val SavingsNavLottieAsset = "animations/deposits_dnm_16_quick.json"
private const val MarketNavLottieAsset = "animations/market20_icon.json"

/** Неактивна вкладка: приглушення Lottie (підпис уже HomeNavIconInactive). */
private fun Modifier.lottieNavInactiveGrayTint(selected: Boolean): Modifier =
    if (selected) this else alpha(0.42f)

/** Вертикальні палітри фону — по одній на кожну сторінку каруселі карт (узгоджено з [HomeCardsCarouselPageCount]). */
private val HomeBgMainPalettes: List<Array<Pair<Float, Color>>> = listOf(
    // Картка 1 — градієнт як у темі / макеті
    arrayOf(
        0.00f to Color(0xFF0B0D40),
        0.10f to Color(0xFF10194F),
        0.20f to Color(0xFF182961),
        0.32f to Color(0xFF12285A),
        0.42f to Color(0xFF0F2556),
        0.55f to Color(0xFF0D234C),
        0.68f to Color(0xFF0F1E3B),
        0.80f to Color(0xFF101727),
        0.90f to Color(0xFF121214),
        1.00f to Color(0xFF0C0C0C)
    ),
    // r2 — лілово-синій
    arrayOf(
        0.00f to Color(0xFF1A0F45),
        0.18f to Color(0xFF2E2568),
        0.34f to Color(0xFF3D3278),
        0.48f to Color(0xFF252050),
        0.70f to Color(0xFF100C24),
        0.82f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    // r3 — бірюзово-синій
    arrayOf(
        0.00f to Color(0xFF062A32),
        0.18f to Color(0xFF0D3D48),
        0.34f to Color(0xFF124A58),
        0.48f to Color(0xFF0A3038),
        0.70f to Color(0xFF041820),
        0.82f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    // r4 — смарагдовий
    arrayOf(
        0.00f to Color(0xFF062818),
        0.18f to Color(0xFF0C3D28),
        0.34f to Color(0xFF104832),
        0.48f to Color(0xFF082818),
        0.70f to Color(0xFF041210),
        0.82f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    // r5 — бордово-коричневий
    arrayOf(
        0.00f to Color(0xFF2A1010),
        0.18f to Color(0xFF451818),
        0.34f to Color(0xFF5A2220),
        0.48f to Color(0xFF381414),
        0.70f to Color(0xFF140A0A),
        0.82f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    )
)

private val HomeBgRadialTops: List<Color> = listOf(
    Color(0x332F6DFF),
    Color(0x337C6DFF),
    Color(0x3322D3EE),
    Color(0x334ADE80),
    Color(0x33FF6B6B)
)

private val HomeBgRadialMids: List<Color> = listOf(
    Color(0x1A4D8DFF),
    Color(0x1A6D5CFF),
    Color(0x1A2EE6A8),
    Color(0x1A6BCB7A),
    Color(0x1AFF8E53)
)

/** Затемнення зверху вниз: довше лишається прозорим, щоб не «тягнуло» середину екрана — перехід ближче до блоку «Операції». */
private val HomeBgOverlayStops: Array<Pair<Float, Color>> = arrayOf(
    0.00f to Color.Transparent,
    0.78f to Color.Transparent,
    0.86f to Color(0x44000000),
    0.93f to Color(0xCC121212),
    1.00f to Color(0xFF121212)
)

private fun colorAlongStops(stops: Array<Pair<Float, Color>>, fraction: Float): Color {
    if (stops.isEmpty()) return Color.Black
    val f = fraction.coerceIn(0f, 1f)
    if (f <= stops.first().first) return stops.first().second
    if (f >= stops.last().first) return stops.last().second
    var i = 0
    while (i < stops.lastIndex && stops[i + 1].first < f) i++
    val (t0, c0) = stops[i]
    val (t1, c1) = stops[i + 1]
    val span = t1 - t0
    if (span == 0f) return c0
    val u = ((f - t0) / span).coerceIn(0f, 1f)
    return lerp(c0, c1, u)
}

private fun lerpColorStops(
    a: Array<Pair<Float, Color>>,
    b: Array<Pair<Float, Color>>,
    t: Float
): Array<Pair<Float, Color>> {
    val tt = t.coerceIn(0f, 1f)
    val positions = (a.map { it.first } + b.map { it.first }).toSet().sorted()
    return Array(positions.size) { idx ->
        val p = positions[idx]
        p to lerp(colorAlongStops(a, p), colorAlongStops(b, p), tt)
    }
}

@Composable
fun StaticHomeBackground(
    modifier: Modifier = Modifier,
    /** Безперервна позиція каруселі: `currentPage + currentPageOffsetFraction` (0…paletteCount−1). */
    cardScrollPosition: Float = 0f,
    /** Скільки палітр використовувати (1 — лише перша, для інших вкладок). */
    cardBackgroundPaletteCount: Int = 1
) {
    val paletteN = cardBackgroundPaletteCount.coerceIn(1, HomeBgMainPalettes.size)
    val maxSlot = (paletteN - 1).coerceAtLeast(0).toFloat()
    val seg = cardScrollPosition.coerceIn(0f, maxSlot)
    val i = floor(seg).toInt().coerceIn(0, paletteN - 1)
    val next = (i + 1).coerceAtMost(paletteN - 1)
    val tLocal = if (next == i) 0f else (seg - i).coerceIn(0f, 1f)
    val mainStops = lerpColorStops(HomeBgMainPalettes[i], HomeBgMainPalettes[next], tLocal)
    val radialTop = lerp(HomeBgRadialTops[i], HomeBgRadialTops[next], tLocal)
    val radialMid = lerp(HomeBgRadialMids[i], HomeBgRadialMids[next], tLocal)

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val width = size.width
                val height = size.height

                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = mainStops,
                        startY = 0f,
                        endY = height
                    ),
                    size = size
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(radialTop, Color.Transparent),
                        center = Offset(width * 0.50f, height * 0.22f),
                        radius = height * 0.42f
                    ),
                    radius = height * 0.42f,
                    center = Offset(width * 0.50f, height * 0.22f)
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(radialMid, Color.Transparent),
                        center = Offset(width * 0.50f, height * 0.40f),
                        radius = height * 0.26f
                    ),
                    radius = height * 0.26f,
                    center = Offset(width * 0.50f, height * 0.40f)
                )

                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = HomeBgOverlayStops,
                        startY = 0f,
                        endY = height
                    ),
                    size = size
                )
            }
    )
}

@Composable
fun HomeScreen() {
    var selectedBottomTab by remember { mutableStateOf(HomeBottomNavTab.Cards) }
    val bottomBarLiftDp = 12.dp
    /** Додатковий зсиг униз на 10 px екрана (щодо попереднього положення). */
    val bottomBarDownFromPx10 = (10f / LocalDensity.current.density).dp

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedBottomTab) {
            HomeBottomNavTab.Cards -> CardsTabScreen()
            HomeBottomNavTab.Credits -> CreditsTabScreen()
            HomeBottomNavTab.Savings -> SavingsTabScreen()
            HomeBottomNavTab.More -> MoreTabScreen()
            HomeBottomNavTab.Market -> MarketTabScreen()
        }

        HomeBottomBar(
            selectedTab = selectedBottomTab,
            onTabSelected = { selectedBottomTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .offset(y = -bottomBarLiftDp + bottomBarDownFromPx10)
                .padding(bottom = 2.dp)
        )
    }
}

/** Кількість карт у каруселі (= ach/r1…r5); фон інтерполюється між сусідніми палітрами під час свайпу. */
internal const val HomeCardsCarouselPageCount = 5

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeCardsTabDashboard(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val firstScreenMinHeight =
            maxHeight - HomeListBottomReserveBelowBars - BottomBarHeight - 12.dp
        val balanceSectionTopPadding =
            56.dp + with(LocalDensity.current) { (130f / density).dp }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    val overlayH = 200.dp.toPx()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.Transparent,
                                0.5f to Color(0xFF000000).copy(alpha = 0.07f),
                                1f to Color(0xFF000000).copy(alpha = 0.24f)
                            ),
                            startY = size.height - overlayH,
                            endY = size.height
                        ),
                        topLeft = Offset(0f, size.height - overlayH),
                        size = Size(size.width, overlayH)
                    )
                },
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = HomeListBottomReserveBelowBars
            )
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = firstScreenMinHeight.coerceAtLeast(0.dp))
                ) {
                    HomeTopBar(onProfileClick = { /* далі */ })
                    Spacer(modifier = Modifier.height(balanceSectionTopPadding))
                    HomeBalanceBlock()
                    HomeCardsCarousel(pagerState = pagerState)
                    Spacer(modifier = Modifier.height(HomeSectionGapCarouselToAllCards))
                    HomeAllCardsChip()
                    Spacer(modifier = Modifier.height(HomeSectionGapAllCardsToQuick))
                    HomeQuickActions()
                    Spacer(modifier = Modifier.height(10.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    HomeOperationsCard()
                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { HomeLimitsAbroadCard() }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { HomeUsefulCard() }
            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}

@Composable
private fun HomeTopBar(onProfileClick: () -> Unit) {
    val graphNegateRoot = rememberAssetImageBitmap(HomeGraphNegateAsset)
    val graphNegateOps = rememberAssetImageBitmap(HomeGraphNegateInOperationsLogos)
    val graphNegateBitmap = graphNegateRoot ?: graphNegateOps
    val chartsCd = stringResource(R.string.home_charts_cd)
    val bonusAmountText = stringResource(R.string.home_bonus_amount)
    val cashbackChipSemantics =
        stringResource(R.string.home_cashback_chip_semantics, bonusAmountText)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(AvatarPlaceholder)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onProfileClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(R.string.home_profile_cd),
                tint = HomeBalanceMainAmountColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = stringResource(R.string.home_chat_cd),
                tint = HomeBalanceMainAmountColor,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier
                    .clearAndSetSemantics {
                        contentDescription = cashbackChipSemantics
                    }
                    .clip(ChipShape)
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                HomeCashbackGiftIcon(modifier = Modifier.size(26.dp))
                Text(
                    text = bonusAmountText,
                    color = HomeBalanceMainAmountColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                contentAlignment = Alignment.Center
            ) {
                if (graphNegateBitmap != null) {
                    Image(
                        bitmap = graphNegateBitmap,
                        contentDescription = chartsCd,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(HomeBalanceMainAmountColor)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = chartsCd,
                        tint = HomeBalanceMainAmountColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeBalanceAddChip(modifier: Modifier = Modifier) {
    val addCd = stringResource(R.string.home_balance_add_cd)
    Box(
        modifier = modifier
            .size(40.dp)
            .semantics { contentDescription = addCd }
            .clip(CircleShape)
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawBehind {
                val c = Offset(size.width * 0.5f, size.height * 0.5f)
                /* Усі кола та плюс — на 10% менші від попередніх пропорцій. */
                val circleScale = 0.9f
                val rOuter = size.minDimension * 0.5f * circleScale
                /* Внутрішній диск менший за зовнішній (22/40 від rOuter). */
                val rInner = rOuter * (22f / 40f)
                /* Кожен елемент ще на ~10% прозоріший (альфа ×0.9). */
                val outerAlpha = 0.25f * 0.9f
                drawCircle(
                    color = Color.White.copy(alpha = outerAlpha),
                    radius = rOuter,
                    center = c
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.75f * 0.9f * 0.9f),
                    radius = rInner,
                    center = c
                )
                /* Плюс у тому ж масштабі, що й кола. */
                val armHalf = size.minDimension * (5f / 40f) * circleScale
                val stroke = size.minDimension * (3.2f / 40f) * circleScale
                val cr = CornerRadius(stroke * 0.5f, stroke * 0.5f)
                val plusPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = c.x - armHalf,
                            top = c.y - stroke * 0.5f,
                            right = c.x + armHalf,
                            bottom = c.y + stroke * 0.5f,
                            cornerRadius = cr
                        )
                    )
                    addRoundRect(
                        RoundRect(
                            left = c.x - stroke * 0.5f,
                            top = c.y - armHalf,
                            right = c.x + stroke * 0.5f,
                            bottom = c.y + armHalf,
                            cornerRadius = cr
                        )
                    )
                }
                drawPath(
                    path = plusPath,
                    color = Color.Black.copy(alpha = 0.9f),
                    blendMode = BlendMode.DstOut
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    )
}

@Composable
private fun HomeBalanceBlock() {
    val walletChipIcon = rememberAssetImageBitmap(CardWalletNegateAsset)
    val creditChipIcon = rememberAssetImageBitmap(CardAddDebitNegateAsset)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HomeBalanceAddChip()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.home_balance_main),
                color = HomeBalanceMainAmountColor,
                fontSize = 43.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BalanceChip(
                icon = {
                    if (walletChipIcon != null) {
                        Image(
                            bitmap = walletChipIcon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(HomeBalanceBarTint)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = HomeBalanceBarTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                text = stringResource(R.string.home_balance_wallet)
            )
            BalanceChip(
                icon = {
                    if (creditChipIcon != null) {
                        Image(
                            bitmap = creditChipIcon,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(HomeBalanceBarTint)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.CreditCard,
                            contentDescription = null,
                            tint = HomeBalanceBarTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                text = stringResource(R.string.home_balance_credit)
            )
        }
    }
}

@Composable
private fun BalanceChip(icon: @Composable () -> Unit, text: String) {
    Row(
        modifier = Modifier
            .clip(ChipShape)
            .background(Color.Transparent)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(text = text, color = HomeBalanceBarTint, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeCardsCarousel(pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(HomeCardCarouselSlotHeight),
        beyondBoundsPageCount = 1,
        pageSpacing = 12.dp
    ) {
        HomeCardPlaceholder()
    }
}

@Composable
private fun HomeCardPlaceholder() {
    val kreditFront = rememberKreditFrontFontFamily()
    val schemeTypefaceFamily = MaterialTheme.typography.titleMedium.fontFamily
    val title = stringResource(R.string.home_card_placeholder)
    val number = stringResource(R.string.home_card_masked_number)
    val visaLogo = rememberAssetImageBitmap(CardVisaLogoAsset)
    val monobankLogo = rememberAssetImageBitmap(CardMonobankNegateAsset)
    val visaCd = stringResource(R.string.home_card_scheme)

    var cardUnfolded by remember { mutableStateOf(false) }
    val unfoldProgress by animateFloatAsState(
        targetValue = if (cardUnfolded) 1f else 0f,
        animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing),
        label = "homeCardUnfold"
    )

    /* Наклон і камера як у [PremiumBankCardPreviewStyle] (58° / 34) у складеному стані. */
    val rotX = lerp(65f, 0f, unfoldProgress)
    val rotY = 0f
    val rotZ = 0f
    val cardTransY = lerp(20f, 0f, unfoldProgress)
    val cameraFactor = lerp(34f, 22f, unfoldProgress)

    // Номер картки: жирний Kredit Front; при розгортанні колір → #BCBCBC.
    val numberColor = lerp(Color(0xFF57667D), Color(0xFFBCBCBC), unfoldProgress)
    val numberStyle = TextStyle(
        fontFamily = kreditFront,
        fontSize = 25.sp,
        letterSpacing = 1.7.sp,
        fontWeight = FontWeight.Bold,
        color = numberColor
    )
    val visaFallbackStyle = TextStyle(
        fontFamily = schemeTypefaceFamily ?: FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HomeCardCarouselSlotHeight)
            .semantics { contentDescription = title },
        contentAlignment = Alignment.TopCenter
    ) {
        HomeMonoTiltedCard(
            monobankLogo = monobankLogo,
            visaLogo = visaLogo,
            number = number,
            numberStyle = numberStyle,
            visaFallbackText = visaCd,
            visaFallbackStyle = visaFallbackStyle,
            monobankContentDescription = title,
            visaContentDescription = visaCd,
            rotationXDegrees = rotX,
            rotationYDegrees = rotY,
            rotationZDegrees = rotZ,
            cameraDistanceFactor = cameraFactor,
            translationY = cardTransY,
            cardUnfoldProgress = unfoldProgress,
            onCardClick = { cardUnfolded = !cardUnfolded },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HomeAllCardsChip() {
    val allCardsIcon = rememberAssetImageBitmap(HomeAllCardsChipIconAsset)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(ChipShape)
                .background(KeypadButton.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
                .padding(horizontal = 26.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (allCardsIcon != null) {
                Image(
                    bitmap = allCardsIcon,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(PinPromptText)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = null,
                    tint = PinPromptText,
                    modifier = Modifier.size(15.dp)
                )
            }
            Text(
                text = stringResource(R.string.home_all_cards),
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun HomeQuickActions() {
    val transferQuickIcon = rememberAssetImageBitmap(QuickActionBankCardNegateAsset)
    val ibanQuickIcon = rememberAssetImageBitmap("$OperationsLogosPath/iban.png")
    val otherQuickIcon = rememberAssetImageBitmap("$OperationsLogosPath/layer_negate.png")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickAction(stringResource(R.string.home_action_transfer)) {
            if (transferQuickIcon != null) {
                Image(
                    bitmap = transferQuickIcon,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(QuickActionIconTint)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = null,
                    tint = QuickActionIconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        QuickAction(stringResource(R.string.home_action_iban)) {
            if (ibanQuickIcon != null) {
                Image(
                    bitmap = ibanQuickIcon,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(QuickActionIconTint)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = null,
                    tint = QuickActionIconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        QuickAction(stringResource(R.string.home_action_other)) {
            if (otherQuickIcon != null) {
                Image(
                    bitmap = otherQuickIcon,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(QuickActionIconTint)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Layers,
                    contentDescription = null,
                    tint = QuickActionIconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickAction(label: String, icon: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(ActionCircleSize)
                .clip(CircleShape)
                .background(QuickActionCircleFill)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = HomeQuickActionCaptionColor,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HomeOperationsCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = OperationsBlockColor
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp,
                bottom = 16.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_operations_title),
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .clip(OperationsAllChipShape)
                        .background(HomeOperationsAllChipBackground)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { }
                        .padding(horizontal = 22.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.home_operations_all) + " ›",
                        color = HomeOperationsAllChipText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            OperationRow(
                title = stringResource(R.string.home_op_steam),
                amount = stringResource(R.string.home_op_steam_amount),
                logoAssetName = "steam.png"
            )
            Spacer(modifier = Modifier.height(32.dp))
            OperationRow(
                title = stringResource(R.string.home_op_card),
                amount = stringResource(R.string.home_op_card_amount),
                logoAssetName = null,
                fallbackIcon = Icons.Filled.CreditCard
            )
            Spacer(modifier = Modifier.height(32.dp))
            OperationRow(
                title = stringResource(R.string.home_op_mcd),
                amount = stringResource(R.string.home_op_mcd_amount),
                logoAssetName = null,
                logoCircleBackground = Color(0xFFC8102E),
                fallbackIcon = Icons.Filled.Restaurant,
                fallbackIconTint = Color.White
            )
        }
    }
}

@Composable
private fun OperationRow(
    title: String,
    amount: String,
    logoAssetName: String?,
    logoCircleBackground: Color = AvatarPlaceholder,
    fallbackIcon: ImageVector? = null,
    fallbackIconTint: Color = HomeBalanceBarTint
) {
    val context = LocalContext.current
    val logoBitmap = remember(logoAssetName) {
        if (logoAssetName.isNullOrBlank()) return@remember null
        runCatching {
            context.assets.open("$OperationsLogosPath/$logoAssetName").use { input ->
                BitmapFactory.decodeStream(input)?.asImageBitmap()
            }
        }.getOrNull()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(logoCircleBackground),
            contentAlignment = Alignment.Center
        ) {
            if (logoBitmap != null) {
                Image(
                    bitmap = logoBitmap,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (fallbackIcon != null) {
                Icon(
                    imageVector = fallbackIcon,
                    contentDescription = null,
                    tint = fallbackIconTint,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = title.take(1).uppercase(),
                    color = PinPromptText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = amount,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun HomeLimitsAbroadCard() {
    val borderBrush = Brush.horizontalGradient(listOf(LimitsGradientStart, LimitsGradientEnd))
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, borderBrush, CardShape),
        shape = CardShape,
        color = KeypadButton
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Outlined.Business, null, tint = LimitsGradientStart, modifier = Modifier.size(28.dp))
                Text(
                    text = stringResource(R.string.home_limits_left),
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 17.sp
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(PinPromptText.copy(alpha = 0.25f))
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Outlined.Language, null, tint = LimitsGradientEnd, modifier = Modifier.size(28.dp))
                Text(
                    text = stringResource(R.string.home_limits_right),
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun HomeUsefulCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = HomeUsefulCardColor
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.home_useful_title),
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RateRow(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
                ) {
                    UsefulTile(
                        Icons.Filled.SupportAgent,
                        stringResource(R.string.home_useful_support),
                        Modifier.weight(1f),
                        iconTint = Color(0xFF3B82F6)
                    )
                    UsefulTile(
                        Icons.Filled.QuestionMark,
                        stringResource(R.string.home_useful_faq),
                        Modifier.weight(1f),
                        iconTint = Color(0xFFA855F7)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
                ) {
                    UsefulTile(
                        Icons.Filled.ReceiptLong,
                        stringResource(R.string.home_useful_statements),
                        Modifier.weight(1f),
                        iconTint = Color(0xFFEC4899)
                    )
                    UsefulTile(
                        Icons.Outlined.Article,
                        stringResource(R.string.home_useful_tariffs),
                        Modifier.weight(1f),
                        iconTint = Color(0xFF22C55E)
                    )
                }
            }
        }
    }
}

@Composable
private fun RateRow(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = HomeUsefulTileFill
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RateCurrencyItem(
                flagEmoji = "\uD83C\uDDFA\uD83C\uDDF8",
                title = stringResource(R.string.home_rate_usd_title),
                values = stringResource(R.string.home_rate_usd_values),
                modifier = Modifier.weight(1f)
            )
            RateCurrencyItem(
                flagEmoji = "\uD83C\uDDEA\uD83C\uDDFA",
                title = stringResource(R.string.home_rate_eur_title),
                values = stringResource(R.string.home_rate_eur_values),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RateCurrencyItem(
    flagEmoji: String,
    title: String,
    values: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(KeypadButton),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = flagEmoji,
                fontSize = 17.sp,
                lineHeight = 19.sp
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = PinPromptText,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = values,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun UsefulTile(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    iconTint: Color = AccentRed
) {
    Surface(
        modifier = modifier
            .height(86.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { },
        shape = RoundedCornerShape(14.dp),
        color = HomeUsefulTileFill
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HomeBottomBar(
    selectedTab: HomeBottomNavTab,
    onTabSelected: (HomeBottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BottomBarGap)
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(BottomBarHeight)
                .border(1.dp, HomeBottomBarBorder, RoundedCornerShape(BottomBarPillRadius)),
            shape = RoundedCornerShape(BottomBarPillRadius),
            color = HomeBottomBarFill,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 0.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavPillCardsLottieNavItem(
                    label = stringResource(R.string.home_nav_cards),
                    selected = selectedTab == HomeBottomNavTab.Cards,
                    onSelect = { onTabSelected(HomeBottomNavTab.Cards) },
                    modifier = Modifier.weight(1f)
                )
                NavPillLottieNavItem(
                    asset = CreditsNavLottieAsset,
                    label = stringResource(R.string.home_nav_credits),
                    selected = selectedTab == HomeBottomNavTab.Credits,
                    tapMaxFrameInclusive = NavLottieTapMaxFrameInclusive,
                    onSelect = { onTabSelected(HomeBottomNavTab.Credits) },
                    modifier = Modifier.weight(1f)
                )
                NavPillSavingsLottieNavItem(
                    label = stringResource(R.string.home_nav_savings),
                    selected = selectedTab == HomeBottomNavTab.Savings,
                    onSelect = { onTabSelected(HomeBottomNavTab.Savings) },
                    modifier = Modifier.weight(1f)
                )
                NavPillLottieNavItem(
                    asset = MoreNavLottieAsset,
                    label = stringResource(R.string.home_nav_more),
                    selected = selectedTab == HomeBottomNavTab.More,
                    tapMaxFrameInclusive = MoreNavTapMaxFrameInclusive,
                    onSelect = { onTabSelected(HomeBottomNavTab.More) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Surface(
            modifier = Modifier
                .size(BottomBarHeight)
                .border(1.dp, HomeBottomBarBorder, CircleShape),
            shape = CircleShape,
            color = HomeBottomBarFill,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            onClick = { onTabSelected(HomeBottomNavTab.Market) }
        ) {
            val marketTint =
                if (selectedTab == HomeBottomNavTab.Market) AccentRed else HomeNavIconInactive
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 1.dp, bottom = BottomBarNavLabelBottomPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                NavMarketLottieSingleFrame(
                    label = stringResource(R.string.home_nav_market),
                    selected = selectedTab == HomeBottomNavTab.Market,
                    modifier = Modifier.size(BottomBarNavLottieIconSize)
                )
                Spacer(modifier = Modifier.height(1.dp))
                BottomBarNavLabel(text = stringResource(R.string.home_nav_market), color = marketTint)
            }
        }
    }
}

/** «Маркет»: один статичний кадр; при зміні вкладки — скидан на початок + сірий тінт, як у «Картки». */
@Composable
private fun NavMarketLottieSingleFrame(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(MarketNavLottieAsset))
    val anim = rememberLottieAnimatable()
    LaunchedEffect(composition, selected) {
        val c = composition ?: return@LaunchedEffect
        anim.snapTo(c, progressForFrame(c, c.startFrame))
    }
    LottieAnimation(
        composition = composition,
        progress = { anim.progress },
        modifier = modifier
            .lottieNavInactiveGrayTint(selected)
            .semantics { contentDescription = label },
        enableMergePaths = true
    )
}

@Composable
private fun NavPillCardsLottieNavItem(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(CardsNavLottieAsset))
    val anim = rememberLottieAnimatable()
    var leftCardsTabOnce by remember { mutableStateOf(false) }

    LaunchedEffect(selected) {
        if (!selected) leftCardsTabOnce = true
    }

    LaunchedEffect(composition, selected, leftCardsTabOnce) {
        val c = composition ?: return@LaunchedEffect
        if (!selected) {
            anim.snapTo(c, progressForFrame(c, c.startFrame))
            return@LaunchedEffect
        }
        if (!leftCardsTabOnce) {
            anim.snapTo(c, progressForFrame(c, NavLottieTapMaxFrameInclusive.toFloat()))
        } else {
            anim.snapTo(c, progressForFrame(c, c.startFrame))
            anim.animate(
                composition = c,
                clipSpec = LottieClipSpec.Frame(0, NavLottieTapMaxFrameInclusive),
                iterations = 1
            )
            anim.snapTo(c, progressForFrame(c, NavLottieTapMaxFrameInclusive.toFloat()))
        }
    }

    val tint = if (selected) AccentRed else HomeNavIconInactive
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            )
            .padding(horizontal = 0.dp)
            .padding(top = 1.dp, bottom = BottomBarNavLabelBottomPadding),
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { anim.progress },
            modifier = Modifier
                .size(BottomBarNavLottieIconSize)
                .lottieNavInactiveGrayTint(selected),
            enableMergePaths = true
        )
        Spacer(modifier = Modifier.height(1.dp))
        BottomBarNavLabel(text = label, color = tint)
    }
}

@Composable
private fun NavPillSavingsLottieNavItem(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(SavingsNavLottieAsset))
    val anim = rememberLottieAnimatable()
    var playToken by remember { mutableIntStateOf(0) }

    LaunchedEffect(selected) {
        if (!selected) playToken = 0
    }

    LaunchedEffect(composition, selected, playToken) {
        val c = composition ?: return@LaunchedEffect
        if (!selected) {
            anim.snapTo(c, progressForFrame(c, c.startFrame))
            return@LaunchedEffect
        }
        if (playToken == 0) {
            anim.snapTo(c, progressForFrame(c, c.startFrame))
        }
    }

    LaunchedEffect(playToken) {
        if (playToken == 0) return@LaunchedEffect
        val c = composition ?: return@LaunchedEffect
        anim.snapTo(c, progressForFrame(c, c.startFrame))
        anim.animate(
            composition = c,
            clipSpec = LottieClipSpec.Frame(0, SavingsNavTapMaxFrameInclusive),
            iterations = 1
        )
        anim.snapTo(c, progressForFrame(c, c.startFrame))
    }

    val tint = if (selected) AccentRed else HomeNavIconInactive
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSelect()
                playToken++
            }
            .padding(horizontal = 0.dp)
            .padding(top = 1.dp, bottom = BottomBarNavLabelBottomPadding),
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { anim.progress },
            modifier = Modifier
                .size(BottomBarNavLottieIconSize)
                .lottieNavInactiveGrayTint(selected),
            enableMergePaths = true
        )
        Spacer(modifier = Modifier.height(1.dp))
        BottomBarNavLabel(text = label, color = tint)
    }
}

@Composable
private fun NavPillLottieNavItem(
    asset: String,
    label: String,
    selected: Boolean,
    tapMaxFrameInclusive: Int,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(asset))
    val anim = rememberLottieAnimatable()
    var playToken by remember { mutableIntStateOf(0) }

    LaunchedEffect(selected) {
        if (!selected) {
            playToken = 0
        }
    }

    LaunchedEffect(composition, selected) {
        val c = composition ?: return@LaunchedEffect
        if (!selected) {
            anim.snapTo(c, progressForFrame(c, c.startFrame))
        }
    }

    LaunchedEffect(playToken) {
        if (playToken == 0) return@LaunchedEffect
        val c = composition ?: return@LaunchedEffect
        anim.snapTo(c, progressForFrame(c, c.startFrame))
        anim.animate(
            composition = c,
            clipSpec = LottieClipSpec.Frame(0, tapMaxFrameInclusive),
            iterations = 1
        )
    }

    val tint = if (selected) AccentRed else HomeNavIconInactive
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSelect()
                playToken++
            }
            .padding(horizontal = 0.dp)
            .padding(top = 1.dp, bottom = BottomBarNavLabelBottomPadding),
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { anim.progress },
            modifier = Modifier
                .size(BottomBarNavLottieIconSize)
                .lottieNavInactiveGrayTint(selected),
            enableMergePaths = true
        )
        Spacer(modifier = Modifier.height(1.dp))
        BottomBarNavLabel(text = label, color = tint)
    }
}

@Composable
private fun BottomBarNavLabel(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = color,
        fontFamily = BottomBarNavLabelFontFamily,
        fontSize = BottomBarNavLabelFontSize,
        fontWeight = FontWeight.Bold,
        lineHeight = BottomBarNavLabelLineHeight,
        letterSpacing = BottomBarNavLabelLetterSpacing,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

/** PNG `gift-box_negate` біля суми кешбеку; якщо файлу немає — [Icons.Outlined.CardGiftcard]. */
@Composable
private fun HomeCashbackGiftIcon(modifier: Modifier = Modifier) {
    val bitmap = rememberFirstSuccessfulAssetBitmap(HomeGiftBoxNegateAssetPaths)
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(HomeBalanceMainAmountColor)
        )
    } else {
        Icon(
            imageVector = Icons.Outlined.CardGiftcard,
            contentDescription = null,
            tint = HomeBalanceMainAmountColor,
            modifier = modifier
        )
    }
}

private fun progressForFrame(composition: LottieComposition, frame: Float): Float =
    composition.getProgressForFrame(frame)
