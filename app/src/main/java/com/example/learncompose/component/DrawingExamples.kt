package com.example.learncompose.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

@Composable
internal fun DrawingExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. 基础 Canvas 示例
        ComponentExampleCard(title = "Canvas 绘图") {
            Text("使用 Canvas Composable 绘制自定义图形。")
            Canvas(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                onDraw = {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    drawCircle(
                        color = Color.Blue,
                        center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                        radius = size.minDimension / 2
                    )
                }
            )
        }

        // 2. drawBehind 修饰符示例
        ComponentExampleCard(title = "drawBehind 修饰符") {
            Text("使用 drawBehind 修饰符在组件内容后面进行绘制。")
            Text(
                text = "带背景的文本",
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFB2EBF2), // Light Cyan
                            size = size,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                        )
                    }
                    .padding(16.dp)
            )
        }

        // 3. 饼图示例
        ComponentExampleCard(title = "饼图示例") {
            Text("一个使用 drawArc 绘制的简单饼图。")
            val pieData = mapOf("红" to 40f, "蓝" to 30f, "绿" to 20f, "黄" to 10f)
            val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow)
            val total = pieData.values.sum()
            var startAngle = 0f

            Canvas(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                pieData.values.forEachIndexed { index, value ->
                    val sweepAngle = (value / total) * 360f
                    drawArc(color = colors[index], startAngle = startAngle, sweepAngle = sweepAngle, useCenter = true)
                    startAngle += sweepAngle
                }
            }
        }
    }
}