package com.example.learncompose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

// 轮播项的数据模型
data class CarouselItem(val color: Color, val text: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CarouselExamples() {
    // 准备一些示例数据
    val items = listOf(
        CarouselItem(Color(0xFFE6ADAD), "第一页"),
        CarouselItem(Color(0xFFADDAE6), "第二页"),
        CarouselItem(Color(0xFFADE6AD), "第三页"),
        CarouselItem(Color(0xFFE6E6AD), "第四页"),
        CarouselItem(Color(0xFFE6C4AD), "第五页")
    )

    // 使用 rememberPagerState 来记住轮播的状态
    val pagerState = rememberPagerState(pageCount = { items.size })

    Column {
        ComponentExampleCard(title = "基础轮播 (Basic Carousel)") {
            // HorizontalPager 是实现轮播的核心组件
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { page ->
                // 为每一页创建一个卡片
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(items[page].color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = items[page].text,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}