package com.example.learncompose.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/lists?hl=zh-cn
 * 本文件演示 Compose 列表（LazyColumn、LazyRow、LazyVerticalGrid）等常用用法。
 * 包含分组、嵌套、contentType、footer、divider等进阶技巧。
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("LazyColumn 基础用法", style = MaterialTheme.typography.titleMedium)
        SimpleLazyColumn()
        Text("LazyRow 横向列表", style = MaterialTheme.typography.titleMedium)
        SimpleLazyRow()
        Text("LazyVerticalGrid 网格列表", style = MaterialTheme.typography.titleMedium)
        SimpleLazyVerticalGrid()
        Text("分组列表（Section Header）", style = MaterialTheme.typography.titleMedium)
        GroupedLazyColumn()
        Text("嵌套滚动列表", style = MaterialTheme.typography.titleMedium)
        NestedLazyColumn()
        Text("contentType 优化", style = MaterialTheme.typography.titleMedium)
        ContentTypeLazyColumn()
        Text("带 Footer 的列表", style = MaterialTheme.typography.titleMedium)
        FooterLazyColumn()
        Text("列表中使用 Divider", style = MaterialTheme.typography.titleMedium)
        DividerLazyColumn()
    }
}

@Composable
fun SimpleLazyColumn() {
    LazyColumn(
        modifier = Modifier.height(120.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            Text("Item #$index", modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun SimpleLazyRow() {
    LazyRow(
        modifier = Modifier.height(56.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            Text("Row #$index", modifier = Modifier.padding(8.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleLazyVerticalGrid() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(80.dp),
        modifier = Modifier.height(120.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(8) { index ->
            Text("Grid #$index", modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun GroupedLazyColumn() {
    val groups = listOf(
        "A" to listOf("Apple", "Avocado"),
        "B" to listOf("Banana", "Blueberry"),
        "C" to listOf("Cherry", "Coconut")
    )
    LazyColumn(
        modifier = Modifier.height(140.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        groups.forEach { (header, items) ->
            stickyHeader {
                Text("分组: $header", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(4.dp))
            }
            items(items) { item ->
                Text(item, modifier = Modifier.padding(start = 16.dp, bottom = 4.dp))
            }
        }
    }
}

@Composable
fun NestedLazyColumn() {
    LazyColumn(
        modifier = Modifier.height(120.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(3) { outer ->
            Text("外层项 #$outer", style = MaterialTheme.typography.bodyMedium)
            LazyRow(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(4) { inner ->
                    Text("内层$outer-$inner", modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}

@Composable
fun ContentTypeLazyColumn() {
    val items = listOf("文本", 123, "更多文本", 456)
    LazyColumn(
        modifier = Modifier.height(100.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items, contentType = { it::class }) { item ->
            when (item) {
                is String -> Text("字符串: $item", modifier = Modifier.padding(4.dp))
                is Int -> Text("数字: $item", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun FooterLazyColumn() {
    val data = (1..4).toList()
    LazyColumn(
        modifier = Modifier.height(100.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(data) { item ->
            Text("内容 $item", modifier = Modifier.padding(4.dp))
        }
        item {
            Text("--- Footer 页脚 ---", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun DividerLazyColumn() {
    val data = (1..4).toList()
    LazyColumn(
        modifier = Modifier.height(100.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(data) { index, item ->
            Text("项 $item", modifier = Modifier.padding(4.dp))
            if (index < data.lastIndex) {
                Divider(modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
} 