package com.konvert.app.ui.home

import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.graphics.Typeface
import kotlin.math.abs
import kotlin.math.floor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clipToBounds
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
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.pow
import java.time.LocalDate

private val CardShape = RoundedCornerShape(24.dp)
private val ChipShape = RoundedCornerShape(20.dp)
/** РљР°РїСЃСѓР»Р° В«РЈСЃС– вЂєВ» вЂ” РІРµР»РёРєРёР№ СЂР°РґС–СѓСЃ (РІС–Р·СѓР°Р»СЊРЅРѕ СЏРє РЅР° СЂРµС„РµСЂРµРЅСЃС–). */
private val OperationsAllChipShape = RoundedCornerShape(999.dp)
/** РќРёР·СЊРєР° РєР°РїСЃСѓР»Р° СЏРє РЅР° СЂРµС„РµСЂРµРЅСЃС– monobank вЂ” РјР°Р»Рѕ РІРµСЂС‚РёРєР°Р»СЊРЅРѕРіРѕ РїРѕРІС–С‚СЂСЏ. */
private val OperationsAllChipPaddingH = 16.dp
private val OperationsAllChipPaddingV = 4.dp
private val OperationsAllChipFontSize = 14.sp
private val OperationsAllChipLineHeight = 14.sp
private val ActionCircleSize = 56.dp

/** Р’РёСЃРѕС‚Р° РЅРёР¶РЅСЊРѕС— В«РїС–РіСѓР»РєРёВ» С‚Р° РњР°СЂРєРµС‚Сѓ вЂ” СЂР°РґС–СѓСЃ = РїРѕР»РѕРІРёРЅР° РІРёСЃРѕС‚Рё (РєР°РїСЃСѓР»Р° / РєРѕР»Рѕ). */
private val BottomBarHeight = 72.dp
private val BottomBarPillRadius = 36.dp
private val BottomBarGap = 4.dp

/** Lottie РІ РјРµРЅСЋ; СЃС‚Р°С‚РёС‡РЅР° С–РєРѕРЅРєР° В«РњР°СЂРєРµС‚В» С‚СЂРѕС…Рё РјРµРЅС€Р°. */
private val BottomBarNavLottieIconSize = 40.dp
private val BottomBarNavStaticIconSize = 28.dp

/** Р’С–РґСЃС‚СѓРї РїС–РґРїРёСЃСѓ РІС–Рґ РЅРёР¶РЅСЊРѕРіРѕ РєСЂР°СЋ РєРѕРјС–СЂРєРё (РјР°Р»РёР№ вЂ” С‚РµРєСЃС‚ Р±Р»РёР¶С‡Рµ РґРѕ С–РєРѕРЅРєРё). */
private val BottomBarNavLabelBottomPadding = 2.dp

/** РџС–РґРїРёСЃРё РІ РЅРёР¶РЅС–Р№ РїР°РЅРµР»С–: `roboto_bold` РЅР° РІСЃСЋ С€РёСЂРёРЅСѓ РіР»С–С„Р° (РґРёРІ. [BottomBarNavLabelFontFamily]). */
private val BottomBarNavLabelFontSize = 11.75.sp
private val BottomBarNavLabelLineHeight = 14.25.sp
private val BottomBarNavLabelLetterSpacing = (-0.1).sp

private val BottomBarNavLabelFontFamily = FontFamily(
    Font(R.font.roboto_bold, FontWeight.Normal, FontStyle.Normal)
)
private val HomeBottomBarShadeHeight = 210.dp

/**
 * Р’РµСЂС‚РёРєР°Р»СЊРЅС– РІС–РґСЃС‚СѓРїРё [HomeBottomBar] РЅР°РІРєРѕР»Рѕ РїС–РіСѓР»РєРё (РґРёРІ. [HomeBottomBar] `padding(vertical = 8.dp)`).
 * РџРѕС‚СЂС–Р±РЅС– РґР»СЏ РЅРёР¶РЅСЊРѕРіРѕ [contentPadding] [LazyColumn], С‰РѕР± В«РљРѕСЂРёСЃРЅРµВ» РЅРµ С…РѕРІР°Р»РѕСЃСЊ РїС–Рґ РїР°РЅРµР»Р»СЋ.
 */
private val HomeBottomBarRowVerticalPadding = 16.dp

/** Р”РѕРґР°С‚РєРѕРІРёР№ Р·Р°Р·РѕСЂ РјС–Р¶ РІРµСЂС…РѕРј РїР»Р°РІР°СЋС‡РѕС— РїР°РЅРµР»С– Р№ РѕСЃС‚Р°РЅРЅС–Рј Р±Р»РѕРєРѕРј РїС–СЃР»СЏ РїРѕРІРЅРѕРіРѕ СЃРєСЂРѕР»Сѓ. */
private val HomeCardsListBottomGapBeyondBar = 8.dp
private val HomeCardsListBottomTailTrim = 48.dp

/** Р’С–РґСЃС‚СѓРї РїС–Рґ С‚РѕРї-Р±Р°СЂРѕРј РїРµСЂРµРґ Р±Р»РѕРєРѕРј Р±Р°Р»Р°РЅСЃСѓ (РЅРµ РјС–Р¶ Р±Р°Р»Р°РЅСЃРѕРј С– РєР°СЂС‚РѕСЋ). */
private val HomeTopBarToBalancePaddingDp = 44.dp
private const val HomeTopBarToBalancePaddingPx: Float = 92f

/** Р“РѕСЂРёР·РѕРЅС‚Р°Р»СЊРЅРёР№ РІС–РґСЃС‚СѓРї [LazyColumn]. */
private val HomeCardsLazyHorizontalPadding = 14.dp

/**
 * Р“РѕСЂРёР·РѕРЅС‚Р°Р»СЊРЅРёР№ inset [HorizontalPager] вЂ” С‚РѕРЅРєС– СЃРјСѓРіРё СЃСѓСЃС–РґРЅС–С… РєР°СЂС‚ Р±С–Р»СЏ С†РµРЅС‚СЂР°Р»СЊРЅРѕС—,
 * Р±Р»РёР·СЊРєРѕ РґРѕ РѕСЃРЅРѕРІРЅРѕС— РїР»Р°СЃС‚РёРЅРё (РЅРµ В«С€РёСЂРѕРєРёР№В» carousel).
 */
private val HomeCardsPagerHorizontalPeek = 8.dp

/** РњС–РЅС–РјР°Р»СЊРЅРёР№ Р·Р°Р·РѕСЂ РјС–Р¶ СЃС‚РѕСЂС–РЅРєР°РјРё РІ РїРµР№РґР¶РµСЂС– (РѕСЃРЅРѕРІРЅРёР№ СЂСѓС… вЂ” РЅР°С‚РёРІРЅРёР№ scroll РїРµР№РґР¶РµСЂР°). */
private val HomeCardsPagerPageSpacing = 0.dp
/** Р”РѕРґР°С‚РєРѕРІРµ Р·Р±Р»РёР¶РµРЅРЅСЏ С‚С–Р»СЊРєРё РїР»Р°СЃС‚РёРєРѕРІРёС… РєР°СЂС‚ РјС–Р¶ СЃРѕР±РѕСЋ (Р±РµР· Р·Р±Р»РёР¶РµРЅРЅСЏ С€РёСЂРѕРєРёС… Р±Р»РѕРєС–РІ РѕРїРµСЂР°С†С–Р№). */
private val HomeCardsPlateNeighborExtraPull = 52.dp

/** РњС–Р¶ РЅРёР¶РЅС–Рј РєСЂР°С”Рј Р±Р°Р»Р°РЅСЃСѓ (С‡РёРїРё) С– РІРµСЂС…РѕРј РєР°СЂСѓСЃРµР»С–. */
private val HomeSectionGapBalanceToCard = 48.dp

