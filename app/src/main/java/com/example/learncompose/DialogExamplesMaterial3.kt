package com.example.learncompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.learncompose.R

/**
 * Compose Material3 Dialog 相关示例合集
 * 参考官方文档：
 * https://developer.android.com/develop/ui/compose/components/dialog?hl=zh-cn
 */
@Composable
fun DialogExamplesMaterial3Screen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text("1. AlertDialog 警告对话框", fontSize = 18.sp)
        AlertDialogExample()
        Text("2. 最小化对话框", fontSize = 18.sp)
        MinimalDialogExample()
        Text("3. 自定义内容对话框（带图片和按钮）", fontSize = 18.sp)
        CustomDialogWithImageExample()
    }
}

/**
 * 示例1：AlertDialog 警告对话框
 */
@Composable
fun AlertDialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) { Text("显示 AlertDialog") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("标题") },
            text = { Text("这是一个警告对话框。你确定要继续吗？") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("取消") }
            }
        )
    }
    Text("说明：AlertDialog 用于显示重要提示或确认操作。", fontSize = 13.sp)
}

/**
 * 示例2：最小化对话框
 * 只包含标签内容
 */
@Composable
fun MinimalDialogExample() {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) { Text("显示最小化对话框") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("最小化对话框") },
            text = null,
            confirmButton = {},
            dismissButton = {}
        )
    }
    Text("说明：最小化对话框只包含标题，无按钮和内容。", fontSize = 13.sp)
}

/**
 * 示例3：自定义内容对话框（带图片和按钮）
 * 使用 Dialog + Card + 自定义内容
 */
@Composable
fun CustomDialogWithImageExample() {
    var showDialog by remember { mutableStateOf(false) }
    Button(onClick = { showDialog = true }) { Text("显示自定义对话框") }
    if (showDialog) {
        DialogWithImage(
            onDismissRequest = { showDialog = false },
            onConfirmation = { showDialog = false },
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            imageDescription = "示例图片"
        )
    }
    Text("说明：自定义对话框可放置图片、表单等复杂内容。", fontSize = 13.sp)
}

/**
 * 自定义内容对话框实现
 */
@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(160.dp)
                )
                Text(
                    text = "这是一个带图片和按钮的自定义对话框。",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("关闭")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("确认")
                    }
                }
            }
        }
    }
} 