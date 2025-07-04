package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ComprehensiveModifiersExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ModifierOrderExample()
        CommonModifiersExample()
        ScopeSafeModifiersExample()
    }
}

@Composable
fun ModifierOrderExample() {
    LayoutTopicExampleCard("修饰符顺序 (Order Matters)") {
        Text("修饰符的调用顺序至关重要。由于每个函数都会更改前一个函数返回的修饰符，因此顺序会影响最终结果。")
        Spacer(Modifier.height(16.dp))

        Text("示例 1: 先 padding 后 background", fontWeight = FontWeight.Bold)
        Text(
            "Text with padding and background",
            modifier = Modifier
                .padding(16.dp) // 1. 在元素周围应用内边距
                .background(Color.Yellow) // 2. 将黄色背景应用到内边距内的区域
        )

        Spacer(Modifier.height(16.dp))

        Text("示例 2: 先 background 后 padding", fontWeight = FontWeight.Bold)
        Text(
            "Text with background and padding",
            modifier = Modifier
                .background(Color.Blue)
                // 1. 先将蓝色背景应用到整个元素
                .padding(16.dp)
                // 2. 在蓝色背景的外部应用内边距，将文本推离边缘
                .background(Color.Yellow)
                // 3. 再给内边距内的区域加上黄色背景
        )
    }
}

@Composable
fun CommonModifiersExample() {
    LayoutTopicExampleCard("常用修饰符") {
        Text("Compose 提供了大量开箱即用的修饰符来满足常见需求。")
        Spacer(Modifier.height(16.dp))

        var clickCount by remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 100.dp) // size: 指定首选尺寸
                .background(Color.LightGray)
        ) {
            Text(
                "点我! (次数: $clickCount)",
                modifier = Modifier
                    .offset(x = 20.dp, y = 10.dp) // offset: 将元素从其原始位置偏移
                    .border(1.dp, Color.Red)
                    .background(Color.Cyan)
                    .padding(8.dp)
                    .clickable { clickCount++ } // clickable: 使元素可点击
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ScopeSafeModifiersExample() {
    LayoutTopicExampleCard("作用域安全修饰符") {
        Text("某些修饰符只能在特定的布局组件（如 Column, Row, Box）的子级中使用。这可以确保修饰符只在有意义的地方使用。")
        Spacer(Modifier.height(16.dp))
        Text("例如，`weight` 修饰符只能在 `RowScope` 或 `ColumnScope` 的子项上使用，用于分配剩余空间。而 `align` 只能在 `BoxScope` 中使用。")
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(
                "占 1 份",
                modifier = Modifier
                    .weight(1f) // weight: 分配剩余宽度的 1/3
                    .background(Color.Red)
                    .padding(4.dp)
            )
            Text(
                "占 2 份",
                modifier = Modifier
                    .weight(2f) // weight: 分配剩余宽度的 2/3
                    .background(Color.Green)
                    .padding(4.dp)
            )
        }
    }
}
