package com.konvert.app.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import kotlin.math.abs
import kotlin.math.floor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.lerp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.draw.drawBehind
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import com.konvert.app.R
import com.konvert.app.admin.LocalAppAdmin
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
import com.konvert.app.ui.theme.HomeNavIconActive
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
import kotlin.math.pow

private val CardShape = RoundedCornerShape(24.dp)
private val ChipShape = RoundedCornerShape(20.dp)
/** Капсула «Усі ›» — великий радіус (візуально як на референсі). */
private val OperationsAllChipShape = RoundedCornerShape(999.dp)
/** Низька капсула як на референсі monobank — мало вертикального повітря. */
private val OperationsAllChipPaddingH = 16.dp
private val OperationsAllChipPaddingV = 4.dp
private val OperationsAllChipFontSize = 14.sp
private val OperationsAllChipLineHeight = 14.sp
private val ActionCircleSize = 56.dp

/** Висота нижньої «пігулки» та Маркету — радіус = половина висоти (капсула / коло). */
private val BottomBarHeight = 72.dp
private val BottomBarPillRadius = 36.dp
private val BottomBarGap = 4.dp

/** Lottie в меню; статична іконка «Маркет» трохи менша. */
private val BottomBarNavLottieIconSize = 40.dp
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
private val HomeBottomBarShadeHeight = 210.dp

/**
 * Вертикальні відступи [HomeBottomBar] навколо пігулки (див. [HomeBottomBar] `padding(vertical = 8.dp)`).
 * Потрібні для нижнього [contentPadding] [LazyColumn], щоб «Корисне» не ховалось під панеллю.
 */
private val HomeBottomBarRowVerticalPadding = 16.dp

/** Додатковий зазор між верхом плаваючої панелі й останнім блоком після повного скролу. */
private val HomeCardsListBottomGapBeyondBar = 8.dp
private val HomeCardsListBottomTailTrim = 48.dp

/** Відступ під топ-баром перед блоком балансу (не між балансом і картою). */
private val HomeTopBarToBalancePaddingDp = 44.dp
private const val HomeTopBarToBalancePaddingPx: Float = 92f

/** Горизонтальний відступ [LazyColumn]. */
private val HomeCardsLazyHorizontalPadding = 14.dp

/**
 * Горизонтальний inset [HorizontalPager] — тонкі смуги сусідніх карт біля центральної,
 * близько до основної пластини (не «широкий» carousel).
 */
private val HomeCardsPagerHorizontalPeek = 8.dp

/** Мінімальний зазор між сторінками в пейджері (основний рух — нативний scroll пейджера). */
private val HomeCardsPagerPageSpacing = 0.dp
/** Додаткове зближення тільки пластикових карт між собою (без зближення широких блоків операцій). */
private val HomeCardsPlateNeighborExtraPull = 54.dp

/** Між нижнім краєм балансу (чипи) і верхом каруселі. */
private val HomeSectionGapBalanceToCard = 48.dp

/** Зовнішній блок навколо пунктів «Особисті дані…» / «Налаштування…». */
private val HomeProfileMenuOuterBlockColor = Color(0xFF303030)
/** Фон рядків усередині цього блоку. */
private val HomeProfileMenuSettingsRowsColor = Color(0xFF323232)
private val HomeProfileMenuCardColor = Color(0xFF323232)
private val HomeProfileMenuIconCircleBg = Color(0xFF2C3E50)
private val HomeProfileMenuDivider = Color.White.copy(alpha = 0.10f)
private val HomeProfileMenuSubtitle = Color(0xFF8E8E93)
private val HomeProfileMenuText = Color(0xFFE6E6E6)
private val HomeProfileMenuRowHighlight = Color(0xFF373737)
private val HomeProfileCheckBg = Color(0xFF8CF4DC)
private val HomeProfileCheckTint = Color(0xFF0B6F63)
private val HomeProfileDetailsButtonBg = Color(0xFF212121)
private val HomeProfileDetailsGradientStart = Color(0xFF776ECA)
private val HomeProfileDetailsGradientEnd = Color(0xFFBD5AAC)
private val HomeProfileMenuSheetShape = RoundedCornerShape(18.dp)

/**
 * Висота, яку займає блок каруселі в [LazyColumn] — мала, щоб «Усі картки» та нижній контент піднялись.
 * Має бути достатньою під вищу пластину [HomeBankCardFrame] + нахил.
 */
private val HomeCardCarouselLayoutReserveHeight = 230.dp

/** Висота слота під [HomeCardPlaceholder] — під вищу/ширшу пластину в [HomeMonoTiltedCard]. */
private val HomeCardCarouselPagerVisualHeight = 240.dp
private val HomeCardCarouselMistTopOffset = 138.dp
private val HomeCardCarouselMistHeight = 150.dp

/**
 * Вертикаль пластики на головному екрані (наклон «від користувача»):
 * - [HomeCardPlateOffsetY] + [HomeCardPlateExtraLiftPx] — базовий зсув у [HomeCardPlaceholder] (`plateOffsetY`);
 * - [HomeCardCarouselPlateNudgeY] — тонке доналаштування вгору/вниз без перерахунку px;
 * - у [HomeCardPlaceholder]: `rotX` (кут нахилу), `cardTransY` (translationY у [HomeMonoTiltedCard], більше — нижче на екрані).
 */
private val HomeCardPlateOffsetY = (-40).dp

/** Додатково підняти пластину (px → dp разом із [HomeCardPlateOffsetY]). */
private const val HomeCardPlateExtraLiftPx: Float = 56f

/** Додатковий зсув пластини вгору на каруселі (негативний = вище). */
private val HomeCardCarouselPlateNudgeY = (-45).dp

/** Підняти весь блок балансу вгору (px → dp у [HomeBalanceBlock]). */
private const val HomeBalanceVerticalLiftPx: Float = 50f

/**
 * Зсув чипа «Усі картки» по вертикалі відносно місця після блоку пластини.
 * **Негативний** — малює чип вище (ближче до картки). Реалізовано через [Modifier.offset], бо
 * `Spacer(height = негатив)` у [Column] зазвичай **обрізається до 0** під час measure — великі
 * від’ємні значення там «не працюють».
 *
 * Щоб підняти контент у **розкладці** (а не лише намалювати вище), зменшуйте [HomeCardCarouselLayoutReserveHeight]
 * або висоту слота [HomeCardCarouselPagerVisualHeight].
 */
private val HomeSectionGapCarouselToAllCards = (-122).dp

/**
 * Додатковий зсув блоку «Швидкі дії» + «Операції» вгору (до чипа «Усі картки»), той самий прийом, що й [Modifier.offset] для чипа.
 * Негативний — вище.
 */
private val HomeSectionOffsetQuickActionsAndOperationsY = (-105).dp

/** Між «Усі картки» і швидкими діями — ~40 px. */
private val HomeSectionGapAllCardsToQuick = 6.dp
/** Між швидкими діями і блоком «Операції». */
private val HomeSectionGapQuickToOperations = 8.dp
/** Між блоком «Операції» (перший item) і «Ліміти та обмеження». */
private val HomeSectionGapOperationsToLimits = 14.dp
/** Між «Ліміти та обмеження» і «Корисне». */
private val HomeSectionGapLimitsToUseful = 88.dp

/**
 * Підняти блоки **під** «Операції» (ліміти + корисне) до операцій і на **ту саму** величину скоротити
 * вертикальний хвіст сторінки: спочатку зменшуються [HomeSectionGapOperationsToLimits] та
 * [HomeSectionGapLimitsToUseful], решта — з нижнього [PaddingValues] [LazyColumn].
 */
private val HomeSectionCompactBelowOperationsDp = 102.dp

