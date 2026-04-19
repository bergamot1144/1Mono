package com.konvert.app.ui.home

import kotlin.math.hypot
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// =============================================================================
// Картка на головному екрані (HomeMonoTiltedCard + HomeMonoCardFaceLayers)
// Редагуй блоки нижче — усі кольори / розміри зібрані тут.
// =============================================================================

/** Ширина й висота пластини в слоті каруселі (ширше — сильніший ефект «трапеції» при наклоні). */
private object HomeBankCardFrame {
    /** Базова ширина; у [HomeMonoTiltedCard] віднімається [HomeBankCardWidthNarrowPxTotal] px. */
    val width: Dp = 320.dp
    /** Висота пластини в [HomeMonoTiltedCard] (нижче — компактніший слот каруселі). */
    val height: Dp = 240.dp
}

/** Сума віднімання від базової ширини в px (перерахунок у dp у [HomeMonoTiltedCard]). */
private const val HomeBankCardWidthNarrowPxTotal: Float = 20f

/** Відступи контенту (логотип, номер, Visa) поверх Canvas. */
private object HomeBankCardContentPadding {
    val horizontal: Dp = 15.dp
    val vertical: Dp = 18.dp
}

/** Розміри логотипів (assets). */
private object HomeBankCardLogos {
    val monobankWidth: Dp = 150.dp
    val monobankHeight: Dp = 35.dp
    val visaWidth: Dp = 60.dp
    val visaHeight: Dp = 25.dp
    /** Відступ логотипу Visa вгору від нижнього краю лиця картки. */
    val visaBottomLift: Dp = 15.dp
    /** Зсув Visa вліво від `BottomEnd` (px → dp у [HomeMonoCardFaceLayers]). */
    const val visaOffsetLeftPx: Float = 10f
}

/**
 * Геометрія та фарби Canvas (лице + товщина).
 * Порядок шарів знизу вгору — як у коді [drawHomeBankCardPlastic].
 *
 * **Синя «димка» на пластику (лице):** [hazeBlueOuter], [hazeBlueMid] — інтенсивність;
 * [hazeBlueCenterYFraction] (0..1 по висоті лиця) — вертикаль центра радіалу;
 * [hazeBlueRadiusFactor] — радіус як частка ширини картки.
 *
 * **Туман поверх верху картки** (накладається в [HomeMonoTiltedCard] поверх усього лиця): [cardFogTopColor],
 * [cardFogMidColor], [cardFogGradientHeightFraction].
 */
private object HomeBankCardPlastic {
    val cornerRadius: Dp = 20.dp
    val frontThickness: Dp = 8.dp

    // --- Об'єм за лицем (видима смуга товщини / ребро при rotationX): #0d0d0d ---
    val volumeBackTop: Color = Color(0xFF0D0D0D)
    val volumeBackBottom: Color = Color(0xFF0D0D0D)

    // --- Основне лице: верх і низ як раніше; по середині висоти — плавно світліше (один стоп, без смуги) ---
    val faceColorStart: Color = Color(0xFF0B101A)
    /** Світліший за колишній #171D2D — лише точка 0.5 на вертикальному градієнті. */
    val faceColorCenter: Color = Color(0xFF202020)
    /** Низ лиця біля ребра картки. */
    val faceColorEnd: Color = Color(0xFF292929)

    // --- Тонка смужка під лицем (відбиття фону) ---
    val stripWhite: Color = Color.White.copy(alpha = 0.035f)
    val stripBlue: Color = Color(0xFF8FB2FF).copy(alpha = 0.08f)

    // --- Радіальне синє «світіння» (центр біля верхнього краю лиця, як на референсі) ---
    val hazeBlueOuter: Color = Color(0xFF355FAF).copy(alpha = 0.10f)
    val hazeBlueMid: Color = Color(0xFF223C6B).copy(alpha = 0.055f)
    const val hazeBlueCenterYFraction: Float = 0.12f
    const val hazeBlueRadiusFactor: Float = 0.56f

    // --- Туман поверх верху пластини (linear поверх контенту в [HomeMonoTiltedCard]; трохи затемнює monobank) ---
    val cardFogTopColor: Color = Color(0xFF102052).copy(alpha = 0.38f)
    val cardFogMidColor: Color = Color(0xFF102052).copy(alpha = 0.14f)
    /** Висота зони градієнта туману як частка висоти пластини. */
    const val cardFogGradientHeightFraction: Float = 0.56f

