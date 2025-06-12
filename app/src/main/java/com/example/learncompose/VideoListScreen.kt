package com.example.learncompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // 导入所有布局相关的工具
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.* // 导入所有 Compose runtime 相关的工具
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.learncompose.network.Video

import com.google.accompanist.swiperefresh.SwipeRefresh // Add for pull-to-refresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState // Add for pull-to-refresh

@Composable
fun VideoListScreen(viewModel: VideoListViewModel) {
    val videos by viewModel.videos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val canLoadMore by viewModel.canLoadMore.collectAsState()

    val listState = rememberLazyListState()

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing), onRefresh = { viewModel.refreshVideos() }) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(videos) {
            video -> VideoListItem(video = video)
        }

        if (isLoading && canLoadMore) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
    }

    // Load More scroll listener
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index } // 监听最后一个可见项的索引
            .collect { index ->
                if (index != null && index >= videos.size - 1 && !isLoading && canLoadMore) { // 当滚动到列表末尾时
                    viewModel.loadMoreVideos()
                }
            }
    }
}

@Composable
fun VideoListItem(video: Video) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = video.coverUrl),
                contentDescription = video.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 