/** У «Корисне»: однаковий горизонтальний inset для ряду курсів і сітки плиток. */
private val HomeUsefulInnerHorizontalPadding = 0.dp
/** Зазор між двома рядами плиток 2×2. */
private val HomeUsefulTilesRowSpacing = 16.dp
/** Зазор між двома плитками в одному ряду. */
private val HomeUsefulTilesHorizontalSpacing = 18.dp
private val HomeQuickActionIconSize = 22.dp
private const val HomeTopAssetIconScaleY = 1.12f
private const val HomeTopProfileMessageScale = 1.22f
private const val HomeTopStatsScale = 1.36f
private val HomeBalanceWalletIconSize = 22.dp
private val HomeBalanceCreditIconSize = 22.dp
private val HomeOperationsTitleFontSize = 20.sp
private val HomeOperationRowFontSize = 16.sp
private val HomeOperationRowTextColor = Color(0xFFE4E2E3)

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
                weight = FontWeight.Thin,
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
private const val HomeProfileMessageAsset = "$OperationsLogosPath/profile_message.png"
private const val HomeProfilePersonalDataAsset = "$OperationsLogosPath/personal_data.png"
private const val HomeProfileSettingsAsset = "$OperationsLogosPath/settings.png"
private const val HomeProfileFopAsset = "$OperationsLogosPath/fop.png"
/** Іконка чипа кредитного ліміту біля [R.string.home_balance_credit]. */
private const val CardCreditSumAsset = "$OperationsLogosPath/credit_sum.png"
/** Іконка чипа «Усі картки» під каруселлю. */
private const val HomeAllCardsChipAsset = "$OperationsLogosPath/all_cards.png"

private const val HomeStatsAsset = "$OperationsLogosPath/stats.png"
private const val HomeUsefulTermsAsset = "$OperationsLogosPath/terms_and_tarifs.png"
private const val HomeUsefulStatementsAsset = "$OperationsLogosPath/dovidky.png"
private const val HomeUsefulQrAsset = "$OperationsLogosPath/qr-scan.png"
private const val HomeUsefulQuestionsAsset = "$OperationsLogosPath/questions.png"
private const val HomeUsefulSupportAsset = "$OperationsLogosPath/support.png"
private const val HomeOperationTransferAssetName = "transfer.png"
private const val HomeLimitsAsset = "$OperationsLogosPath/limits_.png"
private const val HomeForeignAsset = "$OperationsLogosPath/foreign.png"
private const val HomeUsdAsset = "$OperationsLogosPath/usd.png"
private const val HomeEurAsset = "$OperationsLogosPath/eur.png"
private const val HomeGraphNegateAsset = "graph_negate.png"
private const val HomeGraphNegateInOperationsLogos = "$OperationsLogosPath/graph_negate.png"
private const val HomeCatIconAsset = "$OperationsLogosPath/cat_icon.png"

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
private fun rememberCroppedAssetImageBitmap(assetPath: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(assetPath) {
        if (assetPath.isBlank()) null
        else runCatching {
            context.assets.open(assetPath).use { input ->
                BitmapFactory.decodeStream(input)
                    ?.cropVisibleAlpha()
                    ?.asImageBitmap()
            }
        }.getOrNull()
    }
}

private fun android.graphics.Bitmap.cropVisibleAlpha(): android.graphics.Bitmap {
    var minX = width
    var minY = height
    var maxX = -1
    var maxY = -1
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (getPixel(x, y).ushr(24) > 8) {
                if (x < minX) minX = x
                if (y < minY) minY = y
                if (x > maxX) maxX = x
                if (y > maxY) maxY = y
            }
        }
    }
    if (maxX < minX || maxY < minY) return this
    return Bitmap.createBitmap(this, minX, minY, maxX - minX + 1, maxY - minY + 1)
}

@Composable
private fun rememberProfileAvatarBitmap(path: String?): ImageBitmap? {
    return remember(path) {
        path
            ?.takeIf { it.isNotBlank() }
            ?.let { BitmapFactory.decodeFile(it)?.asImageBitmap() }
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

/** Lottie «Накопичення»: при тапі — кадри 0…14 (15 кадрів), після відтворення залишаємось на останньому. */
private const val SavingsNavTapMaxFrameInclusive = 16

private const val CardsNavLottieAsset = "animations/cards_icon.json"
private const val CreditsNavLottieAsset = "animations/credits_icon.json"
private const val MoreNavLottieAsset = "animations/more_icon.json"
private const val SavingsNavLottieAsset = "animations/deposits_dnm_16_quick.json"
private const val MarketNavLottieAsset = "animations/market20_icon.json"

/** Активна вкладка: м'який tint Lottie до #fd8688; неактивна — приглушення. */
private fun Modifier.lottieNavInactiveGrayTint(selected: Boolean): Modifier =
    graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(
                color = if (selected) HomeNavIconActive else HomeNavIconInactive,
                blendMode = BlendMode.SrcAtop
            )
        }

/**
 * Вертикальні палітри фону — по одній на кожну сторінку каруселі карт (узгоджено з [HomeCardsCarouselPageCount]).
 * Порядок: чорна → біла (пурпур) → чорно-зелена (темна бірюза) → чорно-червона (малина).
 */
private val HomeBgMainPalettes: List<Array<Pair<Float, Color>>> = listOf(
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
    arrayOf(
        0.00f to Color(0xFF261132),
        0.14f to Color(0xFF2A1B4F),
        0.30f to Color(0xFF2D2765),
        0.48f to Color(0xFF321C52),
        0.66f to Color(0xFF1A102E),
        0.82f to Color(0xFF141020),
        0.92f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    arrayOf(
        0.00f to Color(0xFF022028),
        0.16f to Color(0xFF04303C),
        0.32f to Color(0xFF053446),
        0.48f to Color(0xFF063E48),
        0.68f to Color(0xFF021C24),
        0.82f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    arrayOf(
        0.00f to Color(0xFF1D042C),
        0.16f to Color(0xFF3B1148),
        0.32f to Color(0xFF51195A),
        0.48f to Color(0xFF401424),
        0.68f to Color(0xFF180810),
        0.82f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    )
)

private val HomeBgRadialTops: List<Color> = listOf(
    Color(0x332F6DFF),
    Color(0x44A78BFA),
    Color(0x3320D4CE),
    Color(0x44FF5C9A)
)

private val HomeBgRadialMids: List<Color> = listOf(
    Color(0x1A4D8DFF),
    Color(0x28654DDC),
    Color(0x24168A8A),
    Color(0x28CC3D72)
)

/** Затемнення для режиму «лише вікно» (немає виміряної висоти контенту). */
private val HomeBgOverlayStops: Array<Pair<Float, Color>> = arrayOf(
    0.00f to Color.Transparent,
    0.83f to Color.Transparent,
    0.90f to Color(0x44000000),
    0.96f to Color(0xCC121212),
    1.00f to Color(0xFF121212)
)

/**
 * Вертикальний градієнт у координатах усього скрол-контенту (як у HEAD: ті самі стопи 0/35/45/55/100%),
 * окрема палітра на кожну картку — інтерполяція по [cardScrollPosition] як у [HomeBgMainPalettes].
 */
private val HomeBgContentScrollPalettes: List<Array<Pair<Float, Color>>> = listOf(
    arrayOf(
        0.00f to Color(0xFF0C0F44),
        0.35f to Color(0xFF122859),
        0.62f to Color(0xFF102A5F),
        0.74f to Color(0xFF0B2142),
        0.84f to Color(0xFF101727),
        0.92f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    arrayOf(
        0.00f to Color(0xFF26072F),
        0.22f to Color(0xFF321247),
        0.42f to Color(0xFF342B68),
        0.58f to Color(0xFF27315F),
        0.72f to Color(0xFF172855),
        0.84f to Color(0xFF111A30),
        0.92f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    arrayOf(
        0.00f to Color(0xFF022028),
        0.35f to Color(0xFF053446),
        0.62f to Color(0xFF063E48),
        0.74f to Color(0xFF042B32),
        0.84f to Color(0xFF091B1F),
        0.92f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    ),
    arrayOf(
        0.00f to Color(0xFF1D042C),
        0.35f to Color(0xFF51195A),
        0.62f to Color(0xFF4A1752),
        0.74f to Color(0xFF351136),
        0.84f to Color(0xFF1E0C18),
        0.92f to Color(0xFF121212),
        1.00f to Color(0xFF121212)
    )
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
    cardBackgroundPaletteCount: Int = 1,
    /**
     * Висота всього скрол-контенту вкладки «Картки» (від верху до низу «Корисне»), px.
     * Якщо ≤ 1 — градієнт у режимі висоти вікна.
     */
    contentHeightPx: Float = 0f,
    /** Зсув скролу першого item [LazyColumn] (px), щоб зріз градієнта збігався з контентом. */
    contentScrollOffsetPx: Float = 0f
) {
    val paletteN = cardBackgroundPaletteCount.coerceIn(1, HomeBgMainPalettes.size)
    val maxSlot = (paletteN - 1).coerceAtLeast(0).toFloat()
    val seg = cardScrollPosition.coerceIn(0f, maxSlot)
    val i = floor(seg).toInt().coerceIn(0, paletteN - 1)
    val next = (i + 1).coerceAtMost(paletteN - 1)
    val tLocal = if (next == i) 0f else (seg - i).coerceIn(0f, 1f)
    val mainStops = lerpColorStops(HomeBgMainPalettes[i], HomeBgMainPalettes[next], tLocal)
    val contentScrollStops = lerpColorStops(
        HomeBgContentScrollPalettes[i],
        HomeBgContentScrollPalettes[next],
        tLocal
    )
    val radialTop = lerp(HomeBgRadialTops[i], HomeBgRadialTops[next], tLocal)
    val radialMid = lerp(HomeBgRadialMids[i], HomeBgRadialMids[next], tLocal)

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val vw = size.width
                val vh = size.height
                val contentMode = contentHeightPx > 1f
                val ch = if (contentMode) contentHeightPx.coerceAtLeast(vh) else vh
                val scroll = if (contentMode) contentScrollOffsetPx else 0f

                if (contentMode) {
                    val gradStart = Offset(0f, -scroll)
                    val gradEnd = Offset(0f, ch - scroll)
                    drawRect(
                        brush = Brush.linearGradient(
                            colorStops = contentScrollStops,
                            start = gradStart,
                            end = gradEnd
                        ),
                        topLeft = Offset.Zero,
                        size = Size(vw, vh)
                    )
                } else {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = mainStops,
                            startY = 0f,
                            endY = vh
                        ),
                        size = Size(vw, vh)
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(radialTop, Color.Transparent),
                            center = Offset(vw * 0.50f, vh * 0.22f),
                            radius = vh * 0.42f
                        ),
                        radius = vh * 0.42f,
                        center = Offset(vw * 0.50f, vh * 0.22f)
                    )

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(radialMid, Color.Transparent),
                            center = Offset(vw * 0.50f, vh * 0.40f),
                            radius = vh * 0.26f
                        ),
                        radius = vh * 0.26f,
                        center = Offset(vw * 0.50f, vh * 0.40f)
                    )

                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = HomeBgOverlayStops,
                            startY = 0f,
                            endY = vh
                        ),
                        size = Size(vw, vh)
                    )
                }
            }
    )
}

