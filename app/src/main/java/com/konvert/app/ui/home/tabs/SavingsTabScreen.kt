package com.konvert.app.ui.home.tabs

import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.konvert.app.admin.AppAdminState
import com.konvert.app.admin.JarAdminConfig
import com.konvert.app.admin.LocalAppAdmin
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.konvert.app.R
import java.util.Locale

private val SavingsScreenBg = Color(0xFF121212)
private val SavingsHeaderTop = Color(0xFF003066)
private val SavingsHeaderBottom = Color(0xFF61ADFA)
private val SavingsCarouselCellBg = Color(0xFF292929)
private val SavingsDepositIconCircle = Color(0xFF3B5BDB)
private val SavingsBankJarCircle = Color(0xFFE5656A)
private val SavingsBondsIconCircle = Color(0xFF5BA372)
private val SavingsArchiveIconCircle = Color(0xFF5C5C5E)
private val SavingsCaptionMuted = Color(0xFF8E8E93)
private val SavingsListDivider = Color.White.copy(alpha = 0.08f)
private val SavingsJarProgressTrack = Color(0xFF21141C)
private val SavingsJarProgressFill = Color(0xFFBB3789)
private val SavingsHeaderIndicatorActive = Color(0xFFFFFDFC)
private val SavingsHeaderIndicatorInactive = Color(0xFF6DA8DF)
private val SavingsHeaderTopContentOffset = 6.dp
private val SavingsActionsCarouselTop = 154.dp
private const val SavingsTrizubecIconAsset = "operations_logos/trizubec.png"
private const val SavingsArchiveIconAsset = "operations_logos/Archive.png"
private const val SavingsJarsIconAsset = "operations_logos/jars.png"
private const val SavingsOpenDepositIconAsset = "operations_logos/open_deposit.png"
private const val SavingsOpenJarIconAsset = "operations_logos/open_jaar.png"
private const val SavingsInfoIconAsset = "operations_logos/info.png"

private data class SavingsActionCell(
    val titleRes: Int,
    val icon: ImageVector,
    val circleColor: Color,
    val assetPath: String? = null
)

private data class SavingsJarRow(
    val title: String,
    val accumulatedLine: String,
    val targetLine: String,
    val progress: Float,
    val jarCircleColor: Color
)

private data class SavingsHeaderSlide(
    val titleRes: Int,
    val amount: String
)

