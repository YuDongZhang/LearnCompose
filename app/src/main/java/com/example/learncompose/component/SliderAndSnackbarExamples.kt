package com.example.learncompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 官方文档：
 * - Slider: https://developer.android.com/develop/ui/compose/components/slider?hl=zh-cn
 * - Snackbar: https://developer.android.com/develop/ui/compose/components/snackbar?hl=zh-cn
 * 本文件演示 Compose Slider、RangeSlider 和 Snackbar 的典型用法。
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderAndSnackbarExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Slider（滑块）", style = MaterialTheme.typography.titleMedium)
        SimpleSlider()
        Text("RangeSlider（范围滑块）", style = MaterialTheme.typography.titleMedium)
        RangeSliderExample()
        Text("Snackbar（快捷信息栏）", style = MaterialTheme.typography.titleMedium)
        SnackbarExample()
    }
}

// region Slider 示例
@Composable
fun SimpleSlider() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 3,
            valueRange = 0f..50f
        )
        Text(text = "当前值: ${sliderPosition.toInt()}")
    }
}

@Composable
fun RangeSliderExample() {
    var sliderPosition by remember { mutableStateOf(0f..100f) }
    Column {
        RangeSlider(
            value = sliderPosition,
            steps = 5,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                // 在这里可以启动一些业务逻辑更新
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
        )
        Text(text = "范围: ${sliderPosition.start.toInt()} - ${sliderPosition.endInclusive.toInt()}")
    }
}
// endregion

// region Snackbar 示例
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackbarExample() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("显示 Snackbar") },
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "") },
                onClick = {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "这是一个 Snackbar 消息",
                            actionLabel = "操作",
                            duration = SnackbarDuration.Indefinite
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                // 处理 Snackbar 操作执行
                            }
                            SnackbarResult.Dismissed -> {
                                // 处理 Snackbar 被关闭
                            }
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("调整滑块值来触发 Snackbar")
            Slider(
                value = sliderPosition,
                onValueChange = { 
                    sliderPosition = it
                    scope.launch {
                        snackbarHostState.showSnackbar("滑块值: ${it.toInt()}")
                    }
                },
                valueRange = 0f..100f
            )
            Text("当前滑块值: ${sliderPosition.toInt()}")
        }
    }
}
// endregion 