@Composable
fun HomeScreen(onOpenAdmin: () -> Unit = {}) {
    var selectedBottomTab by remember { mutableStateOf(HomeBottomNavTab.Cards) }
    var savingsJarFlow by remember { mutableStateOf<SavingsJarFlow>(SavingsJarFlow.None) }
    val bottomBarLiftDp = 0.dp
    /** Додатковий зсиг униз на 10 px екрана (щодо попереднього положення). */
    val bottomBarDownFromPx10 = (10f / LocalDensity.current.density).dp

    LaunchedEffect(selectedBottomTab) {
        if (selectedBottomTab != HomeBottomNavTab.Savings) {
            savingsJarFlow = SavingsJarFlow.None
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedBottomTab) {
            HomeBottomNavTab.Cards -> CardsTabScreen(onOpenAdmin = onOpenAdmin)
            HomeBottomNavTab.Credits -> CreditsTabScreen()
            HomeBottomNavTab.Savings -> SavingsTabScreen(
                onOpenJarBankCar = { index -> savingsJarFlow = SavingsJarFlow.Detail(index) }
            )
            HomeBottomNavTab.More -> MoreTabScreen()
            HomeBottomNavTab.Market -> MarketTabScreen()
        }

        val hideBottomBar =
            selectedBottomTab == HomeBottomNavTab.Savings && savingsJarFlow !is SavingsJarFlow.None
        if (!hideBottomBar) {
            HomeBottomBarShade(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
            HomeBottomBar(
                selectedTab = selectedBottomTab,
                onTabSelected = { selectedBottomTab = it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .offset(y = -bottomBarLiftDp + bottomBarDownFromPx10)
                    .padding(bottom = 0.dp)
            )
        }

        if (savingsJarFlow !is SavingsJarFlow.None) {
            BackHandler {
                val flow = savingsJarFlow
                savingsJarFlow = when (flow) {
                    is SavingsJarFlow.TransactionDetail ->
                        SavingsJarFlow.TopUpTransactions(flow.jarIndex, flow.category)
                    is SavingsJarFlow.TopUpTransactions ->
                        SavingsJarFlow.Detail(flow.jarIndex)
                    is SavingsJarFlow.Share ->
                        SavingsJarFlow.Detail(flow.jarIndex)
                    is SavingsJarFlow.Detail ->
                        SavingsJarFlow.None
                    SavingsJarFlow.None -> SavingsJarFlow.None
                }
            }
            when (val f = savingsJarFlow) {
                is SavingsJarFlow.Detail -> JarBankDetailScreen(
                    jarIndex = f.jarIndex,
                    onBack = { savingsJarFlow = SavingsJarFlow.None },
                    onShareJar = { savingsJarFlow = SavingsJarFlow.Share(f.jarIndex) },
                    onOpenStatsCategory = { cat ->
                        savingsJarFlow = SavingsJarFlow.TopUpTransactions(f.jarIndex, cat)
                    },
                    onOpenTopUpCategory = { cat ->
                        savingsJarFlow = SavingsJarFlow.TopUpTransactions(f.jarIndex, cat)
                    },
                    modifier = Modifier.fillMaxSize()
                )
                is SavingsJarFlow.Share -> JarBankShareScreen(
                    jarIndex = f.jarIndex,
                    onBack = { savingsJarFlow = SavingsJarFlow.Detail(f.jarIndex) },
                    modifier = Modifier.fillMaxSize()
                )
                is SavingsJarFlow.TopUpTransactions -> JarTopUpTransactionsScreen(
                    jarIndex = f.jarIndex,
                    category = f.category,
                    onBack = { savingsJarFlow = SavingsJarFlow.Detail(f.jarIndex) },
                    onOpenTransaction = { payload ->
                        savingsJarFlow = SavingsJarFlow.TransactionDetail(f.jarIndex, f.category, payload)
                    },
                    modifier = Modifier.fillMaxSize()
                )
                is SavingsJarFlow.TransactionDetail -> JarTransactionDetailScreen(
                    payload = f.payload,
                    onBack = {
                        savingsJarFlow = SavingsJarFlow.TopUpTransactions(f.jarIndex, f.category)
                    },
                    modifier = Modifier.fillMaxSize()
                )
                SavingsJarFlow.None -> {}
            }
        }
    }
}

/** Кількість карт у каруселі (= ach/r1…r5); фон інтерполюється між сусідніми палітрами під час свайпу. */
internal const val HomeCardsCarouselPageCount = 4

/**
 * Нормалізований зсув сторінки: 0 — сторінка в центрі, +1 — на екрані правий сусід, −1 — лівий.
 * Єдине джерело правди для [HorizontalPager] + [graphicsLayer] (баланс, картка, чип, дії, операції).
 */
@OptIn(ExperimentalFoundationApi::class)
private fun pagerPageOffsetForMotion(pagerState: PagerState, page: Int): Float =
    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
private fun firstToSecondIncomingHoldFactor(pagerState: PagerState, page: Int): Float {
    val anchorPage = pagerState.settledPage
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
    val dragFromAnchor = scrollPosition - anchorPage
    if (anchorPage != 0 || dragFromAnchor >= -1e-4f || page != 1) return 1f

    val smoothStep: (Float) -> Float = { t ->
        val x = t.coerceIn(0f, 1f)
        x * x * (3f - 2f * x)
    }
    val progress = abs(dragFromAnchor).coerceIn(0f, 1f)
    return if (progress <= 0.5f) {
        val tHalf = (progress / 0.5f).coerceIn(0f, 1f)
        // На першій половині свайпу вхідна (друга) сторінка майже не виїжджає:
        // на 50% шляху лишається ~20% її додаткового руху.
        0.02f + 0.08f * smoothStep(tHalf)
    } else {
        val tSecondHalf = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)
        // Після середини наздоганяє плавно, але помітно активніше.
        val catchUp = smoothStep(tSecondHalf.pow(0.75f))
        0.20f + 0.80f * catchUp
    }
}

/**
 * Легка пластика для сторінки картки в пейджері:
 * у центрі (offset=0) — без трансформації, на сусідніх позиціях (±1) — м'яке стискання й нахил.
 * Snap/fling керуються [PagerDefaults.flingBehavior] + spring; цей шар лише візуальний.
 */
@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.homeCardsUnifiedPageMotion(
    pagerState: PagerState,
    page: Int,
    densityPx: Float
): Modifier = this.graphicsLayer {
    val oRaw = pagerPageOffsetForMotion(pagerState, page)
    val oClamped = oRaw.coerceIn(-1f, 1f)
    val anchorPage = pagerState.settledPage
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
    val dragFromAnchor = scrollPosition - anchorPage
    val swipeDir = when {
        dragFromAnchor < -1e-4f -> -1f
        dragFromAnchor > 1e-4f -> 1f
        else -> 0f
    }
    val progress = abs(dragFromAnchor).coerceIn(0f, 1f)
    val outgoingPage = if (swipeDir != 0f) anchorPage else Int.MIN_VALUE
    val incomingPage = when {
        swipeDir < 0f -> anchorPage + 1
        swipeDir > 0f -> anchorPage - 1
        else -> Int.MIN_VALUE
    }

    val absO = abs(oClamped)
    val compression = absO * absO
    val s = lerp(1f, 0.965f, compression).coerceIn(0.95f, 1f)
    scaleX = s
    scaleY = s
    // Двофазний профіль: до 50% сторінки елементи розходяться, після 50% — плавно сходяться.
    val smoothStep: (Float) -> Float = { t ->
        val x = t.coerceIn(0f, 1f)
        x * x * (3f - 2f * x)
    }
    val stretchOutThenIn = if (progress <= 0.5f) {
        smoothStep(progress / 0.5f)
    } else {
        smoothStep((1f - progress) / 0.5f)
    }
    // Невеликий "хвіст" в кінці жесту: щоб зближення завершувалось без різкого стопу біля snap.
    val settleTail = (1f - smoothStep(progress)).pow(0.75f)
    // Довше утримує вхідну сторінку саме на старті руху.
    val earlyHold = 1f - smoothStep((progress / 0.42f).coerceIn(0f, 1f))
    val firstToSecondHold = firstToSecondIncomingHoldFactor(pagerState, page)
    val outgoingStretchPx =
        if (page == outgoingPage) (-swipeDir) * stretchOutThenIn * 110f * densityPx else 0f
    val incomingHoldPx =
        if (page == incomingPage) {
            (-swipeDir) *
                (0.25f * stretchOutThenIn + 0.34f * settleTail + 0.1f * earlyHold) *
                80f *
                firstToSecondHold *
                densityPx
        } else {
            0f
        }
    translationX = outgoingStretchPx + incomingHoldPx
    transformOrigin = TransformOrigin(0.5f, 0.44f)
    rotationY = (oClamped * -0.8f).coerceIn(-1.6f, 1.6f)
    cameraDistance = 26f * densityPx
    alpha = 1f
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeCardsTabDashboard(
    pagerState: PagerState,
    onOpenCardDetail: () -> Unit = {},
    lazyListState: LazyListState,
    onPagerSectionHeightPx: (Float) -> Unit,
    onHomeScrollContentHeightPx: (Float) -> Unit,
    onRequestProfileMenu: () -> Unit = {},
    pullToRefreshOffsetDp: androidx.compose.ui.unit.Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    val balanceSectionTopPadding =
        HomeTopBarToBalancePaddingDp + with(LocalDensity.current) { (HomeTopBarToBalancePaddingPx / density).dp }

    val density = LocalDensity.current
    val navBottomDp = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }
    val listBottomContentPadding =
        navBottomDp +
            BottomBarHeight +
            HomeBottomBarRowVerticalPadding +
            HomeCardsListBottomGapBeyondBar
    val compactBelow = HomeSectionCompactBelowOperationsDp
    var debtRemDp = compactBelow.value
    val spacerOpsToLimitsAfterCompact = HomeCardsLazyHorizontalPadding
    val spacerLimitsToUsefulAfterCompact = HomeCardsLazyHorizontalPadding + 2.dp
    debtRemDp -= HomeSectionGapOperationsToLimits.value
    debtRemDp -= HomeSectionGapLimitsToUseful.value
    val listBottomAfterCompact =
        (listBottomContentPadding.value - debtRemDp - HomeCardsListBottomTailTrim.value).coerceAtLeast(0f).dp
    val motionDensity = density.density

    val snapSpring = remember {
        spring<Float>(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    }
    // PagerDefaults.flingBehavior — @Composable; не викликати всередині remember { }.
    val pagerFling = PagerDefaults.flingBehavior(
        state = pagerState,
        snapAnimationSpec = snapSpring
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HomeCardsLazyHorizontalPadding)
        ) {
            HomeTopBar(onProfileClick = onRequestProfileMenu)
        }
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .onSizeChanged { onHomeScrollContentHeightPx(it.height.toFloat()) },
            contentPadding = PaddingValues(bottom = listBottomAfterCompact)
        ) {
            item(key = "home_pager_balance_ops") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { onPagerSectionHeightPx(it.height.toFloat()) }
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = HomeCardsPagerHorizontalPeek),
                            pageSpacing = HomeCardsPagerPageSpacing,
                            verticalAlignment = Alignment.Top,
                            beyondBoundsPageCount = 1,
                            flingBehavior = pagerFling
                        ) { page ->
                        val kind = HomeCarouselCardKind.entries[page]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .homeCardsUnifiedPageMotion(pagerState, page, motionDensity)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = HomeCardsLazyHorizontalPadding)
                            ) {
                                Spacer(modifier = Modifier.height(balanceSectionTopPadding))
                                HomeBalanceBlock(kind = kind)
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = HomeCardsLazyHorizontalPadding)
                            ) {
                                Spacer(modifier = Modifier.height(HomeSectionGapBalanceToCard))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(HomeCardCarouselLayoutReserveHeight),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    val pageOffset = pagerPageOffsetForMotion(pagerState, page).coerceIn(-1f, 1f)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(HomeCardCarouselPagerVisualHeight)
                                            .graphicsLayer {
                                                val firstToSecondHold =
                                                    firstToSecondIncomingHoldFactor(pagerState, page)
                                                translationX =
                                                    pageOffset *
                                                        HomeCardsPlateNeighborExtraPull.value *
                                                        motionDensity *
                                                        firstToSecondHold
                                            }
                                    ) {
                                        HomeCardPlaceholder(kind = kind, onCardClick = onOpenCardDetail)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = HomeSectionGapCarouselToAllCards)
                                ) {
                                    HomeAllCardsChip()
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = HomeSectionOffsetQuickActionsAndOperationsY)
                                ) {
                                    Spacer(modifier = Modifier.height(HomeSectionGapAllCardsToQuick))
                                    HomeQuickActions()
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(HomeSectionGapQuickToOperations)
                                    )
                                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                                        HomeOperationsCard(
                                            kind = kind,
                                            modifier = Modifier
                                                .requiredWidth(maxWidth + HomeCardsPagerHorizontalPeek * 2)
                                                .offset(y = pullToRefreshOffsetDp)
                                        )
                                    }
                                }
                            }
                        }
                        }
                        HomeCardCarouselMistOverlay(
                            cardScrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(HomeCardCarouselMistHeight)
                                .offset(y = HomeCardCarouselMistTopOffset)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = HomeCardsLazyHorizontalPadding)
                            .offset(y = -compactBelow)
                    ) {
                        Spacer(modifier = Modifier.height(spacerOpsToLimitsAfterCompact))
                        HomeLimitsAbroadCard()
                        Spacer(modifier = Modifier.height(spacerLimitsToUsefulAfterCompact))
                        HomeUsefulCard()
                    }
                }
            }
        }
    }
}

