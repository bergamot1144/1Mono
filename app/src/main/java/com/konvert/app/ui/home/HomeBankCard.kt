package com.konvert.app.ui.home

import kotlin.math.hypot
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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
    val width: Dp = 372.dp
    val height: Dp = 260.dp
}

/** Відступи контенту (логотип, номер, Visa) поверх Canvas. */
private object HomeBankCardContentPadding {
    val horizontal: Dp = 15.dp
    val vertical: Dp = 18.dp
    /**
     * Відстань між групами по 4 символи в номері на картці.
     * Рядок `number` у [HomeMonoCardFaceLayers] ділиться по пробілах (див. `R.string.home_card_masked_number`).
     */
    val numberInterGroupSpacing: Dp = 20.dp
    /** Частка ширини під масштаб номера (0..1). */
    const val numberTargetWidthFraction: Float = 0.95f
    const val numberScaleMin: Float = 0.9f
    const val numberScaleMax: Float = 1.1f
}

/** Розміри логотипів (assets). */
private object HomeBankCardLogos {
    val monobankWidth: Dp = 150.dp
    val monobankHeight: Dp = 35.dp
    val visaWidth: Dp = 60.dp
    val visaHeight: Dp = 25.dp
    /** Відступ логотипу Visa вгору від нижнього краю лиця картки. */
    val visaBottomLift: Dp = 15.dp
}

/**
 * Геометрія та фарби Canvas (лице + товщина).
 * Порядок шарів знизу вгору — як у коді [drawHomeBankCardPlastic].
 */
private object HomeBankCardPlastic {
    val cornerRadius: Dp = 20.dp
    val frontThickness: Dp = 8.dp

    // --- Об'єм за лицем (видиме ребро при rotationX): єдиний колір #0f1113 ---
    val volumeBackTop: Color = Color(0xFF0F1113)
    val volumeBackBottom: Color = Color(0xFF0F1113)

    // --- Основне лице: верх і низ як раніше; по середині висоти — плавно світліше (один стоп, без смуги) ---
    val faceColorStart: Color = Color(0xFF121A2C)
    /** Світліший за колишній #171D2D — лише точка 0.5 на вертикальному градієнті. */
    val faceColorCenter: Color = Color(0xFF1E2838)
    val faceColorEnd: Color = Color(0xFF1F222A)

    // --- Тонка смужка під лицем (відбиття фону) ---
    val stripWhite: Color = Color.White.copy(alpha = 0.035f)
    val stripBlue: Color = Color(0xFF8FB2FF).copy(alpha = 0.08f)

    // --- Радіальне синє «світіння» по центру ---
    val hazeBlueOuter: Color = Color(0xFF355FAF).copy(alpha = 0.10f)
    val hazeBlueMid: Color = Color(0xFF223C6B).copy(alpha = 0.055f)

    // --- Нижній край лиця (холодний підсвіт) ---
    val bottomGlowWhite: Color = Color.White.copy(alpha = 0.032f)
    val bottomGlowBlue: Color = Color(0xFFC4D6FF).copy(alpha = 0.05f)
    val bottomGlowStartYFraction: Float = 0.70f

    /** Теплий бронзовий відтінок — накладається радіально від центру лиця. */
    val bronze: Color = Color(0xFF27262E).copy(alpha = 0.16f)
    /** Центр радіального бронзового градієнта (0..1 по ширині та висоті лиця). */
    const val bronzeRadialCenterXFraction: Float = 0.50f
    const val bronzeRadialCenterYFraction: Float = 0.48f
    /** Радіус як частка від hypot(ширина, висота лиця) — покриває кути з легким запасом. */
    const val bronzeRadialRadiusFactor: Float = 0.58f

    // --- Затемнення справа ---
    val shadeBlack: Color = Color.Black.copy(alpha = 0.05f)

    // --- Підсвіт на стику лице / ребро: пік лівіше центру, до нуля з обох боків по ширині картки ---
    val creaseHighlight: Color = Color.White.copy(alpha = 0.045f)
    val creaseInset: Dp = 14.dp
    val creaseStroke: Dp = 1.dp
    /** Висота смуги підсвітки (px у Canvas). */
    val creaseHighlightBand: Dp = 2.dp
    /** Позиція піка по X (0 — лівий край картки, 1 — правий), <0.5 — лівіше центру. */
    const val creaseHighlightPeakXFraction: Float = 0.50f
}

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
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(HomeBankCardFrame.width)
                .height(HomeBankCardFrame.height)
                .graphicsLayer {
                    rotationX = rotationXDegrees
                    rotationY = rotationYDegrees
                    rotationZ = rotationZDegrees
                    cameraDistance = cameraDistanceFactor * density
                    transformOrigin = TransformOrigin.Center
                    this.translationY = translationY
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
    modifier: Modifier = Modifier
) {
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
                brush = Brush.verticalGradient(colors = listOf(P.volumeBackTop, P.volumeBackBottom)),
                topLeft = Offset(0f, effectiveThickness),
                size = Size(cardWidth, cardHeight - effectiveThickness),
                cornerRadius = CornerRadius(corner, corner)
            )

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to P.faceColorStart,
                        0.5f to P.faceColorCenter,
                        1f to P.faceColorEnd
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
                    colors = listOf(P.stripWhite, P.stripBlue, P.stripWhite)
                ),
                topLeft = Offset(10.dp.toPx(), faceHeight - 4.dp.toPx()),
                size = Size(cardWidth - 20.dp.toPx(), 5.dp.toPx())
            )
