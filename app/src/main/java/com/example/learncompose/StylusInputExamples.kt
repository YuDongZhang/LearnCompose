package com.example.learncompose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Compose 触控笔输入相关示例合集
 * 参考官方文档：
 * - 触控笔输入简介
 * - 文本字段中的手写输入
 * - 高级触控笔功能（压力、倾斜、悬停等）
 * - Ink API 简介
 * https://developer.android.com/develop/ui/compose/touch-input/stylus-input?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/stylus-input/stylus-input-in-text-fields?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/stylus-input/advanced-stylus-features?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/stylus-input/about-ink-api?hl=zh-cn
 */
@Composable
fun StylusInputExamplesScreen() {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text("1. 触控笔基础绘图", fontSize = 18.sp)
        StylusBasicDrawingExample()
        Text("2. 文本字段中的手写输入", fontSize = 18.sp)
        StylusTextFieldExample()
        Text("3. 高级触控笔特性（压力/倾斜/悬停）", fontSize = 18.sp)
        StylusAdvancedFeaturesExample()
        Text("4. Ink API 简介（占位说明）", fontSize = 18.sp)
        InkApiInfoExample()
    }
}

/**
 * 示例1：触控笔基础绘图
 * 支持用触控笔/手指在画布上绘制路径
 */
@Composable
fun StylusBasicDrawingExample() {
    var paths by remember { mutableStateOf(listOf<List<Offset>>()) }
    var currentPath by remember { mutableStateOf(listOf<Offset>()) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFFF5F5F5))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        currentPath = listOf(offset)
                    },
                    onDrag = { change, dragAmount ->
                        currentPath = currentPath + change.position
                    },
                    onDragEnd = {
                        if (currentPath.isNotEmpty()) {
                            paths = paths + listOf(currentPath)
                            currentPath = listOf()
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 绘制所有历史路径
            for (path in paths) {
                for (i in 1 until path.size) {
                    drawLine(
                        color = Color.Blue,
                        start = path[i - 1],
                        end = path[i],
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                }
            }
            // 绘制当前路径
            for (i in 1 until currentPath.size) {
                drawLine(
                    color = Color.Red,
                    start = currentPath[i - 1],
                    end = currentPath[i],
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
        if (paths.isEmpty() && currentPath.isEmpty()) {
            Text("请用触控笔或手指绘制", color = Color.Gray)
        }
    }
    Button(onClick = { paths = listOf(); currentPath = listOf() }, modifier = Modifier.fillMaxWidth()
        .wrapContentWidth(Alignment.End)) {
        Text("清空")
    }
    Text(
        "说明：支持用触控笔/手指在灰色区域绘图。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例2：文本字段中的手写输入
 * 只要设备支持，系统会自动弹出手写面板
 */
@Composable
fun StylusTextFieldExample() {
    var text by remember { mutableStateOf(TextFieldValue()) }
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9C4))
            .padding(8.dp)
    )
    Text(
        "说明：在支持的设备上，聚焦输入框可用触控笔手写输入。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例3：高级触控笔特性（压力/倾斜/悬停）
 * 通过 pointerInteropFilter 获取原始 MotionEvent 信息
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StylusAdvancedFeaturesExample() {
    var info by remember { mutableStateOf("请用触控笔悬停/按压/倾斜") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFFE1BEE7))
            .pointerInteropFilter { event ->
                // 只处理触控笔事件
                if (event.getToolType(0) == android.view.MotionEvent.TOOL_TYPE_STYLUS) {
                    val pressure = event.pressure
                    val tilt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) event.getAxisValue(android.view.MotionEvent.AXIS_TILT) else 0f
                    val orientation = event.orientation
                    val action = when (event.actionMasked) {
                        android.view.MotionEvent.ACTION_HOVER_ENTER -> "悬停进入"
                        android.view.MotionEvent.ACTION_HOVER_EXIT -> "悬停离开"
                        android.view.MotionEvent.ACTION_DOWN -> "按下"
                        android.view.MotionEvent.ACTION_UP -> "抬起"
                        else -> "其他"
                    }
                    info = "压力: %.2f, 倾斜: %.2f, 方向: %.2f, 动作: %s".format(pressure, tilt, orientation, action)
                }
                false
            },
        contentAlignment = Alignment.Center
    ) {
        Text(info, color = Color.Black)
    }
    Text(
        "说明：在紫色区域用触控笔悬停、按压、倾斜，显示原始信息。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例4：Ink API 简介（占位说明）
 * Ink API 目前 Compose 仅有原生 Android 支持，Compose 需后续支持
 */
@Composable
fun InkApiInfoExample() {
    Text(
        "Ink API 允许更高级的手写/绘图体验，目前 Compose 仅部分支持，详见官方文档。",
        fontSize = 15.sp, color = Color.DarkGray
    )
    Text(
        "参考：https://developer.android.com/develop/ui/compose/touch-input/stylus-input/about-ink-api?hl=zh-cn",
        fontSize = 13.sp, color = Color.Gray
    )
} 