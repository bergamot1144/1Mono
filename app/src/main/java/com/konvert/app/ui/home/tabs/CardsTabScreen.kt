package com.konvert.app.ui.home.tabs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.konvert.app.ui.home.HomeCardDetailScreen
import com.konvert.app.ui.home.HomeCardsCarouselPageCount
import com.konvert.app.ui.home.HomeCardsTabDashboard
import com.konvert.app.ui.home.HomeProfileMenuBottomSheet
import com.konvert.app.ui.home.StaticHomeBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsTabScreen(
    modifier: Modifier = Modifier,
    onOpenAdmin: () -> Unit = {}
) {
    val pageCount = HomeCardsCarouselPageCount
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
    var cardDetailOpen by remember { mutableStateOf(false) }
    val homeListStates = remember(pageCount) {
        Array(pageCount) { LazyListState(firstVisibleItemIndex = 0, firstVisibleItemScrollOffset = 0) }
    }
    val homeScrollOffsetPx by remember {
        derivedStateOf {
            val idx = pagerState.currentPage.coerceIn(0, pageCount - 1)
            homeListStates[idx].firstVisibleItemScrollOffset.toFloat()
        }
    }
    var homeScrollContentHeightPx by remember { mutableFloatStateOf(0f) }
    var profileMenuOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        StaticHomeBackground(
            modifier = Modifier.fillMaxSize(),
            cardScrollPosition = scrollPosition,
            cardBackgroundPaletteCount = pageCount,
            contentHeightPx = if (cardDetailOpen) 0f else homeScrollContentHeightPx,
            contentScrollOffsetPx = if (cardDetailOpen) 0f else homeScrollOffsetPx
        )
        AnimatedContent(
            targetState = cardDetailOpen,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                if (targetState) {
                    (fadeIn(animationSpec = tween(320)) +
                        slideInVertically(animationSpec = tween(380)) { h -> h / 12 })
                        .togetherWith(fadeOut(animationSpec = tween(260)))
                } else {
                    fadeIn(animationSpec = tween(260))
                        .togetherWith(
                            fadeOut(animationSpec = tween(280)) +
                                slideOutVertically(animationSpec = tween(320)) { h -> h / 12 }
                        )
                }
            },
            label = "homeCardDetailNav"
        ) { detail ->
            if (detail) {
                HomeCardDetailScreen(onClose = { cardDetailOpen = false })
            } else {
                HomeCardsTabDashboard(
                    pagerState = pagerState,
                    onOpenCardDetail = { cardDetailOpen = true },
                    lazyListStates = homeListStates,
                    onHomeScrollContentHeightPx = { homeScrollContentHeightPx = it },
                    onRequestProfileMenu = { profileMenuOpen = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        if (profileMenuOpen) {
            HomeProfileMenuBottomSheet(
                onDismiss = { profileMenuOpen = false },
                onAppSettingsClick = {
                    profileMenuOpen = false
                    onOpenAdmin()
                }
            )
        }
    }
}