/**
 * Програмний перехід між сторінками карток з тією ж spring-спекою, що й [PagerDefaults.flingBehavior].
 */
@OptIn(ExperimentalFoundationApi::class)
internal suspend fun PagerState.animateHomeCardPageSpring(targetPage: Int) {
    this.animateScrollToPage(
        page = targetPage.coerceIn(0, HomeCardsCarouselPageCount - 1),
        pageOffsetFraction = 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
}

@Composable
private fun HomeCardCarouselMistOverlay(
    cardScrollPosition: Float,
    modifier: Modifier = Modifier
) {
    val paletteN = HomeCardsCarouselPageCount.coerceIn(1, HomeBgContentScrollPalettes.size)
    val maxSlot = (paletteN - 1).coerceAtLeast(0).toFloat()
    val seg = cardScrollPosition.coerceIn(0f, maxSlot)
    val i = floor(seg).toInt().coerceIn(0, paletteN - 1)
    val next = (i + 1).coerceAtMost(paletteN - 1)
    val tLocal = if (next == i) 0f else (seg - i).coerceIn(0f, 1f)
    val stops = lerpColorStops(HomeBgContentScrollPalettes[i], HomeBgContentScrollPalettes[next], tLocal)
    val mistTop = colorAlongStops(stops, 0.30f)
    val mistCenter = colorAlongStops(stops, 0.40f)
    val mistBottom = colorAlongStops(stops, 0.52f)
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.22f to mistTop.copy(alpha = 0.18f),
                    0.42f to mistCenter.copy(alpha = 0.54f),
                    0.58f to mistCenter.copy(alpha = 0.48f),
                    0.76f to mistBottom.copy(alpha = 0.16f),
                    1f to Color.Transparent
                )
            )
        )
    )
}