    /** Теплий бронзовий відтінок — накладається радіально від центру лиця. */
    val bronze: Color = Color(0xFF27262E).copy(alpha = 0.16f)
    /** Центр радіального бронзового градієнта (0..1 по ширині та висоті лиця). */
    const val bronzeRadialCenterXFraction: Float = 0.50f
    const val bronzeRadialCenterYFraction: Float = 0.48f
    /** Радіус як частка від hypot(ширина, висота лиця) — покриває кути з легким запасом. */
    const val bronzeRadialRadiusFactor: Float = 0.58f

    // --- Затемнення справа ---
    val shadeBlack: Color = Color.Black.copy(alpha = 0.05f)
}

/**
 * Кольори пластику (лице + ребро при нахилі), окремо від геометрії [HomeBankCardPlastic].
 * Порядок у каруселі: [HomeCarouselCardKind] ordinal 0…3.
 */
internal data class HomeBankCardFaceColors(
    val volumeBackTop: Color,
    val volumeBackBottom: Color,
    val faceColorStart: Color,
    val faceColorCenter: Color,
    val faceColorEnd: Color,
    val stripWhite: Color,
    val stripBlue: Color,
    val hazeBlueOuter: Color,
    val hazeBlueMid: Color,
    val bronze: Color,
    val shadeBlack: Color,
    val cardFogTopColor: Color,
    val cardFogMidColor: Color,
    val fogTopLineColor: Color
)

internal val HomeBankCardFaceColorsBlack = HomeBankCardFaceColors(
    volumeBackTop = HomeBankCardPlastic.volumeBackTop,
    volumeBackBottom = HomeBankCardPlastic.volumeBackBottom,
    faceColorStart = HomeBankCardPlastic.faceColorStart,
    faceColorCenter = HomeBankCardPlastic.faceColorCenter,
    faceColorEnd = HomeBankCardPlastic.faceColorEnd,
    stripWhite = HomeBankCardPlastic.stripWhite,
    stripBlue = HomeBankCardPlastic.stripBlue,
    hazeBlueOuter = HomeBankCardPlastic.hazeBlueOuter,
    hazeBlueMid = HomeBankCardPlastic.hazeBlueMid,
    bronze = HomeBankCardPlastic.bronze,
    shadeBlack = HomeBankCardPlastic.shadeBlack,
    cardFogTopColor = HomeBankCardPlastic.cardFogTopColor,
    cardFogMidColor = HomeBankCardPlastic.cardFogMidColor,
    fogTopLineColor = Color(0xFF102052)
)

/** Біле лице, товщина / ребро — чорні. */
private val HomeBankCardFaceColorsWhiteBlackEdge = HomeBankCardFaceColors(
    volumeBackTop = Color(0xFF0A0A0A),
    volumeBackBottom = Color(0xFF050505),
    faceColorStart = Color(0xFFF8F8FA),
    faceColorCenter = Color(0xFFECECF1),
    faceColorEnd = Color(0xFFE0E0E8),
    stripWhite = Color.Black.copy(alpha = 0.045f),
    stripBlue = Color(0xFF5C6B8A).copy(alpha = 0.07f),
    hazeBlueOuter = Color(0xFF7A8CAD).copy(alpha = 0.055f),
    hazeBlueMid = Color(0xFF9DABC4).copy(alpha = 0.028f),
    bronze = Color(0xFF7A7568).copy(alpha = 0.09f),
    shadeBlack = Color.Black.copy(alpha = 0.07f),
    cardFogTopColor = Color(0xFF505050).copy(alpha = 0.09f),
    cardFogMidColor = Color(0xFF707070).copy(alpha = 0.035f),
    fogTopLineColor = Color(0xFFD8D8E0)
)

/** Як чорна базова, ребро — зелене (доларова). */
private val HomeBankCardFaceColorsBlackGreenEdge = HomeBankCardFaceColorsBlack.copy(
    volumeBackTop = Color(0xFF0F6A42),
    volumeBackBottom = Color(0xFF083822)
)

/** Як чорна базова, ребро — червоне (єврова). */
private val HomeBankCardFaceColorsBlackRedEdge = HomeBankCardFaceColorsBlack.copy(
    volumeBackTop = Color(0xFF7A1E26),
    volumeBackBottom = Color(0xFF3D0E12)
)

/**
 * Вид картки в каруселі головного екрану (строго цей порядок: ordinal = сторінка pager).
 * 1 — чорна, 2 — біла з чорним ребром, 3 — чорна зелене ребро (USD), 4 — чорна червоне ребро (EUR).
 */
