package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.layout.alignBy


@Composable
fun AlignmentLinesExample() {
    LayoutTopicExampleCard("AlignmentLines: 对齐线") {
        Text(
            "Compose 允许您创建自定义对齐线，以帮助父布局决定如何对齐和放置其子项，" +
                    "这对于创建精确的、非标准的对齐方式非常有用。"
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "下面的例子创建了一个自定义的对齐线 `MidY`，它位于 Composable 的垂直中心。" +
                    "然后，`Row` 使用这个对齐线来对齐其子项。"
        )
        Spacer(Modifier.height(16.dp))

        // 定义一个自定义的垂直对齐线
        val midY = VerticalAlignmentLine(merger = { old, new -> Math.min(old, new) })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .padding(8.dp),

//            verticalAlignment = Alignment.Vertical.alignBy(midY) // 1. 让 Row 使用自定义对齐线
        ) {
            Text(
                text = "Height 40dp",
                modifier = Modifier
                    .height(40.dp)
                    .background(Color.LightGray)
                    .padding(4.dp)
                    .verticalCenterLayout(midY) // 2. 让 Text 提供对齐线
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Height 60dp",
                modifier = Modifier
                    .height(60.dp)
                    .background(Color.LightGray)
                    .padding(4.dp)
                    .verticalCenterLayout(midY) // 2. 让 Text 提供对齐线
            )
        }
    }
}

@Composable
fun LayoutTopicExampleCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

/**
 * 一个自定义修饰符，用于计算组件的垂直中心并将其作为指定的 AlignmentLine 提供。
 */
private fun Modifier.verticalCenterLayout(alignmentLine: AlignmentLine) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        // 将对齐线的位置设置为高度的一半
        val lineY = placeable.height / 2
        layout(placeable.width, placeable.height, mapOf(alignmentLine to lineY)) {
            placeable.placeRelative(0, 0)
        }
    }