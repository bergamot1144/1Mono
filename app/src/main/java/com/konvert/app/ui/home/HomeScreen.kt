package com.konvert.app.ui.home

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
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
import com.konvert.app.ui.theme.OperationsBlockColor
import com.konvert.app.ui.theme.PinPromptText
import com.konvert.app.ui.theme.QuickActionCircleFill
import com.konvert.app.ui.theme.TextPrimary

private val CardShape = RoundedCornerShape(24.dp)
private val ChipShape = RoundedCornerShape(20.dp)
private val ActionCircleSize = 56.dp

/** Висота нижньої «пігулки» та Маркету — радіус = половина висоти (капсула / коло). */
private val BottomBarHeight = 56.dp
private val BottomBarPillRadius = 28.dp
private val BottomBarGap = 10.dp

@Composable
fun StaticHomeBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val width = size.width
                val height = size.height

                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color(0xFF081754),
                            0.18f to Color(0xFF0B2776),
                            0.34f to Color(0xFF0B2C82),
                            0.48f to Color(0xFF08245D),
                            0.62f to Color(0xFF041736),
                            0.72f to Color(0xFF121212),
                            1.00f to Color(0xFF121212)
                        ),
                        startY = 0f,
                        endY = height
                    ),
                    size = size
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x332F6DFF),
                            Color.Transparent
                        ),
                        center = Offset(width * 0.50f, height * 0.22f),
                        radius = height * 0.42f
                    ),
                    radius = height * 0.42f,
                    center = Offset(width * 0.50f, height * 0.22f)
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x1A4D8DFF),
                            Color.Transparent
                        ),
                        center = Offset(width * 0.50f, height * 0.40f),
                        radius = height * 0.26f
                    ),
                    radius = height * 0.26f,
                    center = Offset(width * 0.50f, height * 0.40f)
                )

                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color.Transparent,
                            0.55f to Color.Transparent,
                            0.72f to Color(0x44000000),
                            0.86f to Color(0xCC121212),
                            1.00f to Color(0xFF121212)
                        ),
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
    Box(modifier = Modifier.fillMaxSize()) {
        StaticHomeBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 104.dp
            )
        ) {
            item { HomeTopBar(onProfileClick = { /* далі */ }) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { HomeBalanceBlock() }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { HomeCardPlaceholder() }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item { HomeAllCardsChip() }
            item { Spacer(modifier = Modifier.height(28.dp)) }
            item { HomeQuickActions() }
            item { Spacer(modifier = Modifier.height(28.dp)) }
            item { HomeOperationsCard() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { HomeLimitsAbroadCard() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { HomeUsefulCard() }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        HomeBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 6.dp)
        )
    }
}

@Composable
private fun HomeTopBar(onProfileClick: () -> Unit) {
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
                tint = HomeBalanceBarTint,
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
                tint = HomeBalanceBarTint,
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
                    .clip(ChipShape)
                    .background(KeypadButton)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CardGiftcard,
                    contentDescription = stringResource(R.string.home_bonus_cd),
                    tint = HomeBalanceBarTint,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "1 659 ₴",
                    color = HomeBalanceBarTint,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                imageVector = Icons.Outlined.BarChart,
                contentDescription = null,
                tint = HomeBalanceBarTint,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun HomeBalanceBlock() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(KeypadButton)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = HomeBalanceBarTint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.home_balance_main),
                color = HomeBalanceBarTint,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BalanceChip(
                icon = { Icon(Icons.Outlined.AccountBalanceWallet, null, tint = HomeBalanceBarTint, modifier = Modifier.size(16.dp)) },
                text = stringResource(R.string.home_balance_wallet)
            )
            BalanceChip(
                icon = { Icon(Icons.Filled.CreditCard, null, tint = HomeBalanceBarTint, modifier = Modifier.size(16.dp)) },
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
            .background(KeypadButton)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        icon()
        Text(text = text, color = HomeBalanceBarTint, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun HomeCardPlaceholder() {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                rotationZ = -6f
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .height(168.dp),
            shape = shape,
            color = KeypadButton,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.home_card_placeholder),
                    color = PinPromptText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "•••• 4829",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
                Text(
                    text = stringResource(R.string.home_card_stub),
                    color = PinPromptText,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun HomeAllCardsChip() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(ChipShape)
                .background(KeypadButton)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { }
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CreditCard,
                contentDescription = null,
                tint = PinPromptText,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.home_all_cards),
                color = TextPrimary,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun HomeQuickActions() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickAction(Icons.Filled.CreditCard, stringResource(R.string.home_action_transfer))
        QuickAction(Icons.Filled.Description, stringResource(R.string.home_action_iban))
        QuickAction(Icons.Filled.Layers, stringResource(R.string.home_action_other))
    }
}