//позиция синей дымки
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(P.hazeBlueOuter, P.hazeBlueMid, Color.Transparent),
                    center = Offset(cardWidth * 0.50f, faceHeight * 0.56f),
                    radius = cardWidth * 0.48f
                ),
                topLeft = Offset(0f, 0f),
                size = Size(cardWidth, faceHeight),
                cornerRadius = CornerRadius(corner, corner)
            )

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        P.bottomGlowWhite,
                        P.bottomGlowBlue
                    ),
                    startY = faceHeight * P.bottomGlowStartYFraction,
                    endY = faceHeight
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
                        0.52f to P.bronze.copy(alpha = P.bronze.alpha * 0.28f),
                        0.76f to P.bronze.copy(alpha = P.bronze.alpha * 0.62f),
                        1f to P.bronze
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
                    colors = listOf(Color.Transparent, P.shadeBlack),
                    start = Offset(cardWidth * 0.58f, faceHeight * 0.30f),
                    end = Offset(cardWidth, faceHeight)
                ),
                topLeft = Offset(0f, 0f),
                size = Size(cardWidth, faceHeight),
                cornerRadius = CornerRadius(corner, corner)
            )

            val creaseBandH = maxOf(P.creaseHighlightBand.toPx(), P.creaseStroke.toPx())
            val pk = P.creaseHighlightPeakXFraction.coerceIn(0.08f, 0.48f)
            val hi = P.creaseHighlight
            val t = Color.Transparent
            drawRect(
                brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0f to t,
                        (pk - 0.18f).coerceAtLeast(0f) to t,
                        (pk - 0.07f).coerceAtLeast(0f) to hi.copy(alpha = hi.alpha * 0.25f),
                        pk to hi,
                        (pk + 0.07f).coerceAtMost(1f) to hi.copy(alpha = hi.alpha * 0.25f),
                        (pk + 0.18f).coerceAtMost(1f) to t,
                        1f to t
                    ),
                    startX = 0f,
                    endX = cardWidth
                ),
                topLeft = Offset(0f, faceHeight - creaseBandH * 0.5f),
                size = Size(cardWidth, creaseBandH)
            )
        }

        val density = LocalDensity.current
        val C = HomeBankCardContentPadding
        val L = HomeBankCardLogos
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = C.horizontal, vertical = C.vertical)
        ) {
            val targetNumberWidthPx = with(density) { (maxWidth * C.numberTargetWidthFraction).toPx() }
            val numberGroups = remember(number) {
                number.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
            }
            var numberRowWidthPx by remember(number) { mutableFloatStateOf(0f) }

            if (monobankLogo != null) {
                Image(
                    bitmap = monobankLogo,
                    contentDescription = monobankContentDescription,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-12).dp)
                        .width(L.monobankWidth)
                        .height(L.monobankHeight),
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 6.dp)
                    .fillMaxWidth(C.numberTargetWidthFraction),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(C.numberInterGroupSpacing),
                    modifier = Modifier
                        .onSizeChanged { numberRowWidthPx = it.width.toFloat() }
                        .graphicsLayer {
                            transformOrigin = TransformOrigin.Center
                            if (numberRowWidthPx > 0f) {
                                scaleX = (targetNumberWidthPx / numberRowWidthPx)
                                    .coerceIn(C.numberScaleMin, C.numberScaleMax)
                            }
                        }
                ) {
                    if (numberGroups.isEmpty()) {
                        Text(
                            text = number,
                            style = numberStyle,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    } else {
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

            if (visaLogo != null) {
                Image(
                    bitmap = visaLogo,
                    contentDescription = visaContentDescription,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = -L.visaBottomLift)
                        .width(L.visaWidth)
                        .height(L.visaHeight),
                    contentScale = ContentScale.Fit
                )
            } else {
                Text(
                    text = visaFallbackText,
                    style = visaFallbackStyle,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = -L.visaBottomLift)
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

    BoxWithConstraints(modifier) {
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