internal enum class HomeCarouselCardKind {
    Black,
    WhiteBlackEdge,
    BlackGreenEdgeUsd,
    BlackRedEdgeEur
}

internal fun HomeCarouselCardKind.faceColors(): HomeBankCardFaceColors = when (this) {
    HomeCarouselCardKind.Black -> HomeBankCardFaceColorsBlack
    HomeCarouselCardKind.WhiteBlackEdge -> HomeBankCardFaceColorsWhiteBlackEdge
    HomeCarouselCardKind.BlackGreenEdgeUsd -> HomeBankCardFaceColorsBlackGreenEdge
    HomeCarouselCardKind.BlackRedEdgeEur -> HomeBankCardFaceColorsBlackRedEdge
}

internal fun HomeCarouselCardKind.negateLogoColorFilter(): ColorFilter? =
    if (this == HomeCarouselCardKind.WhiteBlackEdge) ColorFilter.tint(Color(0xFF141414)) else null

// =============================================================================
// Опційний прев'ю-картка без тексту / логотипів (для макетів і експериментів)
// =============================================================================

/** Розміри та фарби для [PremiumFloatingBankCard] — окремий вигляд «чистої» пластини. */
private object PremiumBankCardPreviewStyle {
    val cardHeight: Dp = 168.dp
    val cornerRadius: Dp = 26.dp
    val frontThickness: Dp = 7.dp
    val cameraFactor: Float = 34f
    val translationY: Float = 6f
    val rotationXDefault: Float = 58f

    val volumeDarkTop: Color = Color(0xFF101012)
    val volumeDarkBottom: Color = Color(0xFF050506)
    val thicknessTop: Color = Color(0xFF141418)
    val thicknessBottom: Color = Color(0xFF080809)
    val face1: Color = Color(0xFF1C1F24)
    val face2: Color = Color(0xFF121418)
    val face3: Color = Color(0xFF0C0D10)
    val blueHaze1: Color = Color(0xFF4A6FA8).copy(alpha = 0.09f)
    val blueHaze2: Color = Color(0xFF2E4A78).copy(alpha = 0.04f)
    val bronze: Color = Color(0xFF6B5230).copy(alpha = 0.055f)
    val edgeLine: Color = Color.White.copy(alpha = 0.065f)
}

/**
 * Зовнішній контейнер картки на Home: наклонена пластина + клік.
 * Параметри обертання задає [com.konvert.app.ui.home.HomeCardPlaceholder].
 */