private val HomeProfileMenuScrimColor = Color.Black.copy(alpha = 0.54f)

@Composable
private fun Modifier.homeProfileRowFeedback(
    interactionSource: MutableInteractionSource
): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    return background(if (pressed || hovered) HomeProfileMenuRowHighlight else Color.Transparent)
}

@Composable
internal fun HomeProfileMenuBottomSheet(
    onDismiss: () -> Unit,
    onAppSettingsClick: () -> Unit = {}
) {
    val personal = stringResource(R.string.home_profile_sheet_personal)
    val settings = stringResource(R.string.home_profile_sheet_settings)
    val admin = LocalAppAdmin.current
    val activeName = admin?.state?.accountFullName?.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.home_profile_sheet_active_name)
    val activeSubtitle = stringResource(R.string.home_profile_sheet_active_subtitle)
    val fop = stringResource(R.string.home_profile_sheet_fop)
    val details = stringResource(R.string.home_profile_sheet_details)
    val activeCd = stringResource(R.string.home_profile_sheet_active_cd)
    val profileSheetBg = Color(0xFF252525)
    val profileAvatar = rememberProfileAvatarBitmap(admin?.state?.profileAvatarPath)
    val detailsBrush = Brush.horizontalGradient(
        listOf(HomeProfileDetailsGradientStart, HomeProfileDetailsGradientEnd)
    )
    var sheetVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        sheetVisible = true
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val dialogView = LocalView.current
        DisposableEffect(dialogView) {
            val window = (dialogView.parent as? DialogWindowProvider)?.window
            val previousStatusBarColor = window?.statusBarColor
            val previousNavigationBarColor = window?.navigationBarColor
            window?.statusBarColor = HomeProfileMenuScrimColor.toArgb()
            window?.navigationBarColor = profileSheetBg.toArgb()
            onDispose {
                if (previousStatusBarColor != null) {
                    window.statusBarColor = previousStatusBarColor
                }
                if (previousNavigationBarColor != null) {
                    window.navigationBarColor = previousNavigationBarColor
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HomeProfileMenuScrimColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        ) {
            AnimatedVisibility(
                visible = sheetVisible,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize(),
                enter = slideInVertically(
                    animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(durationMillis = 180))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.48f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {}
                            ),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        color = profileSheetBg,
                        contentColor = Color.White,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp, bottom = 0.dp)
                                .navigationBarsPadding(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                        shape = HomeProfileMenuSheetShape,
                        color = HomeProfileMenuSettingsRowsColor,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            HomeProfileMenuSettingsRow(
                                iconAsset = HomeProfilePersonalDataAsset,
                                label = personal,
                                onClick = { }
                            )
                            HomeProfileMenuThinDivider()
                            HomeProfileMenuSettingsRow(
                                iconAsset = HomeProfileSettingsAsset,
                                label = settings,
                                onClick = onAppSettingsClick
                            )
                        }
                    }
                    Surface(
                        shape = HomeProfileMenuSheetShape,
                        color = HomeProfileMenuCardColor,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            val activeRowInteraction = remember { MutableInteractionSource() }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .homeProfileRowFeedback(activeRowInteraction)
                                    .hoverable(activeRowInteraction)
                                    .clickable(
                                        interactionSource = activeRowInteraction,
                                        indication = null,
                                        onClick = { }
                                    )
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                                    .semantics { contentDescription = activeCd },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(AvatarPlaceholder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (profileAvatar != null) {
                                        Image(
                                            bitmap = profileAvatar,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Outlined.Person,
                                            contentDescription = null,
                                            modifier = Modifier.size(26.dp),
                                            tint = Color.White.copy(alpha = 0.95f)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = activeName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        color = HomeProfileMenuText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = activeSubtitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = HomeProfileMenuSubtitle,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(HomeProfileCheckBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = HomeProfileCheckTint
                                    )
                                }
                            }
                            HomeProfileMenuThinDivider()
                            val fopRowInteraction = remember { MutableInteractionSource() }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .homeProfileRowFeedback(fopRowInteraction)
                                    .hoverable(fopRowInteraction)
                                    .clickable(
                                        interactionSource = fopRowInteraction,
                                        indication = null,
                                        onClick = { }
                                    )
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0A0A0A)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val fopIcon = rememberAssetImageBitmap(HomeProfileFopAsset)
                                    if (fopIcon != null) {
                                        Image(
                                            bitmap = fopIcon,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Outlined.Business,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = fop,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = HomeProfileMenuText
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(HomeProfileDetailsButtonBg)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { }
                                        )
                                        .padding(horizontal = 16.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        text = details,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            brush = detailsBrush
                                        ),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
}
}

@Composable
private fun HomeProfileMenuSettingsRow(
    iconAsset: String,
    label: String,
    onClick: () -> Unit
) {
    val iconBitmap = rememberAssetImageBitmap(iconAsset)
    val rowInteraction = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .homeProfileRowFeedback(rowInteraction)
            .hoverable(rowInteraction)
            .clickable(
                interactionSource = rowInteraction,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(HomeProfileMenuIconCircleBg),
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
                    imageVector = Icons.Filled.Description,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = HomeProfileMenuText,
            maxLines = 2
        )
    }
}

