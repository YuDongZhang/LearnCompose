package com.example.learncompose

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Compose 复制粘贴相关示例合集
 * 参考官方文档：
 * https://developer.android.com/develop/ui/compose/touch-input/copy-and-paste?hl=zh-cn
 */
@Composable
fun CopyPasteExamplesScreen() {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text("1. 基础文本复制粘贴", fontSize = 18.sp)
        BasicCopyPasteExample()
        Text("2. 敏感内容复制（API 33+）", fontSize = 18.sp)
        SensitiveCopyExample()
    }
}

/**
 * 示例1：基础文本复制粘贴
 * 使用 LocalClipboardManager 实现文本的复制与粘贴
 */
@Composable
fun BasicCopyPasteExample() {
    var text by remember { mutableStateOf(TextFieldValue()) }
    val clipboardManager = LocalClipboardManager.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .weight(1f)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            clipboardManager.setText(AnnotatedString(text.text))
        }) { Text("复制") }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            val clip = clipboardManager.getText()
            if (clip != null) text = TextFieldValue(clip.text)
        }) { Text("粘贴") }
    }
    Text(
        "说明：输入文本后点击复制/粘贴按钮即可。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 示例2：敏感内容复制（API 33+）
 * 复制密码等敏感内容时，避免系统弹出内容预览
 */
@Composable
fun SensitiveCopyExample() {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }
    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        BasicTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .background(Color(0xFFFFF9C4))
                .weight(1f)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            setSensitiveClip(context, password)
        }) { Text("复制密码") }
    }
    Text(
        "说明：API 33+ 复制密码时，系统不会弹出内容预览。",
        fontSize = 13.sp, color = Color.Gray
    )
}

/**
 * 设置敏感内容到剪贴板，避免内容预览（API 33+）
 */
fun setSensitiveClip(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Sensitive", text)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        clip.description.extras = PersistableBundle().apply {
            putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
        }
    } else {
        clip.description.extras = PersistableBundle().apply {
            putBoolean("android.content.extra.IS_SENSITIVE", true)
        }
    }
    clipboard.setPrimaryClip(clip)
} 