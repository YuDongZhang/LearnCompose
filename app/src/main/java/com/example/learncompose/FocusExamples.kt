package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Compose 焦点管理示例合集
 * 参考官方文档：
 * - 更改焦点遍历顺序
 * - 更改焦点行为
 * - 回应焦点
 * https://developer.android.com/develop/ui/compose/touch-input/focus
 */
@Composable
fun FocusExamplesScreen() {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Text("1. 更改焦点遍历顺序", fontSize = 18.sp)
        FocusTraversalOrderExample()
        Text("2. 更改焦点行为", fontSize = 18.sp)
        FocusBehaviorExample()
        Text("3. 回应焦点变化", fontSize = 18.sp)
        FocusResponseExample()
    }
}

/**
 * 示例1：更改焦点遍历顺序
 * 通过 focusOrder/focusRequester 控制 Tab/方向键的焦点顺序
 */
@Composable
fun FocusTraversalOrderExample() {
    val (first, second, third) = remember { FocusRequester.createRefs() }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { second.requestFocus() },
            modifier = Modifier.focusRequester(first).focusOrder(first) { next = second }
        ) { Text("按钮1 (下一个:2)") }
        Button(
            onClick = { third.requestFocus() },
            modifier = Modifier.focusRequester(second).focusOrder(second) { next = third; previous = first }
        ) { Text("按钮2 (下一个:3)") }
        Button(
            onClick = { first.requestFocus() },
            modifier = Modifier.focusRequester(third).focusOrder(third) { next = first; previous = second }
        ) { Text("按钮3 (下一个:1)") }
    }
    Text(
        "说明：Tab 或方向键切换焦点时，顺序为 1→2→3→1。点击按钮也可跳转焦点。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例2：更改焦点行为
 * 展示如何请求、捕获和释放焦点
 */
@Composable
fun FocusBehaviorExample() {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .background(Color(0xFFFFF9C4))
                .focusRequester(focusRequester)
                .focusable()
                .size(120.dp, 40.dp)
        )
        Button(onClick = { focusRequester.requestFocus() }) { Text("请求焦点") }
        Button(onClick = { focusManager.clearFocus() }) { Text("释放焦点") }
    }
    Text(
        "说明：点击“请求焦点”输入框获取焦点，点击“释放焦点”输入框失去焦点。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例3：回应焦点变化
 * 通过 onFocusChanged 响应焦点变化并高亮显示
 */
@Composable
fun FocusResponseExample() {
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = if (isFocused) "已获得焦点" else "未获得焦点",
        onValueChange = {},
        modifier = Modifier
            .background(if (isFocused) Color(0xFFB2DFDB) else Color(0xFFEEEEEE))
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .size(180.dp, 40.dp)
    )
    Text(
        "说明：输入框获得焦点时变色并显示提示。",
        fontSize = 13.sp, color = Color.Gray
    )
} 