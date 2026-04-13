package com.konvert.app.ui.home.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
import androidx.compose.foundation.layout.navigationBarsPadding
import com.konvert.app.ui.home.StaticHomeBackground
import com.konvert.app.ui.theme.TextPrimary

@Composable
private fun TabPlaceholderScaffold(
    titleRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        StaticHomeBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 120.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(titleRes),
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.tab_placeholder_stub),
                color = TextPrimary.copy(alpha = 0.65f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun CreditsTabScreen(modifier: Modifier = Modifier) {
    TabPlaceholderScaffold(titleRes = R.string.home_nav_credits, modifier = modifier)
}

@Composable
fun SavingsTabScreen(modifier: Modifier = Modifier) {
    TabPlaceholderScaffold(titleRes = R.string.home_nav_savings, modifier = modifier)
}

@Composable
fun MoreTabScreen(modifier: Modifier = Modifier) {
    TabPlaceholderScaffold(titleRes = R.string.home_nav_more, modifier = modifier)
}

@Composable
fun MarketTabScreen(modifier: Modifier = Modifier) {
    TabPlaceholderScaffold(titleRes = R.string.home_nav_market, modifier = modifier)
}