/** Р—РѕРІРЅС–С€РЅС–Р№ Р±Р»РѕРє РЅР°РІРєРѕР»Рѕ РїСѓРЅРєС‚С–РІ В«РћСЃРѕР±РёСЃС‚С– РґР°РЅС–вЂ¦В» / В«РќР°Р»Р°С€С‚СѓРІР°РЅРЅСЏвЂ¦В». */
private val HomeProfileMenuOuterBlockColor = Color(0xFF303030)
/** Р¤РѕРЅ СЂСЏРґРєС–РІ СѓСЃРµСЂРµРґРёРЅС– С†СЊРѕРіРѕ Р±Р»РѕРєСѓ. */
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
 * Р’РёСЃРѕС‚Р°, СЏРєСѓ Р·Р°Р№РјР°С” Р±Р»РѕРє РєР°СЂСѓСЃРµР»С– РІ [LazyColumn] вЂ” РјР°Р»Р°, С‰РѕР± В«РЈСЃС– РєР°СЂС‚РєРёВ» С‚Р° РЅРёР¶РЅС–Р№ РєРѕРЅС‚РµРЅС‚ РїС–РґРЅСЏР»РёСЃСЊ.
 * РњР°С” Р±СѓС‚Рё РґРѕСЃС‚Р°С‚РЅСЊРѕСЋ РїС–Рґ РІРёС‰Сѓ РїР»Р°СЃС‚РёРЅСѓ [HomeBankCardFrame] + РЅР°С…РёР».
 */
private val HomeCardCarouselLayoutReserveHeight = 230.dp

/** Р’РёСЃРѕС‚Р° СЃР»РѕС‚Р° РїС–Рґ [HomeCardPlaceholder] вЂ” РїС–Рґ РІРёС‰Сѓ/С€РёСЂС€Сѓ РїР»Р°СЃС‚РёРЅСѓ РІ [HomeMonoTiltedCard]. */
private val HomeCardCarouselPagerVisualHeight = 240.dp
private val HomeCardCarouselMistTopOffset = 138.dp
private val HomeCardCarouselMistHeight = 150.dp

/**
 * Р’РµСЂС‚РёРєР°Р»СЊ РїР»Р°СЃС‚РёРєРё РЅР° РіРѕР»РѕРІРЅРѕРјСѓ РµРєСЂР°РЅС– (РЅР°РєР»РѕРЅ В«РІС–Рґ РєРѕСЂРёСЃС‚СѓРІР°С‡Р°В»):
 * - [HomeCardPlateOffsetY] + [HomeCardPlateExtraLiftPx] вЂ” Р±Р°Р·РѕРІРёР№ Р·СЃСѓРІ Сѓ [HomeCardPlaceholder] (`plateOffsetY`);
 * - [HomeCardCarouselPlateNudgeY] вЂ” С‚РѕРЅРєРµ РґРѕРЅР°Р»Р°С€С‚СѓРІР°РЅРЅСЏ РІРіРѕСЂСѓ/РІРЅРёР· Р±РµР· РїРµСЂРµСЂР°С…СѓРЅРєСѓ px;
 * - Сѓ [HomeCardPlaceholder]: `rotX` (РєСѓС‚ РЅР°С…РёР»Сѓ), `cardTransY` (translationY Сѓ [HomeMonoTiltedCard], Р±С–Р»СЊС€Рµ вЂ” РЅРёР¶С‡Рµ РЅР° РµРєСЂР°РЅС–).
 */
private val HomeCardPlateOffsetY = (-40).dp

/** Р”РѕРґР°С‚РєРѕРІРѕ РїС–РґРЅСЏС‚Рё РїР»Р°СЃС‚РёРЅСѓ (px в†’ dp СЂР°Р·РѕРј С–Р· [HomeCardPlateOffsetY]). */
private const val HomeCardPlateExtraLiftPx: Float = 56f

/** Р”РѕРґР°С‚РєРѕРІРёР№ Р·СЃСѓРІ РїР»Р°СЃС‚РёРЅРё РІРіРѕСЂСѓ РЅР° РєР°СЂСѓСЃРµР»С– (РЅРµРіР°С‚РёРІРЅРёР№ = РІРёС‰Рµ). */
private val HomeCardCarouselPlateNudgeY = (-45).dp

/** РџС–РґРЅСЏС‚Рё РІРµСЃСЊ Р±Р»РѕРє Р±Р°Р»Р°РЅСЃСѓ РІРіРѕСЂСѓ (px в†’ dp Сѓ [HomeBalanceBlock]). */
private const val HomeBalanceVerticalLiftPx: Float = 50f

/**
 * Р—СЃСѓРІ С‡РёРїР° В«РЈСЃС– РєР°СЂС‚РєРёВ» РїРѕ РІРµСЂС‚РёРєР°Р»С– РІС–РґРЅРѕСЃРЅРѕ РјС–СЃС†СЏ РїС–СЃР»СЏ Р±Р»РѕРєСѓ РїР»Р°СЃС‚РёРЅРё.
 * **РќРµРіР°С‚РёРІРЅРёР№** вЂ” РјР°Р»СЋС” С‡РёРї РІРёС‰Рµ (Р±Р»РёР¶С‡Рµ РґРѕ РєР°СЂС‚РєРё). Р РµР°Р»С–Р·РѕРІР°РЅРѕ С‡РµСЂРµР· [Modifier.offset], Р±Рѕ
 * `Spacer(height = РЅРµРіР°С‚РёРІ)` Сѓ [Column] Р·Р°Р·РІРёС‡Р°Р№ **РѕР±СЂС–Р·Р°С”С‚СЊСЃСЏ РґРѕ 0** РїС–Рґ С‡Р°СЃ measure вЂ” РІРµР»РёРєС–
 * РІС–РґвЂ™С”РјРЅС– Р·РЅР°С‡РµРЅРЅСЏ С‚Р°Рј В«РЅРµ РїСЂР°С†СЋСЋС‚СЊВ».
 *
 * Р©РѕР± РїС–РґРЅСЏС‚Рё РєРѕРЅС‚РµРЅС‚ Сѓ **СЂРѕР·РєР»Р°РґС†С–** (Р° РЅРµ Р»РёС€Рµ РЅР°РјР°Р»СЋРІР°С‚Рё РІРёС‰Рµ), Р·РјРµРЅС€СѓР№С‚Рµ [HomeCardCarouselLayoutReserveHeight]
 * Р°Р±Рѕ РІРёСЃРѕС‚Сѓ СЃР»РѕС‚Р° [HomeCardCarouselPagerVisualHeight].
 */
private val HomeSectionGapCarouselToAllCards = (-122).dp

/**
 * Р”РѕРґР°С‚РєРѕРІРёР№ Р·СЃСѓРІ Р±Р»РѕРєСѓ В«РЁРІРёРґРєС– РґС–С—В» + В«РћРїРµСЂР°С†С–С—В» РІРіРѕСЂСѓ (РґРѕ С‡РёРїР° В«РЈСЃС– РєР°СЂС‚РєРёВ»), С‚РѕР№ СЃР°РјРёР№ РїСЂРёР№РѕРј, С‰Рѕ Р№ [Modifier.offset] РґР»СЏ С‡РёРїР°.
 * РќРµРіР°С‚РёРІРЅРёР№ вЂ” РІРёС‰Рµ.
 */
private val HomeSectionOffsetQuickActionsAndOperationsY = (-105).dp

/** РњС–Р¶ В«РЈСЃС– РєР°СЂС‚РєРёВ» С– С€РІРёРґРєРёРјРё РґС–СЏРјРё вЂ” ~40 px. */
private val HomeSectionGapAllCardsToQuick = 6.dp
/** РњС–Р¶ С€РІРёРґРєРёРјРё РґС–СЏРјРё С– Р±Р»РѕРєРѕРј В«РћРїРµСЂР°С†С–С—В». */
private val HomeSectionGapQuickToOperations = 8.dp
/** РњС–Р¶ Р±Р»РѕРєРѕРј В«РћРїРµСЂР°С†С–С—В» (РїРµСЂС€РёР№ item) С– В«Р›С–РјС–С‚Рё С‚Р° РѕР±РјРµР¶РµРЅРЅСЏВ». */
private val HomeSectionGapOperationsToLimits = 14.dp
/** РњС–Р¶ В«Р›С–РјС–С‚Рё С‚Р° РѕР±РјРµР¶РµРЅРЅСЏВ» С– В«РљРѕСЂРёСЃРЅРµВ». */
private val HomeSectionGapLimitsToUseful = 88.dp

/**
 * РџС–РґРЅСЏС‚Рё Р±Р»РѕРєРё **РїС–Рґ** В«РћРїРµСЂР°С†С–С—В» (Р»С–РјС–С‚Рё + РєРѕСЂРёСЃРЅРµ) РґРѕ РѕРїРµСЂР°С†С–Р№ С– РЅР° **С‚Сѓ СЃР°РјСѓ** РІРµР»РёС‡РёРЅСѓ СЃРєРѕСЂРѕС‚РёС‚Рё
 * РІРµСЂС‚РёРєР°Р»СЊРЅРёР№ С…РІС–СЃС‚ СЃС‚РѕСЂС–РЅРєРё: СЃРїРѕС‡Р°С‚РєСѓ Р·РјРµРЅС€СѓСЋС‚СЊСЃСЏ [HomeSectionGapOperationsToLimits] С‚Р°
 * [HomeSectionGapLimitsToUseful], СЂРµС€С‚Р° вЂ” Р· РЅРёР¶РЅСЊРѕРіРѕ [PaddingValues] [LazyColumn].
 */
