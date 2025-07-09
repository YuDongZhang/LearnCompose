package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type

/**
 * Compose 键盘输入与快捷键处理示例合集
 * 参考：https://developer.android.com/develop/ui/compose/touch-input/keyboard-input/commands
 */
@Composable
fun KeyboardInputExamplesScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "1. 监听 S 键释放事件 (onKeyEvent)",
            fontSize = 18.sp
        )
        SKeyUpExample()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "2. 监听 S 键且无修饰键 (onKeyEvent)",
            fontSize = 18.sp
        )
        SKeyUpNoModifierExample()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "3. 监听 Tab 键切换焦点 (onPreviewKeyEvent)",
            fontSize = 18.sp
        )
        TabKeyFocusExample()
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "4. 事件传递与拦截 (onKeyEvent/onPreviewKeyEvent)",
            fontSize = 18.sp
        )
        KeyEventPropagationExample()
    }
}

/**
 * 示例1：监听 S 键释放事件
 * 只要 S 键释放就触发
 */
@Composable
fun SKeyUpExample() {
    var message by remember { mutableStateOf("请按 S 键") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFB3E5FC))
            .focusable()
            .onKeyEvent {
                if (it.type == KeyEventType.KeyUp && it.key == Key.S) {
                    message = "S 键释放!"
                    true
                } else {
                    false
                }
            },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(message)
    }
    Text(
        text = "说明：聚焦蓝色区域，按 S 键松开时触发。",
        fontSize = 13.sp,
        color = Color.Gray
    )
}

/**
 * 示例2：监听 S 键且无修饰键
 * 只有没有 Alt/Ctrl/Meta/Shift 时才触发
 */
@Composable
fun SKeyUpNoModifierExample() {
    var message by remember { mutableStateOf("请按 S 键 (无修饰)") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFC8E6C9))
            .focusable()
            .onKeyEvent {
                if (
                    it.type == KeyEventType.KeyUp &&
                    it.key == Key.S &&
                    !it.isAltPressed &&
                    !it.isCtrlPressed &&
                    !it.isMetaPressed &&
                    !it.isShiftPressed
                ) {
                    message = "S 键释放(无修饰)!"
                    true
                } else {
                    false
                }
            },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(message)
    }
    Text(
        text = "说明：聚焦绿色区域，只有 S 键无修饰时触发。",
        fontSize = 13.sp,
        color = Color.Gray
    )
}

/**
 * 示例3：Tab 键切换焦点 (onPreviewKeyEvent)
 * 在 TextField 里按 Tab，切换到下一个焦点
 */
@Composable
fun TabKeyFocusExample() {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(TextFieldValue()) }
    BasicTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF9C4))
            .onPreviewKeyEvent {
                if (it.type == KeyEventType.KeyUp && it.key == Key.Tab) {
                    focusManager.moveFocus(FocusDirection.Next)
                    true
                } else {
                    false
                }
            }
    )
    Text(
        text = "说明：在输入框按 Tab，会切换到下一个焦点而不是输入制表符。",
        fontSize = 13.sp,
        color = Color.Gray
    )
}

/**
 * 示例4：事件传递与拦截
 * 内部组件消费 S 键，外部组件消费 D 键
 */
@Composable
fun KeyEventPropagationExample() {
    var outerMsg by remember { mutableStateOf("外部区域：等待 D 键") }
    var innerMsg by remember { mutableStateOf("内部区域：等待 S 键") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFD1C4E9))
            .onKeyEvent {
                when {
                    it.type == KeyEventType.KeyUp && it.key == Key.S -> {
                        outerMsg = "外部区域：S 键被内部消费，不会触发"; true
                    }
                    it.type == KeyEventType.KeyUp && it.key == Key.D -> {
                        outerMsg = "外部区域：D 键释放!"; true
                    }
                    else -> false
                }
            },
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp)
                .background(Color(0xFFB39DDB))
                .focusable()
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyUp && it.key == Key.S) {
                        innerMsg = "内部区域：S 键释放!"
                        true
                    } else {
                        false
                    }
                },
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(innerMsg)
        }
    }
    Text(
        text = "说明：聚焦紫色区域，S 键只被内部消费，D 键被外部消费。",
        fontSize = 13.sp,
        color = Color.Gray
    )
} 