@Composable
private fun rememberSavingsAssetBitmap(assetPath: String?): ImageBitmap? {
    val context = LocalContext.current
    return remember(assetPath) {
        if (assetPath == null) {
            null
        } else {
            runCatching {
                context.assets.open(assetPath).use { input ->
                    BitmapFactory.decodeStream(input)?.asImageBitmap()
                }
            }.getOrNull()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavingsTabScreen(
    modifier: Modifier = Modifier,
    onOpenJarBankCar: (Int) -> Unit = {}
) {
    val actions = remember {
        listOf(
            SavingsActionCell(
                R.string.savings_action_deposit,
                Icons.Outlined.MyLocation,
                SavingsDepositIconCircle,
                assetPath = SavingsOpenDepositIconAsset
            ),
            SavingsActionCell(
                R.string.savings_action_bank,
                Icons.Outlined.Savings,
                SavingsBankJarCircle,
                assetPath = SavingsOpenJarIconAsset
            ),
            SavingsActionCell(
                R.string.savings_action_bonds,
                Icons.Outlined.Savings,
                SavingsBondsIconCircle,
                assetPath = SavingsTrizubecIconAsset
            ),
            SavingsActionCell(
                R.string.savings_action_archive,
                Icons.Outlined.Archive,
                SavingsArchiveIconCircle,
                assetPath = SavingsArchiveIconAsset
            )
        )
    }
    val admin = LocalAppAdmin.current
    val jarConfigs = admin?.state?.jars ?: AppAdminState().jars
    val jarCirclePalette = remember {
        listOf(SavingsBankJarCircle, SavingsBondsIconCircle, SavingsDepositIconCircle)
    }
    val jars = jarConfigs.mapIndexed { index, jar ->
        jar.toSavingsJarRow(
            index = index,
            circleColor = jarCirclePalette[index % jarCirclePalette.size]
        )
    }
    val fallbackHeaderBalance = stringResource(R.string.savings_header_balance)
    val uahBalanceText = remember(jarConfigs) {
        jarConfigs
            .map { it.balanceDisplay }
            .filter { it.isNotBlank() }
            .firstOrNull()
    }?.let(::normalizeMoneyDisplay) ?: fallbackHeaderBalance
    val usdBalanceText = normalizeMoneyDisplay(admin?.state?.cardOrDefault(2)?.balanceDisplay ?: "0 $")
    val eurBalanceText = normalizeMoneyDisplay(admin?.state?.cardOrDefault(3)?.balanceDisplay ?: "0 €")
    val headerSlides = listOf(
        SavingsHeaderSlide(R.string.savings_header_title_uah, uahBalanceText),
        SavingsHeaderSlide(R.string.savings_header_title_usd, usdBalanceText),
        SavingsHeaderSlide(R.string.savings_header_title_eur, eurBalanceText)
    )
    val headerPagerState = rememberPagerState(pageCount = { headerSlides.size })
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SavingsScreenBg)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.34f, fill = true)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(SavingsHeaderTop, SavingsHeaderBottom)
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(horizontal = 8.dp)
                    ) {
                        val infoCd = stringResource(R.string.savings_info_cd)
                        val infoBitmap = rememberSavingsAssetBitmap(SavingsInfoIconAsset)
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .size(48.dp)
                                .offset(y = SavingsHeaderTopContentOffset - 6.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            if (infoBitmap != null) {
                                Image(
                                    bitmap = infoBitmap,
                                    contentDescription = infoCd,
                                    modifier = Modifier.size(45.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = infoCd,
                                    tint = Color.White.copy(alpha = 0.95f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(top = 4.dp + SavingsHeaderTopContentOffset),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            HorizontalPager(
                                state = headerPagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(108.dp),
                                verticalAlignment = Alignment.Top
                            ) { page ->
                                val slide = headerSlides[page]
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    SavingsText(
                                        text = stringResource(slide.titleRes),
                                        color = Color.White.copy(alpha = 0.92f),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    SavingsText(
                                        text = slide.amount,
                                        color = Color.White,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.2.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = (-10).dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(headerSlides.size) { idx ->
                                    val dotColor = animateColorAsState(
                                        targetValue = if (headerPagerState.currentPage == idx) {
                                            SavingsHeaderIndicatorActive
                                        } else {
                                            SavingsHeaderIndicatorInactive
                                        },
                                        label = "savingsHeaderDotColor"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 4.dp)
                                            .size(9.dp)
                                            .clip(CircleShape)
                                            .background(dotColor.value)
                                    )
                                }
                            }
                        }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(
                    top = 46.dp,
                        bottom = 112.dp
                    )
                ) {
                    item {
                    Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            SavingsText(
                                text = stringResource(R.string.savings_section_banks),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            SavingsText(
                                text = stringResource(R.string.savings_section_banks_subtitle),
                                color = SavingsCaptionMuted,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    itemsIndexed(jars, key = { index, _ -> index }) { index, jar ->
                        SavingsJarListRow(
                            jar = jar,
                            onClick = { onOpenJarBankCar(index) }
                        )
                    }
                }
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = SavingsActionsCarouselTop)
                    .zIndex(2f)
                    .height(146.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                items(
                    items = actions,
                    key = { it.titleRes }
                ) { cell ->
                    SavingsCarouselCell(
                        cell = cell,
                        modifier = Modifier.width(208.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SavingsCarouselCell(
    cell: SavingsActionCell,
    modifier: Modifier = Modifier
) {
    val assetBitmap = rememberSavingsAssetBitmap(cell.assetPath)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SavingsCarouselCellBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { }
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .then(
                    if (assetBitmap == null) {
                        Modifier.background(cell.circleColor)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (assetBitmap != null) {
                Image(
                    bitmap = assetBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = cell.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        SavingsText(
            text = stringResource(cell.titleRes),
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
            maxLines = 2
        )
    }
}

private fun JarAdminConfig.toSavingsJarRow(index: Int, circleColor: Color): SavingsJarRow {
    val title = name.takeIf { it.isNotBlank() } ?: "Банка ${index + 1}"
    val accumulated = accumulatedDisplay
        .takeIf { it.isNotBlank() }
        ?.let { raw ->
            if (raw.contains("Накопичено")) {
                val valuePart = raw.substringAfter("Накопичено").trim()
                "Накопичено ${normalizeMoneyDisplay(valuePart)}"
            } else {
                "Накопичено ${normalizeMoneyDisplay(raw)}"
            }
        }
        ?: "Накопичено ${normalizeMoneyDisplay(balanceDisplay.trim())}"
    val target = normalizeMoneyDisplay(targetDisplay.takeIf { it.isNotBlank() } ?: balanceDisplay)
    val progress = calculateJarProgress(accumulated, target)
    return SavingsJarRow(
        title = title,
        accumulatedLine = accumulated,
        targetLine = target,
        progress = progress,
        jarCircleColor = circleColor
    )
}

private fun calculateJarProgress(accumulatedLine: String, targetLine: String): Float {
    val accumulated = parseMoneyAmount(accumulatedLine)
    val target = parseMoneyAmount(targetLine)
    if (target <= 0.0) return 0f
    return (accumulated / target).toFloat().coerceIn(0f, 1f)
}

private fun parseMoneyAmount(text: String): Double {
    val normalized = text.replace(',', '.')
    val matches = Regex("\\d+(?:\\.\\d+)?").findAll(normalized).map { it.value }.toList()
    if (matches.isEmpty()) return 0.0
    if (matches.size >= 2 && !normalized.contains('.')) {
        val integerPart = matches.dropLast(1).joinToString("")
        val fractionPart = matches.last()
        return "$integerPart.$fractionPart".toDoubleOrNull() ?: 0.0
    }
    return matches.joinToString("").toDoubleOrNull() ?: 0.0
}

private fun normalizeMoneyDisplay(text: String): String {
    val currency = when {
        text.contains("₴") -> "₴"
        text.contains("$") -> "$"
        text.contains("€") -> "€"
        else -> ""
    }
    val amount = parseMoneyAmount(text)
    val number = String.format(Locale.US, "%,.2f", amount).replace(',', ' ')
    return if (currency.isBlank()) number else "$number $currency"
}

@Composable
private fun SavingsJarListRow(
    jar: SavingsJarRow,
    onClick: () -> Unit
) {
    val jarsBitmap = rememberSavingsAssetBitmap(SavingsJarsIconAsset)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .then(
                        if (jarsBitmap == null) {
                            Modifier.background(jar.jarCircleColor)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (jarsBitmap != null) {
                    Image(
                        bitmap = jarsBitmap,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Savings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SavingsText(
                        text = jar.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    SavingsText(
                        text = jar.targetLine,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(SavingsJarProgressTrack)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(jar.progress)
                            .height(3.dp)
                            .clip(RoundedCornerShape(99.dp))
                            .background(SavingsJarProgressFill)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                SavingsText(
                    text = jar.accumulatedLine,
                    color = SavingsCaptionMuted,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun SavingsText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    letterSpacing: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    lineHeight: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        maxLines = maxLines,
        overflow = overflow
    )
}
