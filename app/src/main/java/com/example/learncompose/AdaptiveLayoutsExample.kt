package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveLayoutsExample() {
    LayoutTopicExampleCard("自适应布局 (Adaptive Layouts)") {
        Text(
            "自适应布局是指能够根据可用空间（如屏幕大小、方向或窗口大小）自动调整其外观和行为的布局。\n\n" +
                    "在 Compose 中，`BoxWithConstraints` 是一个非常有用的可组合项，它能提供其子项可以填充的最大和最小宽度/高度。我们可以利用这些信息来动态改变布局。"
        )
        Spacer(Modifier.height(16.dp))
        Text("下面的示例演示了一个布局：当可用宽度大于 400.dp 时，它显示为一行 (Row)；否则，它显示为一列 (Column)。")
        Spacer(Modifier.height(16.dp))

        // 为了演示，我们限制外部容器的大小
        // 在实际应用中，这可能是整个屏幕或某个可调整大小的窗口
        Column {
            Text("较窄的容器 (宽度 < 400.dp):")
            Box(modifier = Modifier.width(350.dp)) {
                AdaptiveContent()
            }
            Spacer(Modifier.height(24.dp))
            Text("较宽的容器 (宽度 > 400.dp):")
            Box(modifier = Modifier.width(450.dp)) {
                AdaptiveContent()
            }
        }
    }
}

@Composable
private fun AdaptiveContent() {
    // BoxWithConstraints 提供了父布局的约束条件
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        // 我们可以访问 constraints.maxWidth, constraints.minWidth 等

        // 根据最大可用宽度决定使用 Row 还是 Column
        if (maxWidth > 400.dp) {
            // 宽度足够时，使用 Row 布局
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ContentPart(text = "部分 1", color = MaterialTheme.colorScheme.primaryContainer)
                ContentPart(text = "部分 2", color = MaterialTheme.colorScheme.secondaryContainer)
            }
        } else {
            // 宽度不足时，使用 Column 布局
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ContentPart(text = "部分 1", color = MaterialTheme.colorScheme.primaryContainer)
                ContentPart(text = "部分 2", color = MaterialTheme.colorScheme.secondaryContainer)
            }
        }
    }
}

@Composable
private fun ContentPart(text: String, color: Color) {
    Box(
        modifier = Modifier
            .height(80.dp)
            .width(150.dp)
            .background(color, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = MaterialTheme.colorScheme.onSurface)
    }
}