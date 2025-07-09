package com.example.learncompose

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * Compose 用户交互相关示例合集
 * 参考官方文档：
 * - Handling Interactions
 * - Indication & Ripple
 * - Drag and Drop
 * - Swipe to Dismiss
 * https://developer.android.com/develop/ui/compose/touch-input/user-interactions/handling-interactions?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/user-interactions/migrate-indication-ripple?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/user-interactions/drag-and-drop?hl=zh-cn
 * https://developer.android.com/develop/ui/compose/touch-input/user-interactions/swipe-to-dismiss?hl=zh-cn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInteractionsExamplesScreen() {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text("1. 处理交互状态 (Handling Interactions)", fontSize = 18.sp)
        HandlingInteractionsExample()
        Text("2. 自定义涟漪效果 (Indication & Ripple)", fontSize = 18.sp)
        IndicationRippleExample()
        Text("3. 拖放 (Drag and Drop)", fontSize = 18.sp)
        DragAndDropExample()
        Text("4. 滑动删除 (Swipe to Dismiss)", fontSize = 18.sp)
//        SwipeToDismissExample()
    }
}

/**
 * 示例1：处理交互状态
 * 使用 InteractionSource 监听点击、按下、悬停等交互状态
 */
@Composable
fun HandlingInteractionsExample() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(if (isPressed) Color(0xFF90CAF9) else Color(0xFFB3E5FC), RoundedCornerShape(12.dp))
            .indication(interactionSource, LocalIndication.current)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(if (isPressed) "按下中" else "未按下")
    }
    Text(
        "说明：点击蓝色方块，按下时变色。InteractionSource 可监听交互状态。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例2：自定义涟漪效果
 * 使用 indication 修饰符自定义点击涟漪效果
 */
@Composable
fun IndicationRippleExample() {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFA5D6A7))
//            .ripple(color = Color.Red, bounded = true) // 直接用 ripple 修饰符
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Text("自定义涟漪", color = Color.White)
    }
    Text(
        "说明：点击绿色方块，显示红色自定义涟漪。indication 可自定义点击反馈。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例3：拖放 (Drag and Drop)
 * 拖动方块到目标区域，演示拖放交互
 */
@Composable
fun DragAndDropExample() {
    var boxOffset by remember { mutableStateOf(0f) }
    val boxWidth = 100.dp
    val targetOffset = 200f
    val isDropped = boxOffset > targetOffset
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = boxOffset.dp)
                .size(boxWidth)
                .background(if (isDropped) Color(0xFFFFCC80) else Color(0xFF90CAF9), RoundedCornerShape(12.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        boxOffset = (boxOffset + dragAmount.x).coerceIn(0f, 300f)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(if (isDropped) "已放下" else "拖我", color = Color.Black)
        }
        // 目标区域
        Box(
            modifier = Modifier
                .offset(x = targetOffset.dp)
                .size(boxWidth)
                .background(Color(0xFFD1C4E9), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("目标", color = Color.White)
        }
    }
    Text(
        "说明：拖动蓝色方块到紫色目标区域，松手后变色。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例4：滑动删除 (Swipe to Dismiss)
 * 使用 SwipeToDismiss 实现滑动删除交互
 */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SwipeToDismissExample() {
//    var itemVisible by remember { mutableStateOf(true) }
//    val dismissState = rememberDismissState(
//        confirmValueChange = {
//            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
//                itemVisible = false
//            }
//            true
//        }
//    )
//    if (itemVisible) {
//        SwipeToDismiss(
//            state = dismissState,
//            background = {
//                val color = when (dismissState.dismissDirection) {
//                    DismissDirection.StartToEnd -> Color(0xFFA5D6A7)
//                    DismissDirection.EndToStart -> Color(0xFFFFCDD2)
//                    null -> Color.LightGray
//                }
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(color),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("滑动以删除", color = Color.White)
//                }
//            },
//            dismissContent = {
//                Box(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//                        .background(Color(0xFF90CAF9), RoundedCornerShape(12.dp)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("可滑动删除的项", color = Color.White)
//                }
//            }
//        )
//    } else {
//        Text("已删除！", color = Color.Gray)
//    }
//    Text(
//        "说明：左右滑动蓝色项可删除，删除后显示提示。SwipeToDismiss 提供滑动删除交互。",
//        fontSize = 13.sp, color = Color.Gray
//    )
//}