@Composable
private fun QuickAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(108.dp)
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
            Icon(imageVector = icon, contentDescription = null, tint = HomeBalanceBarTint, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 12.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            maxLines = 3,
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
        Column(modifier = Modifier.padding(16.dp)) {
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
                Text(
                    text = stringResource(R.string.home_operations_all) + " ›",
                    color = PinPromptText,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OperationRow(stringResource(R.string.home_op_steam), stringResource(R.string.home_op_steam_amount))
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.08f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            OperationRow(stringResource(R.string.home_op_card), stringResource(R.string.home_op_card_amount))
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.08f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            OperationRow(stringResource(R.string.home_op_mcd), stringResource(R.string.home_op_mcd_amount))
            Spacer(modifier = Modifier.height(4.dp))
            TextButton(
                onClick = { /* усі операції — далі */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.home_operations_expand),
                    color = AccentRed,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun OperationRow(title: String, amount: String) {
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
                .background(AvatarPlaceholder),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title.take(1).uppercase(),
                color = PinPromptText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
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
                    .height(52.dp)
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
                RateCell(
                    flag = "🇺🇸",
                    title = stringResource(R.string.home_rate_usd_title),
                    values = stringResource(R.string.home_rate_usd_values),
                    modifier = Modifier.weight(1f)
                )
                RateCell(
                    flag = "🇪🇺",
                    title = stringResource(R.string.home_rate_eur_title),
                    values = stringResource(R.string.home_rate_eur_values),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

@Composable
private fun RateCell(flag: String, title: String, values: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = HomeUsefulTileFill
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = flag, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = PinPromptText, fontSize = 12.sp)
            Text(text = values, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun UsefulTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    iconTint: Color = AccentRed
) {
    Surface(
        modifier = modifier
            .height(88.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { },
        shape = RoundedCornerShape(14.dp),
        color = HomeUsefulTileFill
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HomeBottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
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
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavPillItem(
                    Icons.Filled.CreditCard,
                    stringResource(R.string.home_nav_cards),
                    selected = true,
                    modifier = Modifier.weight(1f)
                )
                NavPillItem(
                    Icons.Filled.PieChart,
                    stringResource(R.string.home_nav_credits),
                    selected = false,
                    modifier = Modifier.weight(1f)
                )
                NavPillItem(
                    Icons.Filled.Savings,
                    stringResource(R.string.home_nav_savings),
                    selected = false,
                    modifier = Modifier.weight(1f)
                )
                NavPillItem(
                    Icons.Filled.Apps,
                    stringResource(R.string.home_nav_more),
                    selected = false,
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
            onClick = { }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingBag,
                    contentDescription = stringResource(R.string.home_nav_market),
                    tint = HomeNavIconInactive,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = stringResource(R.string.home_nav_market),
                    color = HomeNavIconInactive,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun NavPillItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val tint = if (selected) AccentRed else HomeNavIconInactive
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
            .padding(horizontal = 2.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, label, tint = tint, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = tint, fontSize = 10.sp, maxLines = 1)
    }
}
