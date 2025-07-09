package com.example.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class TouchInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TouchInputScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TouchInputScreen() {
    var showPointerExamples by remember { mutableStateOf(false) }
    var showKeyboardExamples by remember { mutableStateOf(false) }
    var showFocusExamples by remember { mutableStateOf(false) }
    var showStylusExamples by remember { mutableStateOf(false) }
    var showCopyPasteExamples by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Compose 触摸输入演示") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            when {
                showCopyPasteExamples -> {
                    Button(
                        onClick = { showCopyPasteExamples = false },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("返回") }
                    CopyPasteExamplesScreen()
                }
                showStylusExamples -> {
                    Button(
                        onClick = { showStylusExamples = false },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("返回") }
                    StylusInputExamplesScreen()
                }
                showFocusExamples -> {
                    Button(
                        onClick = { showFocusExamples = false },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("返回") }
                    FocusExamplesScreen()
                }
                showKeyboardExamples -> {
                    Button(
                        onClick = { showKeyboardExamples = false },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("返回") }
                    KeyboardInputExamplesScreen()
                }
                showPointerExamples -> {
                    Button(
                        onClick = { showPointerExamples = false },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("返回") }
                    TouchPointerInputExamplesScreen()
                }
                else -> {
                    Text(
                        text = "这里将展示触摸输入相关的 Compose 示例。",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showPointerExamples = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("指针输入与手势检测示例")
                    }
                    Button(
                        onClick = { showKeyboardExamples = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("键盘输入与快捷键示例")
                    }
                    Button(
                        onClick = { showFocusExamples = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("焦点管理示例")
                    }
                    Button(
                        onClick = { showStylusExamples = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("触控笔输入示例")
                    }
                    Button(
                        onClick = { showCopyPasteExamples = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("复制粘贴示例")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTouchInputScreen() {
    TouchInputScreen()
} 