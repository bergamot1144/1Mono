package com.konvert.app.ui.home.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.outlined.ShowChart
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R

private val SavingsScreenBg = Color(0xFF121212)
private val SavingsHeaderBlue = Color(0xFF2D4DB5)
private val SavingsCarouselCellBg = Color(0xFF292929)
private val SavingsDepositIconCircle = Color(0xFF3B5BDB)
private val SavingsBankJarCircle = Color(0xFFE5656A)
private val SavingsBondsIconCircle = Color(0xFF5BA372)
private val SavingsArchiveIconCircle = Color(0xFF5C5C5E)
private val SavingsCaptionMuted = Color(0xFF8E8E93)
private val SavingsListDivider = Color.White.copy(alpha = 0.08f)

private data class SavingsActionCell(
    val titleRes: Int,
    val icon: ImageVector,
    val circleColor: Color
)

private data class SavingsJarRow(
    val titleRes: Int,
    val accumulatedRes: Int,
    val targetRes: Int,
    val jarCircleColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavingsTabScreen(modifier: Modifier = Modifier) {
    val actions = remember {
        listOf(
            SavingsActionCell(R.string.savings_action_deposit, Icons.Outlined.MyLocation, SavingsDepositIconCircle),
            SavingsActionCell(R.string.savings_action_bank, Icons.Outlined.Savings, SavingsBankJarCircle),
            SavingsActionCell(R.string.savings_action_bonds, Icons.Outlined.ShowChart, SavingsBondsIconCircle),
            SavingsActionCell(R.string.savings_action_archive, Icons.Outlined.Archive, SavingsArchiveIconCircle)
        )
    }
    val jars = remember {
        listOf(
            SavingsJarRow(
                titleRes = R.string.savings_jar_car_title,
                accumulatedRes = R.string.savings_jar_car_accumulated,
                targetRes = R.string.savings_jar_car_target,
                jarCircleColor = SavingsBankJarCircle
            )
        )
    }
    val actionPagerState = rememberPagerState(pageCount = { actions.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SavingsScreenBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.28f, fill = true)
                .background(SavingsHeaderBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val infoCd = stringResource(R.string.savings_info_cd)
                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(48.dp)
                    ) {
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
                        .padding(horizontal = 12.dp)
                        .padding(top = 4.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.savings_header_title),
                        color = Color.White.copy(alpha = 0.92f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.savings_header_balance),
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.2.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 112.dp
            )
        ) {
            item {
                HorizontalPager(
                    state = actionPagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(132.dp)
                        .offset(y = (-18).dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    pageSpacing = 10.dp,
                    beyondBoundsPageCount = 2
                ) { page ->
                    SavingsCarouselCell(
                        cell = actions[page],
                        modifier = Modifier.width(154.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.savings_section_banks),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.savings_section_banks_subtitle),
                        color = SavingsCaptionMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            itemsIndexed(jars) { index, jar ->
                SavingsJarListRow(jar = jar, showDividerBelow = index < jars.lastIndex)
            }
        }
    }
}

@Composable
private fun SavingsCarouselCell(
    cell: SavingsActionCell,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SavingsCarouselCellBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { }
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(cell.circleColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = cell.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(cell.titleRes),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun SavingsJarListRow(jar: SavingsJarRow, showDividerBelow: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { }
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(jar.jarCircleColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Savings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(jar.titleRes),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(jar.accumulatedRes),
                    color = SavingsCaptionMuted,
                    fontSize = 13.sp
                )
            }
            Text(
                text = stringResource(jar.targetRes),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
        if (showDividerBelow) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(1.dp)
                    .background(SavingsListDivider)
            )
        }
    }
}