private val HomeSectionCompactBelowOperationsDp = 102.dp

/** РЈ В«РљРѕСЂРёСЃРЅРµВ»: РѕРґРЅР°РєРѕРІРёР№ РіРѕСЂРёР·РѕРЅС‚Р°Р»СЊРЅРёР№ inset РґР»СЏ СЂСЏРґСѓ РєСѓСЂСЃС–РІ С– СЃС–С‚РєРё РїР»РёС‚РѕРє. */
private val HomeUsefulInnerHorizontalPadding = 0.dp
/** Р—Р°Р·РѕСЂ РјС–Р¶ РґРІРѕРјР° СЂСЏРґР°РјРё РїР»РёС‚РѕРє 2Г—2. */
private val HomeUsefulTilesRowSpacing = 16.dp
/** Р—Р°Р·РѕСЂ РјС–Р¶ РґРІРѕРјР° РїР»РёС‚РєР°РјРё РІ РѕРґРЅРѕРјСѓ СЂСЏРґСѓ. */
private val HomeUsefulTilesHorizontalSpacing = 18.dp
private val HomeQuickActionIconSize = 22.dp
private val HomeTopBarTopOffset = 6.dp
private val HomeTopProfileButtonSize = 36.dp
private val HomeTopProfileFallbackIconSize = 18.dp
private const val HomeTopAssetIconScaleY = 1.12f
private const val HomeTopProfileMessageScale = 1.22f
private const val HomeTopStatsScale = 1.36f
private val HomeBalanceWalletIconSize = 22.dp
private val HomeBalanceCreditIconSize = 22.dp
private val HomeOperationsTitleFontSize = 20.sp
private val HomeOperationRowFontSize = 16.sp
private val HomeOperationRowTextColor = Color(0xFFE4E2E3)

/** РљРѕР»С–СЂ РІРµР»РёРєРѕРіРѕ Р±Р°Р»Р°РЅСЃСѓ [R.string.home_balance_main] вЂ” РєРµС€Р±РµРє Сѓ С‚РѕРї-Р±Р°СЂС– С‚Р° СЃСѓСЃС–РґРЅС– С–РєРѕРЅРєРё. */
private val HomeBalanceMainAmountColor = Color(0xFFFFFFFF)

/** `assets/kreditfont/Kredit Front.otf` вЂ” С‚РµРєСЃС‚ РЅР° РєР°СЂС‚С†С– (РЅРµ Р»РѕРіРѕС‚РёРї VISA). */
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
/** Р†РєРѕРЅРєР° С‡РёРїР° В«РіР°РјР°РЅРµС†СЊВ» Р±С–Р»СЏ [R.string.home_balance_wallet] (2 144 в‚ґ). */
private const val CardWalletNegateAsset = "$OperationsLogosPath/wallet_negate.png"
private const val QuickActionBankCardNegateAsset = "$OperationsLogosPath/bank_card_negate.png"
private const val HomeProfileMessageAsset = "$OperationsLogosPath/profile_message.png"
private const val HomeProfilePersonalDataAsset = "$OperationsLogosPath/personal_data.png"
private const val HomeProfileSettingsAsset = "$OperationsLogosPath/settings.png"
private const val HomeProfileFopAsset = "$OperationsLogosPath/fop.png"
/** Р†РєРѕРЅРєР° С‡РёРїР° РєСЂРµРґРёС‚РЅРѕРіРѕ Р»С–РјС–С‚Сѓ Р±С–Р»СЏ [R.string.home_balance_credit]. */
private const val CardCreditSumAsset = "$OperationsLogosPath/credit_sum.png"
/** Р†РєРѕРЅРєР° С‡РёРїР° В«РЈСЃС– РєР°СЂС‚РєРёВ» РїС–Рґ РєР°СЂСѓСЃРµР»Р»СЋ. */
private const val HomeAllCardsChipAsset = "$OperationsLogosPath/all_cards.png"

private const val HomeStatsAsset = "$OperationsLogosPath/stats.png"
private const val HomeUsefulTermsAsset = "$OperationsLogosPath/terms_and_tarifs.png"
private const val HomeUsefulStatementsAsset = "$OperationsLogosPath/dovidky.png"
private const val HomeUsefulQrAsset = "$OperationsLogosPath/qr-scan.png"
private const val HomeUsefulQuestionsAsset = "$OperationsLogosPath/questions.png"
private const val HomeUsefulSupportAsset = "$OperationsLogosPath/support.png"
private const val HomeOperationTransferAssetName = "transfer.png"
private val HomeOperationDetailPatternIconSize = 40.dp
private val HomeOperationDetailPatternStepX = 40.dp
private val HomeOperationDetailPatternStepY = 40.dp
private const val HomeOperationDetailPatternAlpha = 0.3f
private const val HomeOperationDetailPencilAsset = "$OperationsLogosPath/pencil.png"
private const val HomeOperationDetailReferAsset = "$OperationsLogosPath/refer.png"
private const val HomeOperationDetailTagsAsset = "$OperationsLogosPath/tags.png"
private const val HomeOperationDetailWalletAsset = "$OperationsLogosPath/wallet_left.png"
private const val HomeOperationDetailCopyLinkAsset = "$OperationsLogosPath/copy_link.png"
private const val HomeOperationDetailLinkAsset = "$OperationsLogosPath/link_blue.png"
private const val HomeOperationDetailShareAsset = "$OperationsLogosPath/share_negate.png"
private const val HomeOperationDetailRepeatAsset = "$OperationsLogosPath/repeat.png"
private const val HomeOperationDetailSplitAsset = "$OperationsLogosPath/split.png"
private const val HomeOperationDetailQuestionAsset = "$OperationsLogosPath/Question.png"
private const val HomeOperationDetailRegularPaymentAsset = "$OperationsLogosPath/regular_payment.png"
private const val HomeOperationDetailSaveCardAsset = "$OperationsLogosPath/save_card.png"
private const val HomeOperationDetailShowPdfAsset = "$OperationsLogosPath/show_pdf.png"
private const val HomeOperationReceiptLoaderAsset = "animations/loader.json"
private const val HomeLimitsAsset = "$OperationsLogosPath/limits_.png"
private const val HomeForeignAsset = "$OperationsLogosPath/foreign.png"
private const val HomeUsdAsset = "$OperationsLogosPath/usd.png"
private const val HomeEurAsset = "$OperationsLogosPath/eur.png"
private const val HomeGraphNegateAsset = "graph_negate.png"
private const val HomeGraphNegateInOperationsLogos = "$OperationsLogosPath/graph_negate.png"
private const val HomeCatIconAsset = "$OperationsLogosPath/cat_icon.png"

private const val HomeGiftBoxNegateAsset = "gift-box_negate.png"
private const val HomeGiftBoxNegateInOperationsLogos = "$OperationsLogosPath/gift-box_negate.png"

/** РЁР»СЏС…Рё РґРѕ С–РєРѕРЅРєРё РєРµС€Р±РµРєСѓ Р±С–Р»СЏ СЃСѓРјРё [R.string.home_bonus_amount] (СЂС–Р·РЅС– С–РјРµРЅР° С„Р°Р№Р»С–РІ Сѓ Р·Р±С–СЂРєР°С…). */
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

/** Lottie В«РљР°СЂС‚РєРёВ» / В«РљСЂРµРґРёС‚РёВ»: СЃРµРіРјРµРЅС‚ РїРѕ С‚Р°РїСѓ Р°Р±Рѕ РїС–СЃР»СЏ РїРѕРІРµСЂРЅРµРЅРЅСЏ вЂ” РєР°РґСЂРё 0вЂ¦9 (10 РєР°РґСЂС–РІ). */
private const val NavLottieTapMaxFrameInclusive = 15

/** Lottie В«Р©РµВ»: РїСЂРё С‚Р°РїС– вЂ” РєР°РґСЂРё 0вЂ¦14 (15 РєР°РґСЂС–РІ). */
private const val MoreNavTapMaxFrameInclusive = 16