@Composable
private fun HomeProfileMenuThinDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 72.dp, end = 0.dp)
            .height(1.dp)
            .background(HomeProfileMenuDivider)
    )
}

@Composable
private fun HomeTopBar(onProfileClick: () -> Unit) {
    val admin = LocalAppAdmin.current
    val profileAvatar = rememberProfileAvatarBitmap(admin?.state?.profileAvatarPath)
    val statsBitmap = rememberAssetImageBitmap(HomeStatsAsset)
    val profileMessageBitmap = rememberAssetImageBitmap(HomeProfileMessageAsset)
    val catIconBitmap = rememberAssetImageBitmap(HomeCatIconAsset)
    val chartsCd = stringResource(R.string.home_charts_cd)
    val bonusAmountText = stringResource(R.string.home_bonus_amount)
    val cashbackChipSemantics =
        stringResource(R.string.home_cashback_chip_semantics, bonusAmountText)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
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
                if (profileAvatar != null) {
                    Image(
                        bitmap = profileAvatar,
                        contentDescription = stringResource(R.string.home_profile_cd),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = stringResource(R.string.home_profile_cd),
                        tint = HomeBalanceMainAmountColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
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
                if (profileMessageBitmap != null) {
                    Image(
                        bitmap = profileMessageBitmap,
                        contentDescription = stringResource(R.string.home_chat_cd),
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = HomeTopProfileMessageScale,
                                scaleY = HomeTopProfileMessageScale * HomeTopAssetIconScaleY
                            ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = stringResource(R.string.home_chat_cd),
                        tint = HomeBalanceMainAmountColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                HomeCashbackGiftIcon(modifier = Modifier.size(19.dp))
                Text(
                    text = bonusAmountText,
                    color = HomeBalanceMainAmountColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(1.dp)
                    .background(HomeBalanceMainAmountColor.copy(alpha = 0.24f))
            )
            if (catIconBitmap != null) {
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
                    Image(
                        bitmap = catIconBitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 40.dp, height = 27.dp)
                            .graphicsLayer(scaleY = HomeTopAssetIconScaleY),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
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
                if (statsBitmap != null) {
                    Image(
                        bitmap = statsBitmap,
                        contentDescription = chartsCd,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = HomeTopStatsScale,
                                scaleY = HomeTopStatsScale * HomeTopAssetIconScaleY
                            ),
                        contentScale = ContentScale.Crop
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
                /* Масштаб кіл і плюса: −10% від попереднього (0.9 → 0.81). */
                val circleScale = 0.81f
                val rOuter = size.minDimension * 0.5f * circleScale
                /* Внутрішній диск: пропорція до rOuter мінус 3 px радіуса за макетом. */
                val rInner = (rOuter * (22f / 40f) - 3f).coerceAtLeast(1f)
                /* Зовнішнє кільце: ~90% прозорість (альфа 0.1); внутрішній диск — #ffffff. */
                val outerAlpha = 0.1f
                drawCircle(
                    color = Color.White.copy(alpha = outerAlpha),
                    radius = rOuter,
                    center = c
                )
                drawCircle(
                    color = Color(0xFFFFFFFF),
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
private fun HomeBalanceBlock(kind: HomeCarouselCardKind) {
    val admin = LocalAppAdmin.current
    val mainBal = when (kind) {
        HomeCarouselCardKind.Black -> admin?.state?.cardOrDefault(0)?.balanceDisplay
        HomeCarouselCardKind.WhiteBlackEdge -> admin?.state?.cardOrDefault(1)?.balanceDisplay
        HomeCarouselCardKind.BlackGreenEdgeUsd -> admin?.state?.cardOrDefault(2)?.balanceDisplay
        HomeCarouselCardKind.BlackRedEdgeEur -> admin?.state?.cardOrDefault(3)?.balanceDisplay
    }?.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.home_balance_main)
    val walletBal = admin?.state?.balanceWallet?.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.home_balance_wallet)
    val creditBal = admin?.state?.balanceCredit?.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.home_balance_credit)
    val showSecondaryBalances = kind == HomeCarouselCardKind.Black
    val secondaryBalancesReservedHeight = 46.dp
    val walletChipIcon = rememberAssetImageBitmap(CardWalletNegateAsset)
    val creditChipIcon = rememberCroppedAssetImageBitmap(CardCreditSumAsset)
    val density = LocalDensity.current.density
    val balanceLift = (-HomeBalanceVerticalLiftPx / density).dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = balanceLift),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HomeBalanceAddChip()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = mainBal,
                color = HomeBalanceMainAmountColor,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        if (showSecondaryBalances) {
            Spacer(modifier = Modifier.height(6.dp))
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
                                modifier = Modifier.size(HomeBalanceWalletIconSize),
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(HomeBalanceBarTint)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.AccountBalanceWallet,
                                contentDescription = null,
                                tint = HomeBalanceBarTint,
                                modifier = Modifier.size(HomeBalanceWalletIconSize)
                            )
                        }
                    },
                    text = walletBal
                )
                BalanceChip(
                    icon = {
                        if (creditChipIcon != null) {
                            Image(
                                bitmap = creditChipIcon,
                                contentDescription = null,
                                modifier = Modifier.size(HomeBalanceCreditIconSize),
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
                    text = creditBal
                )
            }
        } else {
            Spacer(modifier = Modifier.height(6.dp + secondaryBalancesReservedHeight))
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

@Composable
private fun HomeCardPlaceholder(
    kind: HomeCarouselCardKind = HomeCarouselCardKind.Black,
    onCardClick: () -> Unit = {}
) {
    val admin = LocalAppAdmin.current
    val kreditFront = rememberKreditFrontFontFamily()
    val schemeTypefaceFamily = MaterialTheme.typography.titleMedium.fontFamily
    val title = stringResource(
        when (kind) {
            HomeCarouselCardKind.Black -> R.string.home_carousel_card_black
            HomeCarouselCardKind.WhiteBlackEdge -> R.string.home_carousel_card_white_black_edge
            HomeCarouselCardKind.BlackGreenEdgeUsd -> R.string.home_carousel_card_black_green_usd
            HomeCarouselCardKind.BlackRedEdgeEur -> R.string.home_carousel_card_black_red_eur
        }
    )
    val defaultNumber = stringResource(R.string.home_card_masked_number)
    val numberRaw = when (kind) {
        HomeCarouselCardKind.Black -> admin?.state?.cardOrDefault(0)?.cardNumber
        HomeCarouselCardKind.WhiteBlackEdge -> admin?.state?.cardOrDefault(1)?.cardNumber
        HomeCarouselCardKind.BlackGreenEdgeUsd -> admin?.state?.cardOrDefault(2)?.cardNumber
        HomeCarouselCardKind.BlackRedEdgeEur -> admin?.state?.cardOrDefault(3)?.cardNumber
    }?.takeIf { it.isNotBlank() } ?: defaultNumber
    val number = maskCardNumberForDisplay(numberRaw)
    val visaLogo = rememberAssetImageBitmap(CardVisaLogoAsset)
    val monobankLogo = rememberAssetImageBitmap(CardMonobankNegateAsset)
    val visaCd = stringResource(R.string.home_card_scheme)

    val rotX = 65f
    val rotY = 0f
    val rotZ = 0f
    val cardTransY = 10f
    val cameraFactor = 15f

    val numberColor = if (kind == HomeCarouselCardKind.WhiteBlackEdge) {
        Color(0xFF2C2C32)
    } else {
        Color(0xFFB1B2B7)
    }
    val numberStyle = TextStyle(
        fontFamily = kreditFront,
        fontSize = 27.sp,
        letterSpacing = 1.7.sp,
        fontWeight = FontWeight.ExtraBold,
        color = numberColor
    )
    val visaFallbackColor = if (kind == HomeCarouselCardKind.WhiteBlackEdge) {
        Color(0xFF1A1C22)
    } else {
        Color.White
    }
    val visaFallbackStyle = TextStyle(
        fontFamily = schemeTypefaceFamily ?: FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = visaFallbackColor
    )

    val density = LocalDensity.current.density
    val plateOffsetY =
        HomeCardPlateOffsetY + (-HomeCardPlateExtraLiftPx / density).dp + HomeCardCarouselPlateNudgeY

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HomeCardCarouselPagerVisualHeight)
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
            cardUnfoldProgress = 0f,
            faceColors = kind.faceColors(),
            logoColorFilter = kind.negateLogoColorFilter(),
            onCardClick = onCardClick,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = plateOffsetY)
        )
    }
}

