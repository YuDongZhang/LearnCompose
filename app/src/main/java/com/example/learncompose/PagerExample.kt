package com.example.learncompose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HorizontalPagerExample()
        VerticalPagerExample()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerExample() {
    LayoutTopicExampleCard("HorizontalPager: 水平分页器") {
        Text("HorizontalPager 允许用户在多个页面之间水平翻页。它类似于传统 Android 开发中的 ViewPager。")
        Spacer(Modifier.height(16.dp))

        // 1. 记住 Pager 状态，并指定页面总数
        val pageCount = 10
        val pagerState = rememberPagerState(pageCount = { pageCount })
        val scope = rememberCoroutineScope()

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 2. 创建 HorizontalPager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                pageSize = PageSize.Fill, // 页面大小，Fill表示占满整个宽度
                pageSpacing = 8.dp // 添加页面间距
            ) { page ->
                // 每个页面的内容
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "第 ${page + 1} 页", fontSize = 24.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 3. 添加页面指示器和控制按钮
            Row(
                Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = pagerState.currentPage > 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "上一页")
                }

                // 指示器
                Row(
                    Modifier
                        .wrapContentSize()
                        .height(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(12.dp)
                        )
                    }
                }

                IconButton(
                    enabled = pagerState.currentPage < pagerState.pageCount - 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "下一页")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "当前页: ${pagerState.currentPage + 1} / $pageCount",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalPagerExample() {
    LayoutTopicExampleCard("VerticalPager: 垂直分页器") {
        Text("VerticalPager 与 HorizontalPager 类似，但允许用户垂直翻页。")
        Spacer(Modifier.height(16.dp))

        val pagerState = rememberPagerState(pageCount = { 5 })

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (page % 3) {
                            0 -> MaterialTheme.colorScheme.primaryContainer
                            1 -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "垂直页面 ${page + 1}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}