/** Lottie В«РќР°РєРѕРїРёС‡РµРЅРЅСЏВ»: РїСЂРё С‚Р°РїС– вЂ” РєР°РґСЂРё 0вЂ¦14 (15 РєР°РґСЂС–РІ), РїС–СЃР»СЏ РІС–РґС‚РІРѕСЂРµРЅРЅСЏ Р·Р°Р»РёС€Р°С”РјРѕСЃСЊ РЅР° РѕСЃС‚Р°РЅРЅСЊРѕРјСѓ. */
private const val SavingsNavTapMaxFrameInclusive = 16

private const val CardsNavLottieAsset = "animations/cards_icon.json"
private const val CreditsNavLottieAsset = "animations/credits_icon.json"
private const val MoreNavLottieAsset = "animations/more_icon.json"
private const val SavingsNavLottieAsset = "animations/deposits_dnm_16_quick.json"
private const val MarketNavLottieAsset = "animations/market20_icon.json"

/** РђРєС‚РёРІРЅР° РІРєР»Р°РґРєР°: Рј'СЏРєРёР№ tint Lottie РґРѕ #fd8688; РЅРµР°РєС‚РёРІРЅР° вЂ” РїСЂРёРіР»СѓС€РµРЅРЅСЏ. */
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
 * Р’РµСЂС‚РёРєР°Р»СЊРЅС– РїР°Р»С–С‚СЂРё С„РѕРЅСѓ вЂ” РїРѕ РѕРґРЅС–Р№ РЅР° РєРѕР¶РЅСѓ СЃС‚РѕСЂС–РЅРєСѓ РєР°СЂСѓСЃРµР»С– РєР°СЂС‚ (СѓР·РіРѕРґР¶РµРЅРѕ Р· [HomeCardsCarouselPageCount]).
 * РџРѕСЂСЏРґРѕРє: С‡РѕСЂРЅР° в†’ Р±С–Р»Р° (РїСѓСЂРїСѓСЂ) в†’ С‡РѕСЂРЅРѕ-Р·РµР»РµРЅР° (С‚РµРјРЅР° Р±С–СЂСЋР·Р°) в†’ С‡РѕСЂРЅРѕ-С‡РµСЂРІРѕРЅР° (РјР°Р»РёРЅР°).
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

/** Р—Р°С‚РµРјРЅРµРЅРЅСЏ РґР»СЏ СЂРµР¶РёРјСѓ В«Р»РёС€Рµ РІС–РєРЅРѕВ» (РЅРµРјР°С” РІРёРјС–СЂСЏРЅРѕС— РІРёСЃРѕС‚Рё РєРѕРЅС‚РµРЅС‚Сѓ). */
private val HomeBgOverlayStops: Array<Pair<Float, Color>> = arrayOf(
    0.00f to Color.Transparent,
    0.83f to Color.Transparent,
    0.90f to Color(0x44000000),
    0.96f to Color(0xCC121212),
    1.00f to Color(0xFF121212)
)

/**
 * Р’РµСЂС‚РёРєР°Р»СЊРЅРёР№ РіСЂР°РґС–С”РЅС‚ Сѓ РєРѕРѕСЂРґРёРЅР°С‚Р°С… СѓСЃСЊРѕРіРѕ СЃРєСЂРѕР»-РєРѕРЅС‚РµРЅС‚Сѓ (СЏРє Сѓ HEAD: С‚С– СЃР°РјС– СЃС‚РѕРїРё 0/35/45/55/100%),
 * РѕРєСЂРµРјР° РїР°Р»С–С‚СЂР° РЅР° РєРѕР¶РЅСѓ РєР°СЂС‚РєСѓ вЂ” С–РЅС‚РµСЂРїРѕР»СЏС†С–СЏ РїРѕ [cardScrollPosition] СЏРє Сѓ [HomeBgMainPalettes].
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
    /** Р‘РµР·РїРµСЂРµСЂРІРЅР° РїРѕР·РёС†С–СЏ РєР°СЂСѓСЃРµР»С–: `currentPage + currentPageOffsetFraction` (0вЂ¦paletteCountв€’1). */
    cardScrollPosition: Float = 0f,
    /** РЎРєС–Р»СЊРєРё РїР°Р»С–С‚СЂ РІРёРєРѕСЂРёСЃС‚РѕРІСѓРІР°С‚Рё (1 вЂ” Р»РёС€Рµ РїРµСЂС€Р°, РґР»СЏ С–РЅС€РёС… РІРєР»Р°РґРѕРє). */
    cardBackgroundPaletteCount: Int = 1,
    /**
     * Р’РёСЃРѕС‚Р° РІСЃСЊРѕРіРѕ СЃРєСЂРѕР»-РєРѕРЅС‚РµРЅС‚Сѓ РІРєР»Р°РґРєРё В«РљР°СЂС‚РєРёВ» (РІС–Рґ РІРµСЂС…Сѓ РґРѕ РЅРёР·Сѓ В«РљРѕСЂРёСЃРЅРµВ»), px.
     * РЇРєС‰Рѕ в‰¤ 1 вЂ” РіСЂР°РґС–С”РЅС‚ Сѓ СЂРµР¶РёРјС– РІРёСЃРѕС‚Рё РІС–РєРЅР°.
     */
    contentHeightPx: Float = 0f,
    /** Р—СЃСѓРІ СЃРєСЂРѕР»Сѓ РїРµСЂС€РѕРіРѕ item [LazyColumn] (px), С‰РѕР± Р·СЂС–Р· РіСЂР°РґС–С”РЅС‚Р° Р·Р±С–РіР°РІСЃСЏ Р· РєРѕРЅС‚РµРЅС‚РѕРј. */
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
    /** Р”РѕРґР°С‚РєРѕРІРёР№ Р·СЃРёРі СѓРЅРёР· РЅР° 10 px РµРєСЂР°РЅР° (С‰РѕРґРѕ РїРѕРїРµСЂРµРґРЅСЊРѕРіРѕ РїРѕР»РѕР¶РµРЅРЅСЏ). */
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

/** РљС–Р»СЊРєС–СЃС‚СЊ РєР°СЂС‚ Сѓ РєР°СЂСѓСЃРµР»С– (= ach/r1вЂ¦r5); С„РѕРЅ С–РЅС‚РµСЂРїРѕР»СЋС”С‚СЊСЃСЏ РјС–Р¶ СЃСѓСЃС–РґРЅС–РјРё РїР°Р»С–С‚СЂР°РјРё РїС–Рґ С‡Р°СЃ СЃРІР°Р№РїСѓ. */
internal const val HomeCardsCarouselPageCount = 4

