package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Compose 指针输入与手势检测示例合集
 * 参考：
 * https://developer.android.com/develop/ui/compose/touch-input/pointer-input?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/pointer-input/understand-gestures?hl=zh-cn
 */
@Composable
fun TouchPointerInputExamplesScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "1. 基础点击手势 (detectTapGestures)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        TapGestureExample()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "2. 拖动手势 (detectDragGestures)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        DragGestureExample()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "3. 长按与双击 (detectTapGestures)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        LongPressAndDoubleTapExample()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * 示例1：基础点击手势
 * 使用 pointerInput + detectTapGestures 检测单击
 */
@Composable
fun TapGestureExample() {
    var tapCount by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color(0xFF90CAF9))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        tapCount++
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text("点击次数: $tapCount", color = Color.White)
    }
    Text(
        text = "说明：点击蓝色方块，计数会增加。detectTapGestures 是检测点击的推荐方式。",
        fontSize = 13.sp,
        color = Color.Gray
    )
}

/**
 * 示例2：拖动手势
 * 使用 pointerInput + detectDragGestures 检测拖动
 */
@Composable
fun DragGestureExample() {
    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color(0xFFA5D6A7))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "偏移: (%.0f, %.0f)".format(offset.x, offset.y),
            color = Color.White
        )
    }
    Text(
        text = "说明：按住绿色方块并拖动，显示当前偏移量。detectDragGestures 可用于自定义拖拽。",
        fontSize = 13.sp,
        color = Color.Gray
    )
}

/**
 * 示例3：长按与双击
 * 使用 detectTapGestures 检测长按和双击
 */
@Composable
fun LongPressAndDoubleTapExample() {
    var message by remember { mutableStateOf("等待操作...") }
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color(0xFFFFCC80))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        message = "长按触发!"
                    },
                    onDoubleTap = {
                        message = "双击触发!"
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = Color.Black)
    }
    Text(
        text = "说明：长按橙色方块显示'长按触发'，双击显示'双击触发'。",
        fontSize = 13.sp,
        color = Color.Gray
    )
} 