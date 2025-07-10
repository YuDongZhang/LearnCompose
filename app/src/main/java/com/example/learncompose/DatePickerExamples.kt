package com.example.learncompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Compose Material3 DatePicker 相关示例合集
 * 参考官方文档：
 * https://developer.android.com/develop/ui/compose/components/datepickers?hl=zh-cn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerExamplesScreen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text("1. 单日期选择器", fontSize = 18.sp)
        SingleDatePickerExample()
        Text("2. 范围日期选择器", fontSize = 18.sp)
        DateRangePickerExample()
        Text("3. 单日期选择对话框", fontSize = 18.sp)
        DatePickerDialogExample()
        Text("4. 范围日期选择对话框", fontSize = 18.sp)
        DateRangePickerDialogExample()
    }
}

/**
 * 示例1：单日期选择器
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleDatePickerExample() {
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    Column {
        DatePicker(state = datePickerState)
        Button(onClick = { selectedDate = datePickerState.selectedDateMillis }) {
            Text("确认选择")
        }
        if (selectedDate != null) {
            val date = Date(selectedDate!!)
            val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            Text("已选日期: $formatted")
        } else {
            Text("未选择日期")
        }
        Text("说明：直接在页面选择日期，点击确认按钮。", fontSize = 13.sp)
    }
}

/**
 * 示例2：范围日期选择器
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerExample() {
    val rangePickerState = rememberDateRangePickerState()
    var selectedRange by remember { mutableStateOf<Pair<Long?, Long?>?>(null) }
    Column {
        DateRangePicker(state = rangePickerState)
        Button(onClick = {
            selectedRange = Pair(rangePickerState.selectedStartDateMillis, rangePickerState.selectedEndDateMillis)
        }) {
            Text("确认选择")
        }
        if (selectedRange != null) {
            val (start, end) = selectedRange!!
            val startStr = start?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) } ?: "--"
            val endStr = end?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) } ?: "--"
            Text("已选范围: $startStr ~ $endStr")
        } else {
            Text("未选择日期范围")
        }
        Text("说明：选择日期范围后点击确认按钮。", fontSize = 13.sp)
    }
}

/**
 * 示例3：单日期选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    val dialogDatePickerState = rememberDatePickerState()
    Button(onClick = { showDialog = true }) { Text("打开日期选择对话框") }
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    selectedDate = dialogDatePickerState.selectedDateMillis
                    showDialog = false
                }) { Text("确定") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("取消") }
            }
        ) {

            DatePicker(state = dialogDatePickerState)
        }
    }
    if (selectedDate != null) {
        val date = Date(selectedDate!!)
        val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        Text("已选日期: $formatted")
    }
    Text("说明：弹出对话框选择日期。", fontSize = 13.sp)
}

/**
 * 示例4：范围日期选择对话框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedRange by remember { mutableStateOf<Pair<Long?, Long?>?>(null) }
    Button(onClick = { showDialog = true }) { Text("打开范围日期选择对话框") }
    if (showDialog) {
        DatePickerRangeDialog(
            onDateRangeSelected = {
                selectedRange = it
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
    if (selectedRange != null) {
        val (start, end) = selectedRange!!
        val startStr = start?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) } ?: "--"
        val endStr = end?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it)) } ?: "--"
        Text("已选范围: $startStr ~ $endStr")
    }
    Text("说明：弹出对话框选择日期范围。", fontSize = 13.sp)
}

/**
 * 范围日期选择对话框实现
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerRangeDialog(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                }
            ) { Text("确定") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("取消") }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
} 