/**
 * РќРѕСЂРјР°Р»С–Р·РѕРІР°РЅРёР№ Р·СЃСѓРІ СЃС‚РѕСЂС–РЅРєРё: 0 вЂ” СЃС‚РѕСЂС–РЅРєР° РІ С†РµРЅС‚СЂС–, +1 вЂ” РЅР° РµРєСЂР°РЅС– РїСЂР°РІРёР№ СЃСѓСЃС–Рґ, в€’1 вЂ” Р»С–РІРёР№.
 * Р„РґРёРЅРµ РґР¶РµСЂРµР»Рѕ РїСЂР°РІРґРё РґР»СЏ [HorizontalPager] + [graphicsLayer] (Р±Р°Р»Р°РЅСЃ, РєР°СЂС‚РєР°, С‡РёРї, РґС–С—, РѕРїРµСЂР°С†С–С—).
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
        // РќР° РїРµСЂС€С–Р№ РїРѕР»РѕРІРёРЅС– СЃРІР°Р№РїСѓ РІС…С–РґРЅР° (РґСЂСѓРіР°) СЃС‚РѕСЂС–РЅРєР° РјР°Р№Р¶Рµ РЅРµ РІРёС—Р¶РґР¶Р°С”:
        // РЅР° 50% С€Р»СЏС…Сѓ Р»РёС€Р°С”С‚СЊСЃСЏ ~20% С—С— РґРѕРґР°С‚РєРѕРІРѕРіРѕ СЂСѓС…Сѓ.
        0.02f + 0.08f * smoothStep(tHalf)
    } else {
        val tSecondHalf = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)
        // РџС–СЃР»СЏ СЃРµСЂРµРґРёРЅРё РЅР°Р·РґРѕРіР°РЅСЏС” РїР»Р°РІРЅРѕ, Р°Р»Рµ РїРѕРјС–С‚РЅРѕ Р°РєС‚РёРІРЅС–С€Рµ.
        val catchUp = smoothStep(tSecondHalf.pow(0.75f))
        0.20f + 0.80f * catchUp
    }
}

/**
 * Р›РµРіРєР° РїР»Р°СЃС‚РёРєР° РґР»СЏ СЃС‚РѕСЂС–РЅРєРё РєР°СЂС‚РєРё РІ РїРµР№РґР¶РµСЂС–:
 * Сѓ С†РµРЅС‚СЂС– (offset=0) вЂ” Р±РµР· С‚СЂР°РЅСЃС„РѕСЂРјР°С†С–С—, РЅР° СЃСѓСЃС–РґРЅС–С… РїРѕР·РёС†С–СЏС… (В±1) вЂ” Рј'СЏРєРµ СЃС‚РёСЃРєР°РЅРЅСЏ Р№ РЅР°С…РёР».
 * Snap/fling РєРµСЂСѓСЋС‚СЊСЃСЏ [PagerDefaults.flingBehavior] + spring; С†РµР№ С€Р°СЂ Р»РёС€Рµ РІС–Р·СѓР°Р»СЊРЅРёР№.
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
    scaleY = 1f
    // Р”РІРѕС„Р°Р·РЅРёР№ РїСЂРѕС„С–Р»СЊ: РґРѕ 50% СЃС‚РѕСЂС–РЅРєРё РµР»РµРјРµРЅС‚Рё СЂРѕР·С…РѕРґСЏС‚СЊСЃСЏ, РїС–СЃР»СЏ 50% вЂ” РїР»Р°РІРЅРѕ СЃС…РѕРґСЏС‚СЊСЃСЏ.
    val smoothStep: (Float) -> Float = { t ->
        val x = t.coerceIn(0f, 1f)
        x * x * (3f - 2f * x)
    }
    val stretchOutThenIn = if (progress <= 0.5f) {
        smoothStep(progress / 0.5f)
    } else {
        smoothStep((1f - progress) / 0.5f)
    }
    // РќРµРІРµР»РёРєРёР№ "С…РІС–СЃС‚" РІ РєС–РЅС†С– Р¶РµСЃС‚Сѓ: С‰РѕР± Р·Р±Р»РёР¶РµРЅРЅСЏ Р·Р°РІРµСЂС€СѓРІР°Р»РѕСЃСЊ Р±РµР· СЂС–Р·РєРѕРіРѕ СЃС‚РѕРїСѓ Р±С–Р»СЏ snap.
    val settleTail = (1f - smoothStep(progress)).pow(0.75f)
    // Р”РѕРІС€Рµ СѓС‚СЂРёРјСѓС” РІС…С–РґРЅСѓ СЃС‚РѕСЂС–РЅРєСѓ СЃР°РјРµ РЅР° СЃС‚Р°СЂС‚С– СЂСѓС…Сѓ.
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
    // PagerDefaults.flingBehavior вЂ” @Composable; РЅРµ РІРёРєР»РёРєР°С‚Рё РІСЃРµСЂРµРґРёРЅС– remember { }.
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
 * РџСЂРѕРіСЂР°РјРЅРёР№ РїРµСЂРµС…С–Рґ РјС–Р¶ СЃС‚РѕСЂС–РЅРєР°РјРё РєР°СЂС‚РѕРє Р· С‚С–С”СЋ Р¶ spring-СЃРїРµРєРѕСЋ, С‰Рѕ Р№ [PagerDefaults.flingBehavior].
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = HomeTopBarTopOffset),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(HomeTopProfileButtonSize)
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
                        modifier = Modifier.size(HomeTopProfileFallbackIconSize)
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
                /* РњР°СЃС€С‚Р°Р± РєС–Р» С– РїР»СЋСЃР°: в€’10% РІС–Рґ РїРѕРїРµСЂРµРґРЅСЊРѕРіРѕ (0.9 в†’ 0.81). */
                val circleScale = 0.81f
                val rOuter = size.minDimension * 0.5f * circleScale
                /* Р’РЅСѓС‚СЂС–С€РЅС–Р№ РґРёСЃРє: РїСЂРѕРїРѕСЂС†С–СЏ РґРѕ rOuter РјС–РЅСѓСЃ 3 px СЂР°РґС–СѓСЃР° Р·Р° РјР°РєРµС‚РѕРј. */
                val rInner = (rOuter * (22f / 40f) - 3f).coerceAtLeast(1f)
                /* Р—РѕРІРЅС–С€РЅС” РєС–Р»СЊС†Рµ: ~90% РїСЂРѕР·РѕСЂС–СЃС‚СЊ (Р°Р»СЊС„Р° 0.1); РІРЅСѓС‚СЂС–С€РЅС–Р№ РґРёСЃРє вЂ” #ffffff. */
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
                /* РџР»СЋСЃ Сѓ С‚РѕРјСѓ Р¶ РјР°СЃС€С‚Р°Р±С–, С‰Рѕ Р№ РєРѕР»Р°. */
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

    val rotX = 66f
    val rotY = 0f
    val rotZ = 0f
    val cardTransY = 10f
    val cameraFactor = 14f

    val numberColor = if (kind == HomeCarouselCardKind.WhiteBlackEdge) {
        Color(0xFF1E1E21)
    } else {
        Color(0xFFD4D6D9)
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
 * Р•РєСЂР°РЅ РєР°СЂС‚РєРё РїС–СЃР»СЏ РЅР°С‚РёСЃРєР°РЅРЅСЏ РЅР° РїР»Р°СЃС‚РёРЅСѓ: РєР°СЂС‚РєР° Р·РІРµСЂС…Сѓ (СЂРѕР·РіРѕСЂС‚Р°РЅРЅСЏ + РѕР±РµСЂС‚Р°РЅРЅСЏ), СЃРїРёСЃРѕРє-Р·Р°РіР»СѓС€РєР°.
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
    val receiptNumber: String,
    val receiptPdfUri: String?,
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
            dateLabel = "РЎСЊРѕРіРѕРґРЅС–",
            commissionAmount = null,
            receiptNumber = "CHEA-EKE4-3M3A-A37P",
            receiptPdfUri = null,
            logoAssetName = HomeOperationTransferAssetName
        ),
        HomeOperationUi(
            title = stringResource(R.string.home_op_card),
            amount = stringResource(R.string.home_op_card_amount),
            dateLabel = "Р’С‡РѕСЂР°",
            commissionAmount = null,
            receiptNumber = "CHEA-EKE4-3M3A-A37P",
            receiptPdfUri = null,
            logoAssetName = HomeOperationTransferAssetName,
            fallbackIcon = Icons.Filled.CreditCard
        ),
        HomeOperationUi(
            title = stringResource(R.string.home_op_mcd),
            amount = stringResource(R.string.home_op_mcd_amount),
            dateLabel = "14 РєРІС–С‚РЅСЏ 2026",
            commissionAmount = null,
            receiptNumber = "CHEA-EKE4-3M3A-A37P",
            receiptPdfUri = null,
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
                dateLabel = op.dateLabel.ifBlank { "Р‘РµР· РґР°С‚Рё" },
                commissionAmount = op.commissionAmount.takeIf { op.hasCommission && it.isNotBlank() },
                receiptNumber = op.receiptNumber.ifBlank { "CHEA-EKE4-3M3A-A37P" },
                receiptPdfUri = op.receiptPdfUri,
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
                        text = stringResource(R.string.home_operations_all) + " \u203A",
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
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        SystemBarsColorEffect(
            statusBarColor = Color(0xFF5D5ED6),
            navigationBarColor = Color(0xFF252525),
            decorBackgroundColor = Color(0xFF09090A)
        )
        val dialogView = LocalView.current
        DisposableEffect(dialogView) {
            val window = (dialogView.parent as? DialogWindowProvider)?.window
            val previousStatusBarColor = window?.statusBarColor
            val previousNavigationBarColor = window?.navigationBarColor
            window?.statusBarColor = Color(0xFF5D5ED6).toArgb()
            window?.navigationBarColor = Color(0xFF252525).toArgb()
            window?.setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.MATCH_PARENT
            )
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
                        text = "200.49 \u20B4",
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
    val transferBitmap = rememberCroppedAssetImageBitmap("$OperationsLogosPath/$HomeOperationTransferAssetName")
    val pencilBitmap = rememberAssetImageBitmap(HomeOperationDetailPencilAsset)
    val referBitmap = rememberAssetImageBitmap(HomeOperationDetailReferAsset)
    val tagsBitmap = rememberAssetImageBitmap(HomeOperationDetailTagsAsset)
    val walletBitmap = rememberCroppedAssetImageBitmap(HomeOperationDetailWalletAsset)
    val copyLinkBitmap = rememberAssetImageBitmap(HomeOperationDetailCopyLinkAsset)
    val linkBitmap = rememberCroppedAssetImageBitmap(HomeOperationDetailLinkAsset)
    val shareBitmap = rememberAssetImageBitmap(HomeOperationDetailShareAsset)
    val splitBitmap = rememberAssetImageBitmap(HomeOperationDetailSplitAsset)
    val repeatBitmap = rememberAssetImageBitmap(HomeOperationDetailRepeatAsset)
    val questionBitmap = rememberAssetImageBitmap(HomeOperationDetailQuestionAsset)
    val regularPaymentBitmap = rememberAssetImageBitmap(HomeOperationDetailRegularPaymentAsset)
    val saveCardBitmap = rememberAssetImageBitmap(HomeOperationDetailSaveCardAsset)
    val showPdfBitmap = rememberAssetImageBitmap(HomeOperationDetailShowPdfAsset)
    val commission = operation.commissionAmount?.takeIf { it.isNotBlank() }
    val summaryPanelHeight = if (commission != null) 232.dp else 220.dp
    var receiptOpen by remember(operation) { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        SystemBarsColorEffect(
            statusBarColor = Color(0xFF5D5ED6),
            navigationBarColor = Color(0xFF262626),
            decorBackgroundColor = Color(0xFF262626)
        )
        val dialogView = LocalView.current
        DisposableEffect(dialogView) {
            val window = (dialogView.parent as? DialogWindowProvider)?.window
            val previousStatusBarColor = window?.statusBarColor
            val previousNavigationBarColor = window?.navigationBarColor
            window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color(0xFF262626).toArgb()))
            window?.decorView?.setBackgroundColor(Color(0xFF262626).toArgb())
            window?.statusBarColor = Color(0xFF5D5ED6).toArgb()
            window?.navigationBarColor = Color(0xFF262626).toArgb()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                window?.isStatusBarContrastEnforced = false
                window?.isNavigationBarContrastEnforced = false
            }
            window?.setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.MATCH_PARENT
            )
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
                .background(Color(0xFF262626))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(112.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        color = Color(0xFF1D1D1D)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .height(summaryPanelHeight)
                                .background(Color(0xFF262626))
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 42.dp, bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = operation.title,
                                color = Color(0xFFE6E6E6),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            HomeOperationDetailTypePill(pencilBitmap)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = operation.dateLabel.ifBlank { "10 \u043A\u0432\u0456\u0442\u043D\u044F 2026, 14:27" },
                                color = Color(0xFF8D8D91),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = operation.amount,
                                color = Color(0xFFE9E9E9),
                                fontSize = 46.sp,
                                lineHeight = 50.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (commission != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\u041A\u043E\u043C\u0456\u0441\u0456\u044F $commission",
                                    color = Color(0xFF8B8B8F),
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            } else {
                                Spacer(modifier = Modifier.height(28.dp))
                            }
                            HomeOperationDetailInviteCard(referBitmap)
                            Spacer(modifier = Modifier.height(16.dp))
                            HomeOperationDetailPlaceholderRow(tagsBitmap)
                            Spacer(modifier = Modifier.height(16.dp))
                            HomeOperationDetailBalanceCard(walletBitmap)
                            Spacer(modifier = Modifier.height(18.dp))
                            HomeOperationDetailSpentChart()
                            Spacer(modifier = Modifier.height(16.dp))
                            HomeOperationDetailReceiptCard(linkBitmap, copyLinkBitmap, shareBitmap)
                        }
                    }
                    }
                }
                HomeOperationDetailActions(
                    splitBitmap = splitBitmap,
                    repeatBitmap = repeatBitmap,
                    saveCardBitmap = saveCardBitmap,
                    showPdfBitmap = showPdfBitmap,
                    regularPaymentBitmap = regularPaymentBitmap,
                    questionBitmap = questionBitmap,
                    onPdfReceiptClick = { receiptOpen = true }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .background(Color(0xFF5D5ED6))
            ) {
                HomeOperationDetailHeaderPattern(modifier = Modifier.fillMaxSize())
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(start = 2.dp, top = 22.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.jar_bank_back_cd),
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 102.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color(0xFF262626))
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 80.dp)
                    .size(64.dp),
                shape = CircleShape,
                color = Color(0xFF4738C7)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (transferBitmap != null) {
                        Image(
                            bitmap = transferBitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.CreditCard,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
    if (receiptOpen) {
        HomeOperationPdfReceiptScreen(
            operation = operation,
            onDismiss = { receiptOpen = false }
        )
    }
}

@Composable
private fun HomeOperationDetailHeaderPattern(modifier: Modifier = Modifier) {
    Box(modifier = modifier.clipToBounds()) {
        repeat(3) { row ->
            repeat(13) { column ->
                Image(
                    painter = painterResource(
                        if ((row + column) % 2 == 0) R.drawable.card_45 else R.drawable.billsvg
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(HomeOperationDetailPatternIconSize)
                        .offset(
                            x = (-18).dp + (HomeOperationDetailPatternStepX * column),
                            y = (-7).dp + (HomeOperationDetailPatternStepY * row)
                        )
                        .graphicsLayer(alpha = HomeOperationDetailPatternAlpha)
                )
            }
        }
    }
}

@Composable
private fun HomeOperationDetailTypePill(pencilBitmap: ImageBitmap?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.12f))
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF302D9E), Color(0xFF433FB7))
                    )
                )
                .padding(horizontal = 18.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "\u041F\u0435\u0440\u0435\u043A\u0430\u0437 \u043D\u0430 \u043A\u0430\u0440\u0442\u043A\u0443",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.width(7.dp))
                if (pencilBitmap != null) {
                    Image(
                        bitmap = pencilBitmap,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text("\u270E", color = Color.White, fontSize = 12.sp)
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.12f))
        )
    }
}

