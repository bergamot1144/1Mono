package com.konvert.app.ui.home.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.konvert.app.ui.home.HomeCardsCarouselPageCount
import com.konvert.app.ui.home.HomeCardsTabDashboard
import com.konvert.app.ui.home.StaticHomeBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsTabScreen(modifier: Modifier = Modifier) {
    val pageCount = HomeCardsCarouselPageCount
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction

    Box(modifier = modifier.fillMaxSize()) {
        StaticHomeBackground(
            modifier = Modifier.fillMaxSize(),
            cardScrollPosition = scrollPosition,
            cardBackgroundPaletteCount = pageCount
        )
        HomeCardsTabDashboard(pagerState = pagerState, modifier = Modifier.fillMaxSize())
    }
}
