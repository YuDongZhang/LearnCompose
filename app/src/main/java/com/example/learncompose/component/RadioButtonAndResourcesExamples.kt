package com.example.learncompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.learncompose.R

/**
 * 官方文档：
 * - RadioButton: https://developer.android.com/develop/ui/compose/components/radio-button?hl=zh-cn
 * - Resources: https://developer.android.com/develop/ui/compose/resources?hl=zh-cn
 * 本文件演示 Compose 单选按钮和资源访问的典型用法。
 */

@Composable
fun RadioButtonAndResourcesExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("RadioButton（单选按钮）", style = MaterialTheme.typography.titleMedium)
        SimpleRadioButton()
        Text("Resources（资源访问）", style = MaterialTheme.typography.titleMedium)
        ResourcesExample()
    }
}

// region RadioButton 示例
@Composable
fun SimpleRadioButton() {
    val radioOptions = listOf("通话", "未接来电", "好友")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { selectedOption = text },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null // null recommended for accessibility with screen readers
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
// endregion

// region Resources 示例
@Composable
fun ResourcesExample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 字符串资源示例
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.bodyLarge
        )
        
        // 颜色资源示例（如果存在）
        Text(
            text = "颜色资源示例",
            color = Color(0xFF6200EE), // 使用硬编码颜色作为示例
            style = MaterialTheme.typography.bodyLarge
        )
        
        // 尺寸资源示例
        Text(
            text = "尺寸资源示例",
            fontSize = 18.sp, // 使用硬编码尺寸作为示例
            style = MaterialTheme.typography.bodyLarge
        )
        
        // 图片资源示例（如果存在）
        // Image(
        //     painter = painterResource(R.drawable.ic_launcher_foreground),
        //     contentDescription = stringResource(R.string.app_name),
        //     modifier = Modifier.size(48.dp)
        // )
    }
}
// endregion 