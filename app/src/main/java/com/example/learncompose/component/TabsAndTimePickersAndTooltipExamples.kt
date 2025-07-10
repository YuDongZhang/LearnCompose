package com.example.learncompose.component

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.LocalTime

import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerDefaults

import androidx.compose.material3.rememberTimePickerState
//import androidx.compose.material3.TimePickerDialog   //需要升级到 Material3  1.4.0


/**
 * 官方文档：
 * - Tabs: https://developer.android.com/develop/ui/compose/components/tabs?hl=zh-cn
 * - TimePicker: https://developer.android.com/develop/ui/compose/components/time-pickers?hl=zh-cn
 * - Tooltip: https://developer.android.com/develop/ui/compose/components/tooltip?hl=zh-cn
 * 本文件演示 Compose TabRow、TimePicker、Tooltip 的典型用法。
 */

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsAndTimePickersAndTooltipExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("TabRow（标签页）", style = MaterialTheme.typography.titleMedium)
        SimpleTabRow()
        Text("TimePicker（时间选择器）", style = MaterialTheme.typography.titleMedium)
        SimpleTimePicker()
        Text("Tooltip（工具提示）", style = MaterialTheme.typography.titleMedium)
        SimpleTooltip()
    }
}

// region TabRow 示例
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTabRow() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("歌曲", "专辑", "播放列表")
    
    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("当前选中: ${tabs[selectedTabIndex]}")
    }
}
// endregion

// region TimePicker 示例
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTimePicker() {
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime.hour,
        initialMinute = selectedTime.minute
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showTimePicker = true }) {
            Text("选择时间")
        }
        Text("当前时间: ${selectedTime.hour}:${selectedTime.minute}")

        if (showTimePicker) {
//            TimePickerDialog(
//                onDismissRequest = { showTimePicker = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        showTimePicker = false
//                        selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
//                    }) {
//                        Text("确定")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showTimePicker = false }) {
//                        Text("取消")
//                    }
//                }
//            ) {
//                TimePicker(
//                    state = timePickerState,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
        }
    }
}
// endregion

// region Tooltip 示例
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTooltip() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text("这是一个工具提示")
                }
            },
            state = rememberTooltipState()
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Info, contentDescription = "信息")
            }
        }
        
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
                    title = { Text("富文本工具提示") },
                    action = {
                        TextButton(onClick = {}) {
                            Text("操作")
                        }
                    }
                ) {
                    Text("这是一个富文本工具提示，包含标题和操作按钮")
                }
            },
            state = rememberTooltipState()
        ) {
            Button(onClick = {}) {
                Text("悬停查看工具提示")
            }
        }
    }
}
// endregion 