private fun maskCardNumberForDisplay(rawNumber: String): String {
    val digits = rawNumber.filter { it.isDigit() }
    if (digits.length < 8) return rawNumber
    val first4 = digits.take(4)
    val last4 = digits.takeLast(4)
    return "$first4 **** **** $last4"
}

/**
 * Екран картки після натискання на пластину: картка зверху (розгортання + обертання), список-заглушка.
 */
@Composable
internal fun HomeCardDetailScreen(onClose: () -> Unit) {
    val kreditFront = rememberKreditFrontFontFamily()
    val schemeTypefaceFamily = MaterialTheme.typography.titleMedium.fontFamily
    val title = stringResource(R.string.home_card_placeholder)
    val number = stringResource(R.string.home_card_masked_number)
    val visaLogo = rememberAssetImageBitmap(CardVisaLogoAsset)
    val monobankLogo = rememberAssetImageBitmap(CardMonobankNegateAsset)
    val visaCd = stringResource(R.string.home_card_scheme)
    val closeCd = stringResource(R.string.home_card_detail_close_cd)

    var targetRotX by remember { mutableFloatStateOf(65f) }
    var targetRotY by remember { mutableFloatStateOf(-18f) }
    LaunchedEffect(Unit) {
        targetRotX = 0f
        targetRotY = 0f
    }
    val rotX by animateFloatAsState(
        targetValue = targetRotX,
        animationSpec = tween(520, easing = FastOutSlowInEasing),
        label = "detailCardRotX"
    )
    val rotY by animateFloatAsState(
        targetValue = targetRotY,
        animationSpec = tween(520, easing = FastOutSlowInEasing),
        label = "detailCardRotY"
    )

    val numberStyle = TextStyle(
        fontFamily = kreditFront,
        fontSize = 25.sp,
        letterSpacing = 1.7.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFBCBCBC)
    )
    val visaFallbackStyle = TextStyle(
        fontFamily = schemeTypefaceFamily ?: FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    val detailScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = closeCd,
                    tint = TextPrimary
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(detailScroll)
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 20.dp),
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
                    rotationZDegrees = 0f,
                    cameraDistanceFactor = 22f,
                    translationY = 0f,
                    cardUnfoldProgress = 1f,
                    onCardClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(
                text = stringResource(R.string.home_card_detail_section_title),
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Surface(
                shape = CardShape,
                color = OperationsBlockColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.home_card_detail_stub_row),
                    color = PinPromptText,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HomeAllCardsChip(modifier: Modifier = Modifier) {
    val allCardsChip = rememberAssetImageBitmap(HomeAllCardsChipAsset)
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 86.dp, height = 20.5.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            contentAlignment = Alignment.Center
        ) {
            if (allCardsChip != null) {
                Image(
                    bitmap = allCardsChip,
                    contentDescription = stringResource(R.string.home_all_cards),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun HomeQuickActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickAction(stringResource(R.string.home_action_transfer)) {
            Image(
                painter = painterResource(R.drawable.cardpay),
                contentDescription = null,
                modifier = Modifier.size(HomeQuickActionIconSize),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(QuickActionIconTint)
            )
        }
        QuickAction(stringResource(R.string.home_action_iban)) {
            Image(
                painter = painterResource(R.drawable.ibanpay),
                contentDescription = null,
                modifier = Modifier.size(HomeQuickActionIconSize),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(QuickActionIconTint)
            )
        }
        QuickAction(stringResource(R.string.home_action_other)) {
            Image(
                painter = painterResource(R.drawable.other_payments),
                contentDescription = null,
                modifier = Modifier.size(HomeQuickActionIconSize),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(QuickActionIconTint)
            )
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

private data class HomeOperationUi(
    val title: String,
    val amount: String,
    val dateLabel: String,
    val commissionAmount: String?,
    val logoAssetName: String?,
    val logoCircleBackground: Color = AvatarPlaceholder,
    val fallbackIcon: ImageVector? = null,
    val fallbackIconTint: Color = HomeBalanceBarTint
)

@Composable
private fun HomeOperationsCard(
    kind: HomeCarouselCardKind,
    modifier: Modifier = Modifier
) {
    val admin = LocalAppAdmin.current
    var allOpsOpen by remember { mutableStateOf(false) }
    var selectedOperation by remember { mutableStateOf<HomeOperationUi?>(null) }
    val cardIndex = when (kind) {
        HomeCarouselCardKind.Black -> 0
        HomeCarouselCardKind.WhiteBlackEdge -> 1
        HomeCarouselCardKind.BlackGreenEdgeUsd -> 2
        HomeCarouselCardKind.BlackRedEdgeEur -> 3
    }
    val defaultOps = listOf(
        HomeOperationUi(
            title = stringResource(R.string.home_op_steam),
            amount = stringResource(R.string.home_op_steam_amount),
            dateLabel = "Сьогодні",
            commissionAmount = null,
            logoAssetName = HomeOperationTransferAssetName
        ),
        HomeOperationUi(
            title = stringResource(R.string.home_op_card),
            amount = stringResource(R.string.home_op_card_amount),
            dateLabel = "Вчора",
            commissionAmount = null,
            logoAssetName = HomeOperationTransferAssetName,
            fallbackIcon = Icons.Filled.CreditCard
        ),
        HomeOperationUi(
            title = stringResource(R.string.home_op_mcd),
            amount = stringResource(R.string.home_op_mcd_amount),
            dateLabel = "14 квітня 2026",
            commissionAmount = null,
            logoAssetName = HomeOperationTransferAssetName,
            logoCircleBackground = Color(0xFFC8102E),
            fallbackIcon = Icons.Filled.Restaurant,
            fallbackIconTint = Color.White
        )
    )
    val configuredOps = admin?.state
        ?.cardOrDefault(cardIndex)
        ?.operations
        ?.filter { it.title.isNotBlank() || it.amount.isNotBlank() }
        ?.map { op ->
            HomeOperationUi(
                title = op.title.ifBlank { stringResource(R.string.admin_card_operation_default_title) },
                amount = op.amount.ifBlank { stringResource(R.string.admin_card_operation_default_amount) },
                dateLabel = op.dateLabel.ifBlank { "Без дати" },
                commissionAmount = op.commissionAmount.takeIf { op.hasCommission && it.isNotBlank() },
                logoAssetName = HomeOperationTransferAssetName,
                fallbackIcon = Icons.Filled.CreditCard
            )
        }
        .orEmpty()
    val opsToRender = if (configuredOps.isNotEmpty()) configuredOps else defaultOps
    val topThreeOps = opsToRender.take(3)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CardShape,
        color = OperationsBlockColor
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 18.dp,
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
                    fontSize = HomeOperationsTitleFontSize,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .clip(OperationsAllChipShape)
                        .background(HomeOperationsAllChipBackground)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { allOpsOpen = true }
                        .padding(
                            horizontal = OperationsAllChipPaddingH,
                            vertical = OperationsAllChipPaddingV
                        )
                ) {
                    Text(
                        text = stringResource(R.string.home_operations_all) + " ›",
                        color = HomeOperationsAllChipText,
                        fontSize = OperationsAllChipFontSize,
                        lineHeight = OperationsAllChipLineHeight,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            topThreeOps.forEachIndexed { index, operation ->
                OperationRow(
                    title = operation.title,
                    amount = operation.amount,
                    logoAssetName = operation.logoAssetName,
                    logoCircleBackground = operation.logoCircleBackground,
                    fallbackIcon = operation.fallbackIcon,
                    fallbackIconTint = operation.fallbackIconTint,
                    onClick = { selectedOperation = operation }
                )
                if (index != topThreeOps.lastIndex) {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
    if (allOpsOpen) {
        HomeAllOperationsScreen(
            operations = opsToRender,
            onDismiss = { allOpsOpen = false },
            onOperationClick = { selectedOperation = it }
        )
    }
    selectedOperation?.let { operation ->
        HomeOperationDetailScreen(
            operation = operation,
            onDismiss = { selectedOperation = null }
        )
    }
}

@Composable
private fun OperationRow(
    title: String,
    amount: String,
    logoAssetName: String?,
    logoCircleBackground: Color = AvatarPlaceholder,
    fallbackIcon: ImageVector? = null,
    fallbackIconTint: Color = HomeBalanceBarTint,
    onClick: () -> Unit = {}
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
            ) { onClick() },
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 14.dp)
        ) {
            Text(
                text = title,
                color = HomeOperationRowTextColor,
                fontSize = HomeOperationRowFontSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = amount,
            color = HomeOperationRowTextColor,
            fontSize = HomeOperationRowFontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(min = 82.dp)
        )
    }
}

@Composable
private fun HomeAllOperationsScreen(
    operations: List<HomeOperationUi>,
    onDismiss: () -> Unit,
    onOperationClick: (HomeOperationUi) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF09090A))
                .statusBarsPadding()
        ) {
            val grouped = operations.groupBy { it.dateLabel }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.home_card_detail_close_cd),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.home_charts_cd),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "200.49 ₴",
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    grouped.forEach { (dateLabel, rows) ->
                        Text(
                            text = dateLabel,
                            color = PinPromptText,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        rows.forEachIndexed { index, row ->
                            OperationRow(
                                title = row.title,
                                amount = row.amount,
                                logoAssetName = row.logoAssetName,
                                logoCircleBackground = row.logoCircleBackground,
                                fallbackIcon = row.fallbackIcon,
                                fallbackIconTint = row.fallbackIconTint,
                                onClick = { onOperationClick(row) }
                            )
                            if (index != rows.lastIndex) {
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeOperationDetailScreen(
    operation: HomeOperationUi,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B0B0D))
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.jar_bank_back_cd),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF242428)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = operation.title,
                            color = Color.White,
                            fontSize = 34.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = operation.dateLabel,
                            color = PinPromptText,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = operation.amount,
                            color = Color.White,
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Light
                        )
                        operation.commissionAmount?.let { commission ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.home_operation_commission, commission),
                                color = PinPromptText,
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeLimitsAbroadCard() {
    val borderBrush = Brush.horizontalGradient(listOf(LimitsGradientStart, LimitsGradientEnd))
    val limitsIcon = rememberAssetImageBitmap(HomeLimitsAsset)
    val foreignIcon = rememberAssetImageBitmap(HomeForeignAsset)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, borderBrush, CardShape),
        shape = CardShape,
        color = Color(0xFF262626)
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
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (limitsIcon != null) {
                    Image(
                        bitmap = limitsIcon,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(Icons.Outlined.Business, null, tint = LimitsGradientStart, modifier = Modifier.size(28.dp))
                }
                Text(
                    text = stringResource(R.string.home_limits_left),
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
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
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (foreignIcon != null) {
                    Image(
                        bitmap = foreignIcon,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(Icons.Outlined.Language, null, tint = LimitsGradientEnd, modifier = Modifier.size(28.dp))
                }
                Text(
                    text = stringResource(R.string.home_limits_right),
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold
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
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            )
        ) {
            Text(
                text = stringResource(R.string.home_useful_title),
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HomeUsefulInnerHorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RateRow(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HomeUsefulInnerHorizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    UsefulTile(
                        assetPath = HomeUsefulSupportAsset,
                        label = stringResource(R.string.home_useful_support),
                        Modifier.weight(1f),
                        tileHeight = 82.dp,
                        iconSize = 30.dp
                    )
                    UsefulTile(
                        assetPath = HomeUsefulQuestionsAsset,
                        label = stringResource(R.string.home_useful_faq),
                        Modifier.weight(1f),
                        tileHeight = 82.dp,
                        iconSize = 26.dp
                    )
                    UsefulTile(
                        assetPath = HomeUsefulQrAsset,
                        label = stringResource(R.string.home_useful_qr),
                        Modifier.weight(1f),
                        tileHeight = 82.dp,
                        iconSize = 30.dp
                    )
                }
                Spacer(modifier = Modifier.height(HomeUsefulTilesRowSpacing))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        HomeUsefulTilesHorizontalSpacing,
                        Alignment.CenterHorizontally
                    )
                ) {
                    UsefulTile(
                        assetPath = HomeUsefulStatementsAsset,
                        label = stringResource(R.string.home_useful_statements),
                        Modifier.weight(1f),
                        tileHeight = 82.dp,
                        iconSize = 30.dp
                    )
                    UsefulTile(
                        assetPath = HomeUsefulTermsAsset,
                        label = stringResource(R.string.home_useful_tariffs),
                        Modifier.weight(1f),
                        tileHeight = 82.dp,
                        iconSize = 30.dp
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
                iconAsset = HomeUsdAsset,
                title = stringResource(R.string.home_rate_usd_title),
                values = stringResource(R.string.home_rate_usd_values),
                modifier = Modifier.weight(1f)
            )
            RateCurrencyItem(
                iconAsset = HomeEurAsset,
                title = stringResource(R.string.home_rate_eur_title),
                values = stringResource(R.string.home_rate_eur_values),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RateCurrencyItem(
    iconAsset: String,
    title: String,
    values: String,
    modifier: Modifier = Modifier
) {
    val iconBitmap = rememberAssetImageBitmap(iconAsset)
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
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
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
    assetPath: String,
    label: String,
    modifier: Modifier = Modifier,
    tileHeight: androidx.compose.ui.unit.Dp = 82.dp,
    iconSize: androidx.compose.ui.unit.Dp = 30.dp
) {
    val iconBitmap = rememberAssetImageBitmap(assetPath)
    Surface(
        modifier = modifier
            .height(tileHeight)
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
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HomeBottomBarShade(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(HomeBottomBarShadeHeight)
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.48f to Color.Transparent,
                        0.68f to Color.Black.copy(alpha = 0.08f),
                        0.86f to Color.Black.copy(alpha = 0.26f),
                        1f to Color.Black.copy(alpha = 0.50f)
                    )
                )
            )
    )
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
                .height(BottomBarHeight),
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
                .size(BottomBarHeight),
            shape = CircleShape,
            color = HomeBottomBarFill,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            onClick = { onTabSelected(HomeBottomNavTab.Market) }
        ) {
            val marketTint =
                if (selectedTab == HomeBottomNavTab.Market) HomeNavIconActive else HomeNavIconInactive
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

    val tint = if (selected) HomeNavIconActive else HomeNavIconInactive
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
    val context = LocalContext.current
    val savingsNavLottieFontMap = remember(context) {
        val tf: Typeface? = ResourcesCompat.getFont(context, R.font.roboto_bold)
        if (tf != null) mapOf("Roboto-Bold" to tf) else emptyMap()
    }
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
        anim.snapTo(c, progressForFrame(c, SavingsNavTapMaxFrameInclusive.toFloat()))
    }

    val tint = if (selected) HomeNavIconActive else HomeNavIconInactive
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
            enableMergePaths = true,
            fontMap = savingsNavLottieFontMap
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

    val tint = if (selected) HomeNavIconActive else HomeNavIconInactive
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
    Image(
        painter = painterResource(R.drawable.gift),
        contentDescription = null,
        modifier = modifier.graphicsLayer(scaleY = HomeTopAssetIconScaleY),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(HomeBalanceMainAmountColor)
    )
}

private fun progressForFrame(composition: LottieComposition, frame: Float): Float =
    composition.getProgressForFrame(frame)
