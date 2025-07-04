package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowLayoutsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FlowRowExample()
        FlowColumnExample()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowExample() {
    LayoutTopicExampleCard(title = "FlowRow: 流式行布局") {
        Text(
            "FlowRow 类似于 Row，但当水平空间不足时，它会将子项自动换行到下一行。",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))

        FlowRow(
            // horizontalArrangement 用于控制一行内子项的水平间距和对齐方式。
            // Arrangement.spacedBy(8.dp) 会在每个子项之间添加 8.dp 的间距。
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            // verticalArrangement 用于控制多行之间的垂直间距。
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // maxItemsInEachRow 可以限制每行最多显示的子项数量。
            // 这里我们不限制，让它根据宽度自动换行。
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            // 创建一些不同宽度的示例项
            val items = listOf(
                "标签 1", "一个长标签", "标签 3", "短", "另一个长标签",
                "标签 5", "标签 6", "中等长度", "标签 8", "标签 9", "超长标签示例"
            )
            items.forEach { text ->
                Chip(text = text)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowColumnExample() {
    LayoutTopicExampleCard(title = "FlowColumn: 流式列布局") {
        Text(
            "FlowColumn 类似于 Column，但当垂直空间不足时，它会将子项自动换列到下一列。",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))

        FlowColumn(
            // verticalArrangement 用于控制一列内子项的垂直间距和对齐方式。
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // horizontalArrangement 用于控制多列之间的水平间距。
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            // maxItemsInEachColumn 可以限制每列最多显示的子项数量。
            maxItemsInEachColumn = 4,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // 给定一个固定的高度来演示换列
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            // 创建一些不同高度的示例项
            val items = listOf(
                "第1项", "第2项", "第3项", "第4项 (换列)",
                "第5项", "第6项", "第7项", "第8项 (换列)",
                "第9项", "第10项"
            )
            items.forEach { text ->
                Chip(text = text)
            }
        }
    }
}

// 用于演示的 Chip 可组合项
@Composable
private fun Chip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}