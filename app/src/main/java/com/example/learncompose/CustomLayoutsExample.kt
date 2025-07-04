package com.example.learncompose

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

// 主入口，展示所有自定义布局的示例
@Composable
fun CustomLayoutsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        MyBasicColumnExample()
        MyStaggeredGridExample()
        CustomModifierExample()
    }
}

// 示例1：一个简单的自定义 Column
@Composable
fun MyBasicColumnExample() {
    LayoutTopicExampleCard("自定义布局: 简单的 Column") {
        Text("你可以通过 Layout Composable 创建自己的自定义布局。你需要手动测量和放置子项。")
        Spacer(Modifier.height(8.dp))
        Text("下面是一个自定义的 Column，它会将子项垂直排列：")
        Spacer(Modifier.height(16.dp))
        MyBasicColumn(modifier = Modifier.border(1.dp, Color.Gray).padding(8.dp)) {
            Text("第一项")
            Text("第二项")
            Text("第三项")
        }
    }
}

@Composable
fun MyBasicColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Layout Composable 是自定义布局的核心
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // 1. 测量 (Measure)
        // 不要用相同的约束条件测量子项两次。
        // 测量每个子 Composable，获取它们的 Placeable
        val placeables = measurables.map { measurable ->
            // 测量每个子项
            measurable.measure(constraints)
        }

        // 2. 布局 (Layout)
        // layout() 函数定义了父布局的大小，并提供了放置子项的 lambda
        layout(
            width = constraints.maxWidth,
            height = placeables.sumOf { it.height }
        ) {
            var yPosition = 0
            // 放置每个子项
            placeables.forEach { placeable ->
                // 在屏幕上放置子项
                placeable.placeRelative(x = 0, y = yPosition)
                // 更新下一个子项的 Y 坐标
                yPosition += placeable.height
            }
        }
    }
}


// 示例2：交错网格布局
@Composable
fun MyStaggeredGridExample() {
    LayoutTopicExampleCard("自定义布局: 交错网格") {
        Text("一个更复杂的例子，实现一个交错网格布局，子项会填充到最短的列中。")
        Spacer(Modifier.height(16.dp))
        // 准备一些不同主题的文本项
        val topics = listOf(
            "艺术 & 设计", "时尚", "电影", "游戏", "美食",
            "科技", "音乐", "绘画", "旅行"
        )
        // 将它们排列在两行中
        Row(modifier = Modifier.padding(8.dp)) {
            MyStaggeredGrid(modifier = Modifier.padding(end = 8.dp), rows = 3) {
                for (topic in topics) {
                    Chip(text = topic)
                }
            }
        }
    }
}

@Composable
fun MyStaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 1. 测量 (Measure)
        // 记录每行的宽度
        val rowWidths = IntArray(rows) { 0 }
        // 记录每行的高度
        val rowHeights = IntArray(rows) { 0 }

        // 不要用相同的约束条件测量子项两次
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            // 将子项分配到最短的行
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // 网格的宽度是所有行中最宽的一行
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // 网格的高度是所有行高度的总和
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // 每行的 X 坐标
        val rowX = IntArray(rows) { 0 }
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        // 2. 布局 (Layout)
        layout(width, height) {
            val rowXOffsets = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowXOffsets[row],
                    y = rowY[row]
                )
                rowXOffsets[row] += placeable.width
            }
        }
    }
}


// 示例3：自定义布局修饰符
@Composable
fun CustomModifierExample() {
    LayoutTopicExampleCard("自定义布局修饰符") {
        Text("使用 `layout` 修饰符可以创建自定义的布局行为。例如，下面的修饰符可以根据文本的第一条基线来设置其顶部内边距。")
        Spacer(Modifier.height(16.dp))
        Text("应用了自定义修饰符的文本", Modifier.firstBaselineToTop(32.dp))
        Text("未应用修饰符的文本")
    }
}

@SuppressLint("SuspiciousModifierThen")
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        // 测量可组合项
        val placeable = measurable.measure(constraints)

        // 检查可组合项是否有第一条基线
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        // 可组合项的高度，减去基线位置，再加上所需的顶部距离
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // 放置可组合项
            placeable.placeRelative(0, placeableY)
        }
    }
)

// 用于演示的 Chip 可组合项
@Composable
private fun Chip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
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