@Composable
internal fun HomeMonoTiltedCard(
    monobankLogo: ImageBitmap?,
    visaLogo: ImageBitmap?,
    number: String,
    numberStyle: TextStyle,
    visaFallbackText: String,
    visaFallbackStyle: TextStyle,
    monobankContentDescription: String?,
    visaContentDescription: String,
    rotationXDegrees: Float,
    rotationYDegrees: Float,
    rotationZDegrees: Float,
    cameraDistanceFactor: Float,
    translationY: Float,
    /** 0 — складена, 1 — розгорнута: ребро зникає (див. [HomeMonoCardFaceLayers]). */
    cardUnfoldProgress: Float = 0f,
    faceColors: HomeBankCardFaceColors = HomeBankCardFaceColorsBlack,
    logoColorFilter: ColorFilter? = null,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    val plateWidth = HomeBankCardFrame.width - (HomeBankCardWidthNarrowPxTotal / density).dp
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(plateWidth)
                .height(HomeBankCardFrame.height)
                .graphicsLayer {
                    rotationX = rotationXDegrees
                    rotationY = rotationYDegrees
                    rotationZ = rotationZDegrees
                    cameraDistance = cameraDistanceFactor * density
                    transformOrigin = TransformOrigin.Center
                    this.translationY = translationY
                }
                .drawWithContent {
                    drawContent()
                    val P = HomeBankCardPlastic
                    val fogH = size.height * P.cardFogGradientHeightFraction
                    val linePx = 1.dp.toPx()
                    drawRect(
                        color = faceColors.fogTopLineColor,
                        topLeft = Offset.Zero,
                        size = Size(size.width, linePx)
                    )
                    val fogBodyH = (fogH - linePx).coerceAtLeast(0f)
                    if (fogBodyH > 0f) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0f to faceColors.cardFogTopColor,
                                    0.5f to faceColors.cardFogMidColor,
                                    1f to Color.Transparent
                                ),
                                startY = linePx,
                                endY = fogH
                            ),
                            topLeft = Offset(0f, linePx),
                            size = Size(size.width, fogBodyH)
                        )
                    }
                }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onCardClick
                )
        ) {
            HomeMonoCardFaceLayers(
                monobankLogo = monobankLogo,
                visaLogo = visaLogo,
                number = number,
                numberStyle = numberStyle,
                visaFallbackText = visaFallbackText,
                visaFallbackStyle = visaFallbackStyle,
                monobankContentDescription = monobankContentDescription,
                visaContentDescription = visaContentDescription,
                cardUnfoldProgress = cardUnfoldProgress,
                faceColors = faceColors,
                logoColorFilter = logoColorFilter,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Лице картки: Canvas (пластик) + шар з логотипами та номером.
 */
@Composable
internal fun HomeMonoCardFaceLayers(
    monobankLogo: ImageBitmap?,
    visaLogo: ImageBitmap?,
    number: String,
    numberStyle: TextStyle,
    visaFallbackText: String,
    visaFallbackStyle: TextStyle,
    monobankContentDescription: String?,
    visaContentDescription: String,
    /** 0 — складена, 1 — розгорнута: нижнє ребро зникає (товщина → 0). */
    cardUnfoldProgress: Float = 0f,
    faceColors: HomeBankCardFaceColors = HomeBankCardFaceColorsBlack,
    logoColorFilter: ColorFilter? = null,
    modifier: Modifier = Modifier
) {
    val fc = faceColors
    val P = HomeBankCardPlastic
    val unfold = cardUnfoldProgress.coerceIn(0f, 1f)
    Box(modifier = modifier) {
        Canvas(Modifier.fillMaxSize()) {
            val cardWidth = size.width
            val cardHeight = size.height
            val corner = P.cornerRadius.toPx()
            val thicknessFull = P.frontThickness.toPx()
            val effectiveThickness = thicknessFull * (1f - unfold)
            val faceHeight = cardHeight - effectiveThickness

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        fc.volumeBackTop,
                        fc.volumeBackBottom
                    )
                ),
                topLeft = Offset(0f, effectiveThickness),
                size = Size(cardWidth, cardHeight - effectiveThickness),
                cornerRadius = CornerRadius(corner, corner)
            )

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to fc.faceColorStart,
                        0.5f to fc.faceColorCenter,
                        1f to fc.faceColorEnd
                    ),
                    startY = 0f,
                    endY = faceHeight
                ),
                topLeft = Offset(0f, 0f),
                size = Size(cardWidth, faceHeight),
                cornerRadius = CornerRadius(corner, corner)
            )

            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(fc.stripWhite, fc.stripBlue, fc.stripWhite)
                ),
                topLeft = Offset(10.dp.toPx(), faceHeight - 4.dp.toPx()),
                size = Size(cardWidth - 20.dp.toPx(), 5.dp.toPx())
            )
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(fc.hazeBlueOuter, fc.hazeBlueMid, Color.Transparent),
                    center = Offset(cardWidth * 0.50f, faceHeight * P.hazeBlueCenterYFraction),
                    radius = cardWidth * P.hazeBlueRadiusFactor
                ),
                topLeft = Offset(0f, 0f),
                size = Size(cardWidth, faceHeight),
                cornerRadius = CornerRadius(corner, corner)
            )

            val bronzeR = hypot(cardWidth.toDouble(), faceHeight.toDouble())
                .toFloat() * P.bronzeRadialRadiusFactor
            drawRoundRect(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.32f to Color.Transparent,
                        0.52f to fc.bronze.copy(alpha = fc.bronze.alpha * 0.28f),
                        0.76f to fc.bronze.copy(alpha = fc.bronze.alpha * 0.62f),
                        1f to fc.bronze
                    ),
                    center = Offset(
                        cardWidth * P.bronzeRadialCenterXFraction,
                        faceHeight * P.bronzeRadialCenterYFraction
                    ),
                    radius = bronzeR
                ),
                topLeft = Offset(0f, 0f),
                size = Size(cardWidth, faceHeight),
                cornerRadius = CornerRadius(corner, corner)
            )

            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, fc.shadeBlack),
                    start = Offset(cardWidth * 0.58f, faceHeight * 0.30f),
                    end = Offset(cardWidth, faceHeight)
                ),
                topLeft = Offset(0f, 0f),
                size = Size(cardWidth, faceHeight),
                cornerRadius = CornerRadius(corner, corner)
            )
        }

        val density = LocalDensity.current
        val C = HomeBankCardContentPadding
        val L = HomeBankCardLogos
        val visaShiftLeft = (-L.visaOffsetLeftPx / density.density).dp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = C.horizontal, vertical = C.vertical)
        ) {
            val numberGroups = remember(number) {
                number.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
            }

            if (monobankLogo != null) {
                Image(
                    bitmap = monobankLogo,
                    contentDescription = monobankContentDescription,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-12).dp)
                        .width(L.monobankWidth)
                        .height(L.monobankHeight),
                    contentScale = ContentScale.Fit,
                    colorFilter = logoColorFilter
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 6.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    numberGroups.isEmpty() -> {
                        Text(
                            text = number,
                            style = numberStyle,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }

                    numberGroups.size == 1 -> {
                        Text(
                            text = numberGroups.first(),
                            style = numberStyle,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            numberGroups.forEach { group ->
                                Text(
                                    text = group,
                                    style = numberStyle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }
                    }
                }
            }

            if (visaLogo != null) {
                Image(
                    bitmap = visaLogo,
                    contentDescription = visaContentDescription,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = visaShiftLeft, y = -L.visaBottomLift)
                        .width(L.visaWidth)
                        .height(L.visaHeight),
                    contentScale = ContentScale.Fit,
                    colorFilter = logoColorFilter
                )
            } else {
                Text(
                    text = visaFallbackText,
                    style = visaFallbackStyle,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = visaShiftLeft, y = -L.visaBottomLift)
                )
            }
        }
    }
}


