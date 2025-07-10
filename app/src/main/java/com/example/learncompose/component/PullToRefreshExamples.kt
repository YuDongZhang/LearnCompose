package com.example.learncompose.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState

import androidx.compose.ui.graphics.graphicsLayer

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/components/pull-to-refresh?hl=zh-cn
 * 本文件演示 Compose 下拉刷新 PullToRefreshBox、PullToRefreshState、指示器自定义等用法。
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("PullToRefreshBox（下拉刷新）", style = MaterialTheme.typography.titleMedium)
        SimplePullToRefresh()
        Text("自定义指示器", style = MaterialTheme.typography.titleMedium)
        CustomIndicatorPullToRefresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimplePullToRefresh() {
    val scope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        isRefreshing = refreshing,
        onRefresh = {
            scope.launch {
                refreshing = true
                delay(1500)
                refreshing = false
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(10) { i ->
                Text("列表项 $i", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomIndicatorPullToRefresh() {
    val scope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        isRefreshing = refreshing,
        onRefresh = {
            scope.launch {
                refreshing = true
                delay(1500)
                refreshing = false
            }
        },
        indicator = {
            MyCustomIndicator(state, refreshing)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(180.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(10) { i ->
                Text("自定义指示器项 $i", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCustomIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.pullToRefreshIndicator(
            state = state,
            isRefreshing = isRefreshing,
            containerColor = PullToRefreshDefaults.containerColor,
            threshold = PullToRefreshDefaults.PositionalThreshold
        ),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isRefreshing,
            animationSpec = androidx.compose.animation.core.tween(durationMillis = 300),
            modifier = Modifier.align(Alignment.Center)
        ) { refreshing ->
            if (refreshing) {
                CircularProgressIndicator(Modifier.size(24.dp))
            } else {
                val distanceFraction = state.distanceFraction.coerceIn(0f, 1f)
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Refresh",
                    modifier = Modifier
                        .size(18.dp)
                        .graphicsLayer {
                            this.alpha = distanceFraction
                            this.scaleX = distanceFraction
                            this.scaleY = distanceFraction
                        }
                )
            }
        }
    }
} 