@Composable
private fun HomeOperationDetailInviteCard(referBitmap: ImageBitmap?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    1.4.dp,
                    Brush.horizontalGradient(listOf(Color(0xFF7D24F0), Color(0xFFFF2C9D)))
                ),
                RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF262626)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 17.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF7D31F2), Color(0xFFFF3A91)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (referBitmap != null) {
                    Image(
                        bitmap = referBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.SupportAgent,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(13.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "\u0417\u0430\u043F\u0440\u043E\u0441\u0438\u0442\u0438 \u043E\u0442\u0440\u0438\u043C\u0443\u0432\u0430\u0447\u0430 \u0434\u043E monobank",
                    color = Color(0xFFE6E6E6),
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "\u0422\u0430 \u043E\u0442\u0440\u0438\u043C\u0430\u0442\u0438 100 \u20B4 \u043D\u0430 \u0440\u0430\u0445\u0443\u043D\u043E\u043A \u043A\u0435\u0448\u0431\u0435\u043A\u0443",
                    color = Color(0xFF8B8B8F),
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HomeOperationDetailPlaceholderRow(tagsBitmap: ImageBitmap?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF262626)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (tagsBitmap != null) {
                Image(
                    bitmap = tagsBitmap,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = null,
                    tint = Color(0xFF77777B),
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = "\u041E\u043F\u0438\u0441 \u0442\u0430 #\u0442\u0435\u0433\u0438",
                color = Color(0xFF77777B),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun HomeOperationDetailBalanceCard(walletBitmap: ImageBitmap?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF262626)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .then(
                        if (walletBitmap == null) {
                            Modifier
                                .clip(CircleShape)
                                .background(Color(0xFF6558F3))
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (walletBitmap != null) {
                    Image(
                        bitmap = walletBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(18.dp))
            Column {
                Text(
                    text = "\u0417\u0430\u043B\u0438\u0448\u043E\u043A",
                    color = Color(0xFF8A8A8D),
                    fontSize = 16.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = "2 781.49 \u20B4",
                    color = Color(0xFFE9E9E9),
                    fontSize = 18.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeOperationDetailSpentChart() {
    val allMonthLabels = remember {
        listOf(
            "\u0441\u0456\u0447",
            "\u043b\u044e\u0442",
            "\u0431\u0435\u0440",
            "\u043a\u0432\u0456",
            "\u0442\u0440\u0430",
            "\u0447\u0435\u0440",
            "\u043b\u0438\u043f",
            "\u0441\u0435\u0440",
            "\u0432\u0435\u0440",
            "\u0436\u043e\u0432",
            "\u043b\u0438\u0441",
            "\u0433\u0440\u0443"
        )
    }
    val visibleMonthIndices = remember {
        val currentMonthIndex = LocalDate.now().monthValue - 1
        (5 downTo 0).map { offset ->
            (currentMonthIndex - offset + 12) % 12
        }
    }
    val visibleMonthLabels = remember(visibleMonthIndices) {
        visibleMonthIndices.map { allMonthLabels[it] }
    }
    var showSettings by remember { mutableStateOf(false) }
    var topLineInput by remember { mutableStateOf("200") }
    var middleLineInput by remember { mutableStateOf("100") }
    var monthInputs by remember {
        mutableStateOf(
            listOf("6", "5", "4", "180", "5", "85", "0", "0", "0", "0", "0", "0")
        )
    }
    val topLineRaw = topLineInput.toFloatOrNull()?.coerceAtLeast(1f) ?: 200f
    val middleLine = (middleLineInput.toFloatOrNull() ?: (topLineRaw / 2f)).coerceIn(0f, topLineRaw)
    val topLine = topLineRaw.coerceAtLeast(middleLine.coerceAtLeast(1f))
    val monthValues = monthInputs.map { it.toFloatOrNull()?.coerceAtLeast(0f) ?: 0f }
    val visibleMonthValues = visibleMonthIndices.map { monthValues[it] }
    val totalSpent = visibleMonthValues.sum().toInt()

    if (showSettings) {
        HomeOperationDetailChartSettingsDialog(
            monthLabels = visibleMonthLabels,
            topLineInput = topLineInput,
            middleLineInput = middleLineInput,
            monthInputs = visibleMonthIndices.map { monthInputs.getOrElse(it) { "" } },
            onTopLineChange = { topLineInput = it },
            onMiddleLineChange = { middleLineInput = it },
            onMonthChange = { index, value ->
                monthInputs = monthInputs.toMutableList().also { it[visibleMonthIndices[index]] = value }
            },
            onDismiss = { showSettings = false }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { },
                onLongClick = { showSettings = true }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.13f))
            )
            Text(
                text = "\u0412\u0438\u0442\u0440\u0430\u0442\u0438\u043b\u0438 $totalSpent \u20b4 \u0437\u0430 \u043f\u0456\u0432\u0440\u043e\u043a\u0443",
                color = Color(0xFF96969A),
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.16f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 3.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.13f))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF262626)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .height(142.dp)
            ) {
                val leftInset = 44.dp
                val chartWidth = maxWidth - leftInset - 6.dp
                val chartTopInset = 16.dp
                val chartBottomInset = 36.dp
                val chartHeightDp = 142.dp - chartTopInset - chartBottomInset
                val middleFractionForLabel = (middleLine / topLine).coerceIn(0f, 1f)
                val topLabelY = chartTopInset - 14.dp
                val middleLabelY = chartTopInset + chartHeightDp * (1f - middleFractionForLabel) - 14.dp
                val bottomLabelY = chartTopInset + chartHeightDp - 14.dp
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val axis = Color.White.copy(alpha = 0.2f)
                    val leftInsetPx = leftInset.toPx()
                    val bottomInset = 36.dp.toPx()
                    val topInset = 16.dp.toPx()
                    val chartWidthPx = size.width - leftInsetPx - 6.dp.toPx()
                    val chartHeight = size.height - bottomInset - topInset
                    val middleFraction = (middleLine / topLine).coerceIn(0f, 1f)
                    listOf(0f, middleFraction, 1f).forEach { fraction ->
                        val y = topInset + chartHeight * (1f - fraction)
                        drawLine(
                            color = axis,
                            start = Offset(leftInsetPx, y),
                            end = Offset(leftInsetPx + chartWidthPx, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                        )
                    }
                    val barWidth = 15.dp.toPx()
                    val zeroBarHeight = 3.dp.toPx()
                    val slotWidth = chartWidthPx / visibleMonthValues.size
                    visibleMonthValues.forEachIndexed { index, amount ->
                        val value = (amount / topLine).coerceIn(0f, 1f)
                        val centerX = leftInsetPx + slotWidth * (index + 0.5f)
                        val left = centerX - barWidth / 2f
                        val barHeight = if (amount <= 0f) zeroBarHeight else (chartHeight * value).coerceAtLeast(zeroBarHeight)
                        if (amount <= 0f) {
                            drawRoundRect(
                                color = Color(0xFFDEDEDE),
                                topLeft = Offset(left, topInset + chartHeight - barHeight),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                            )
                        } else {
                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    listOf(Color(0xFFD83BAC), Color(0xFF4A43CB)),
                                    startY = topInset + chartHeight - barHeight,
                                    endY = topInset + chartHeight
                                ),
                                topLeft = Offset(left, topInset + chartHeight - barHeight),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                            )
                        }
                    }
                }
                Text("${topLine.toInt()} \u20b4", color = Color(0xFFE1E1E1), fontSize = 12.sp, modifier = Modifier.offset(x = 0.dp, y = topLabelY))
                Text("${middleLine.toInt()} \u20b4", color = Color(0xFFE1E1E1), fontSize = 12.sp, modifier = Modifier.offset(x = 0.dp, y = middleLabelY))
                Text("0 \u20b4", color = Color(0xFFE1E1E1), fontSize = 12.sp, modifier = Modifier.offset(x = 10.dp, y = bottomLabelY))
                Row(
                    modifier = Modifier
                        .offset(x = leftInset, y = chartTopInset + chartHeightDp + 8.dp)
                        .requiredWidth(chartWidth),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    visibleMonthLabels.forEach { label ->
                        Text(
                            text = label,
                            color = Color(0xFF8B8B8F),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeOperationDetailChartSettingsDialog(
    monthLabels: List<String>,
    topLineInput: String,
    middleLineInput: String,
    monthInputs: List<String>,
    onTopLineChange: (String) -> Unit,
    onMiddleLineChange: (String) -> Unit,
    onMonthChange: (Int, String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF262626),
        title = {
            Text(
                text = "\u041d\u0430\u043b\u0430\u0448\u0442\u0443\u0432\u0430\u0442\u0438 \u0433\u0440\u0430\u0444\u0456\u043a",
                color = Color(0xFFE6E6E6),
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                HomeOperationDetailChartField(
                    value = middleLineInput,
                    onValueChange = onMiddleLineChange,
                    label = "\u0421\u0443\u043c\u0430 \u0434\u0440\u0443\u0433\u043e\u0457 \u043b\u0456\u043d\u0456\u0457"
                )
                Spacer(modifier = Modifier.height(8.dp))
                HomeOperationDetailChartField(
                    value = topLineInput,
                    onValueChange = onTopLineChange,
                    label = "\u0421\u0443\u043c\u0430 \u0442\u0440\u0435\u0442\u044c\u043e\u0457 \u043b\u0456\u043d\u0456\u0457"
                )
                Spacer(modifier = Modifier.height(12.dp))
                monthLabels.forEachIndexed { index, label ->
                    HomeOperationDetailChartField(
                        value = monthInputs.getOrElse(index) { "" },
                        onValueChange = { onMonthChange(index, it) },
                        label = "\u0421\u0443\u043c\u0430: $label"
                    )
                    if (index != monthLabels.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "OK", color = Color(0xFFE6E6E6))
            }
        }
    )
}

@Composable
private fun HomeOperationDetailChartField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { next ->
            onValueChange(next.filter { it.isDigit() || it == '.' || it == ',' }.replace(',', '.'))
        },
        label = { Text(text = label, color = Color(0xFFB8B8B8)) },
        singleLine = true,
        textStyle = TextStyle(color = Color(0xFFE6E6E6), fontSize = 15.sp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun HomeOperationDetailReceiptCard(
    linkBitmap: ImageBitmap?,
    copyLinkBitmap: ImageBitmap?,
    shareBitmap: ImageBitmap?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF262626)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .then(
                            if (linkBitmap == null) {
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFF4D77E9))
                            } else {
                                Modifier
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (linkBitmap != null) {
                        Image(
                            bitmap = linkBitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(Icons.AutoMirrored.Filled.ReceiptLong, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\u041F\u043E\u0441\u0438\u043B\u0430\u043D\u043D\u044F \u043D\u0430 \u043A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044E",
                        color = Color(0xFFE2E2E2),
                        fontSize = 17.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (copyLinkBitmap != null) {
                            Image(
                                bitmap = copyLinkBitmap,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = "check.monobank.ua/p/6gKR0bczqRK9Q...",
                            color = Color(0xFF8B8B8F),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF3A3A3A)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 11.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (shareBitmap != null) {
                        Image(
                            bitmap = shareBitmap,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            colorFilter = ColorFilter.tint(Color(0xFFE6E6E6)),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text("\u2318", color = Color(0xFFE6E6E6), fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "\u041F\u043E\u0434\u0456\u043B\u0438\u0442\u0438\u0441\u044F",
                        color = Color(0xFFE6E6E6),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeOperationPdfReceiptScreen(
    operation: HomeOperationUi,
    onDismiss: () -> Unit
) {
    val receiptNumber = operation.receiptNumber.ifBlank { "CHEA-EKE4-3M3A-A37P" }
    val context = LocalContext.current
    val pdfPage = rememberPdfFirstPageImageBitmap(operation.receiptPdfUri)
    val pdfUri = operation.receiptPdfUri?.takeIf { it.isNotBlank() }
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(HomeOperationReceiptLoaderAsset))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = composition != null
    )
    var showLoader by remember(operation) { mutableStateOf(true) }

    LaunchedEffect(operation) {
        showLoader = true
        delay(650)
        showLoader = false
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        SystemBarsColorEffect(
            statusBarColor = Color(0xFF262626),
            navigationBarColor = Color(0xFF1D1D1D),
            decorBackgroundColor = Color(0xFF1D1D1D)
        )
        val dialogView = LocalView.current
        DisposableEffect(dialogView) {
            val window = (dialogView.parent as? DialogWindowProvider)?.window
            val previousStatusBarColor = window?.statusBarColor
            val previousNavigationBarColor = window?.navigationBarColor
            window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(Color(0xFF1D1D1D).toArgb()))
            window?.decorView?.setBackgroundColor(Color(0xFF1D1D1D).toArgb())
            window?.statusBarColor = Color(0xFF262626).toArgb()
            window?.navigationBarColor = Color(0xFF1D1D1D).toArgb()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                window?.isStatusBarContrastEnforced = false
                window?.isNavigationBarContrastEnforced = false
            }
            window?.setLayout(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.MATCH_PARENT
            )
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
                .background(Color(0xFF1D1D1D))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF262626))
                        .statusBarsPadding()
                        .padding(bottom = 16.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .padding(start = 2.dp, top = 8.dp)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.jar_bank_back_cd),
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = "\u041A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044F",
                        color = Color(0xFFE4E4E4),
                        fontSize = 20.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )
                    Text(
                        text = "\u2116 $receiptNumber",
                        color = Color(0xFFC8C8C8),
                        fontSize = 14.sp,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp)
                        .padding(top = 16.dp)
                ) {
                    if (pdfPage != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.65f),
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White
                        ) {
                            Box(contentAlignment = Alignment.TopCenter) {
                                Image(
                                    bitmap = pdfPage,
                                    contentDescription = "\u041A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044F PDF",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 2.dp, top = 12.dp, end = 2.dp, bottom = 2.dp),
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.fillMaxWidth().height(540.dp))
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                        .height(52.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            shareOperationReceiptPdf(
                                context = context,
                                pdfUri = pdfUri,
                                receiptNumber = receiptNumber
                            )
                        },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFF4E55)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "\u041F\u043E\u0434\u0456\u043B\u0438\u0442\u0438\u0441\u044F",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (showLoader) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.58f)),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.size(66.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1D1D1D)
                    ) {
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            enableMergePaths = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberPdfFirstPageImageBitmap(pdfUri: String?): ImageBitmap? {
    val context = LocalContext.current
    var image by remember(pdfUri) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(pdfUri) {
        image = null
        if (pdfUri.isNullOrBlank()) return@LaunchedEffect
        image = withContext(Dispatchers.IO) {
            renderPdfFirstPage(context, pdfUri)?.asImageBitmap()
        }
    }
    return image
}

private fun renderPdfFirstPage(context: android.content.Context, pdfUri: String): Bitmap? {
    return runCatching {
        val uri = Uri.parse(pdfUri)
        context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
            PdfRenderer(descriptor).use { renderer ->
                if (renderer.pageCount <= 0) {
                    null
                } else {
                    renderer.openPage(0).use { page ->
                        val scale = 2
                        val bitmap = Bitmap.createBitmap(
                            page.width * scale,
                            page.height * scale,
                            Bitmap.Config.ARGB_8888
                        )
                        bitmap.eraseColor(android.graphics.Color.WHITE)
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        bitmap
                    }
                }
            }
        }
    }.getOrNull()
}

private fun shareOperationReceiptPdf(
    context: android.content.Context,
    pdfUri: String?,
    receiptNumber: String
) {
    if (pdfUri.isNullOrBlank()) return
    val uri = Uri.parse(pdfUri)
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "\u041A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044F \u2116 $receiptNumber")
        clipData = ClipData.newUri(context.contentResolver, "\u041A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044F \u2116 $receiptNumber", uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(
        Intent.createChooser(sendIntent, "\u041F\u043E\u0434\u0456\u043B\u0438\u0442\u0438\u0441\u044F")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    )
}

@Composable
private fun HomeOperationDetailActions(
    splitBitmap: ImageBitmap?,
    repeatBitmap: ImageBitmap?,
    saveCardBitmap: ImageBitmap?,
    showPdfBitmap: ImageBitmap?,
    regularPaymentBitmap: ImageBitmap?,
    questionBitmap: ImageBitmap?,
    onPdfReceiptClick: () -> Unit
) {
    val rows = listOf(
        "\u0420\u043E\u0437\u0434\u0456\u043B\u0438\u0442\u0438 \u0432\u0438\u0442\u0440\u0430\u0442\u0443" to Icons.Filled.Layers,
        "\u041F\u043E\u0432\u0442\u043E\u0440\u0438\u0442\u0438 \u043F\u043B\u0430\u0442\u0456\u0436" to Icons.Filled.CreditCard,
        "\u0417\u0431\u0435\u0440\u0435\u0433\u0442\u0438 \u043A\u0430\u0440\u0442\u043A\u0443" to Icons.Filled.CreditCard,
        "\u041F\u0435\u0440\u0435\u0433\u043B\u044F\u043D\u0443\u0442\u0438 PDF-\u043A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044E" to Icons.AutoMirrored.Filled.ReceiptLong,
        "\u0417\u0440\u043E\u0431\u0438\u0442\u0438 \u0440\u0435\u0433\u0443\u043B\u044F\u0440\u043D\u0438\u043C" to Icons.Outlined.Settings,
        "\u041F\u043E\u0441\u0442\u0430\u0432\u0438\u0442\u0438 \u0437\u0430\u043F\u0438\u0442\u0430\u043D\u043D\u044F" to Icons.Filled.QuestionMark
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF262626))
            .padding(horizontal = 16.dp)
    ) {
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (index == 3) onPdfReceiptClick()
                    }
                    .padding(vertical = 17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val actionBitmap = when (index) {
                    0 -> splitBitmap
                    1 -> repeatBitmap
                    2 -> saveCardBitmap
                    3 -> showPdfBitmap
                    4 -> regularPaymentBitmap
                    5 -> questionBitmap
                    else -> null
                }
                if (actionBitmap != null) {
                    Image(
                        bitmap = actionBitmap,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp)
                    )
                } else {
                    Icon(
                        imageVector = row.second,
                        contentDescription = null,
                        tint = Color(0xFFE4E4E4),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = row.first,
                    color = Color(0xFFE4E4E4),
                    fontSize = 18.sp
                )
            }
            if (index != rows.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(start = 56.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.10f))
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
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

/** В«РњР°СЂРєРµС‚В»: РѕРґРёРЅ СЃС‚Р°С‚РёС‡РЅРёР№ РєР°РґСЂ; РїСЂРё Р·РјС–РЅС– РІРєР»Р°РґРєРё вЂ” СЃРєРёРґР°РЅ РЅР° РїРѕС‡Р°С‚РѕРє + СЃС–СЂРёР№ С‚С–РЅС‚, СЏРє Сѓ В«РљР°СЂС‚РєРёВ». */
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

/** PNG `gift-box_negate` Р±С–Р»СЏ СЃСѓРјРё РєРµС€Р±РµРєСѓ; СЏРєС‰Рѕ С„Р°Р№Р»Сѓ РЅРµРјР°С” вЂ” [Icons.Outlined.CardGiftcard]. */
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
