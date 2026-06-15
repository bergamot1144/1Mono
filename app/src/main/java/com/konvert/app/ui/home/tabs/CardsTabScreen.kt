package com.konvert.app.ui.home.tabs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.konvert.app.ui.home.HomeCardDetailScreen
import com.konvert.app.ui.home.HomeCardsCarouselPageCount
import com.konvert.app.ui.home.HomeCardsTabDashboard
import com.konvert.app.ui.home.HomeOperationUi
import com.konvert.app.ui.home.HomeProfileMenuBottomSheet
import com.konvert.app.ui.home.StaticHomeBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsTabScreen(
    modifier: Modifier = Modifier,
    onOpenAdmin: () -> Unit = {},
    onOperationSelected: (HomeOperationUi) -> Unit = {}
) {
    val pageCount = HomeCardsCarouselPageCount
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
    var cardDetailOpen by remember { mutableStateOf(false) }
    val homeListState = rememberLazyListState()
    var pagerSectionHeightPx by remember { mutableFloatStateOf(0f) }
    val homeScrollOffsetPx by remember {
        derivedStateOf {
            with(homeListState) {
                if (firstVisibleItemIndex == 0) {
                    firstVisibleItemScrollOffset.toFloat()
                } else {
                    pagerSectionHeightPx + firstVisibleItemScrollOffset.toFloat()
                }
            }
        }
    }
    var homeScrollContentHeightPx by remember { mutableFloatStateOf(0f) }
    var profileMenuOpen by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val maxPullPx = with(density) { 220.dp.toPx() }
    var pullDistancePx by remember { mutableFloatStateOf(0f) }
    val isAtTop by remember {
        derivedStateOf {
            homeListState.firstVisibleItemIndex == 0 && homeListState.firstVisibleItemScrollOffset == 0
        }
    }
    val pullNestedScroll = remember(cardDetailOpen, isAtTop, maxPullPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source != NestedScrollSource.Drag) return Offset.Zero
                if (available.y >= 0f || pullDistancePx <= 0f) return Offset.Zero
                val consume = minOf(pullDistancePx, -available.y)
                pullDistancePx -= consume
                return Offset(x = 0f, y = -consume)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source != NestedScrollSource.Drag) return Offset.Zero
                if (cardDetailOpen || !isAtTop || available.y <= 0f) return Offset.Zero
                val dragDown = available.y * 0.56f
                val prev = pullDistancePx
                pullDistancePx = (pullDistancePx + dragDown).coerceIn(0f, maxPullPx)
                return Offset(x = 0f, y = pullDistancePx - prev)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullDistancePx <= 0f) return Velocity.Zero
                pullDistancePx = 0f
                return available
            }
        }
    }
    val pullOffsetPx by animateFloatAsState(
        targetValue = pullDistancePx,
        animationSpec = tween(durationMillis = 220),
        label = "cardsTabPullOffsetPx"
    )
    val operationsDropDp = with(density) { (pullOffsetPx * 0.23f).toDp() }

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
                    lazyListState = homeListState,
                    onPagerSectionHeightPx = { pagerSectionHeightPx = it },
                    onHomeScrollContentHeightPx = { homeScrollContentHeightPx = it },
                    onRequestProfileMenu = { profileMenuOpen = true },
                    onOperationSelected = onOperationSelected,
                    pullToRefreshOffsetDp = operationsDropDp,
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(pullNestedScroll)
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