// --- Прев'ю: картка без контенту (не використовується на Home за замовчуванням) ---

@Composable
fun PremiumFloatingBankCard(
    modifier: Modifier = Modifier,
    rotationXDegrees: Float = PremiumBankCardPreviewStyle.rotationXDefault,
    cardHeight: Dp = PremiumBankCardPreviewStyle.cardHeight,
    cornerRadius: Dp = PremiumBankCardPreviewStyle.cornerRadius,
    frontThickness: Dp = PremiumBankCardPreviewStyle.frontThickness,
    cameraDistanceFactor: Float = PremiumBankCardPreviewStyle.cameraFactor,
    translationY: Float = PremiumBankCardPreviewStyle.translationY
) {
    val S = PremiumBankCardPreviewStyle
    val density = LocalDensity.current.density

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .height(cardHeight)
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationX = rotationXDegrees
                    rotationY = 0f
                    rotationZ = 0f
                    cameraDistance = cameraDistanceFactor * density
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                    this.translationY = translationY
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val cornerPx = cornerRadius.toPx()
                val thickPx = frontThickness.toPx()
                val faceH = h - thickPx

                drawRoundRect(
                    brush = Brush.verticalGradient(colors = listOf(S.volumeDarkTop, S.volumeDarkBottom)),
                    topLeft = Offset(0f, thickPx),
                    size = Size(w, h - thickPx),
                    cornerRadius = CornerRadius(cornerPx, cornerPx)
                )

                drawRoundRect(
                    brush = Brush.verticalGradient(colors = listOf(S.thicknessTop, S.thicknessBottom)),
                    topLeft = Offset(0f, h - thickPx),
                    size = Size(w, thickPx),
                    cornerRadius = CornerRadius(cornerPx, cornerPx)
                )

                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(S.face1, S.face2, S.face3),
                        start = Offset(0f, 0f),
                        end = Offset(w, faceH)
                    ),
                    topLeft = Offset(0f, 0f),
                    size = Size(w, faceH),
                    cornerRadius = CornerRadius(cornerPx, cornerPx)
                )

                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(S.blueHaze1, S.blueHaze2, Color.Transparent),
                        center = Offset(w * 0.5f, faceH * 0.48f),
                        radius = w * 0.38f
                    ),
                    topLeft = Offset(0f, 0f),
                    size = Size(w, faceH),
                    cornerRadius = CornerRadius(cornerPx, cornerPx)
                )

                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(S.bronze, Color.Transparent),
                        center = Offset(w * 0.18f, faceH * 0.82f),
                        radius = w * 0.35f
                    ),
                    topLeft = Offset(0f, 0f),
                    size = Size(w, faceH),
                    cornerRadius = CornerRadius(cornerPx, cornerPx)
                )

                drawLine(
                    color = S.edgeLine,
                    start = Offset(10.dp.toPx(), faceH),
                    end = Offset(w - 10.dp.toPx(), faceH),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

@Composable
fun PremiumFloatingBankCardSlot(
    modifier: Modifier = Modifier,
    rotationXDegrees: Float = PremiumBankCardPreviewStyle.rotationXDefault
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        contentAlignment = Alignment.Center
    ) {
        PremiumFloatingBankCard(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            rotationXDegrees = rotationXDegrees
        )
    }
}
