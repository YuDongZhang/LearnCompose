package com.example.learncompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape

/**
 * Compose Material3 Chip 相关示例合集
 * 参考官方文档：
 * https://developer.android.com/develop/ui/compose/components/chip?hl=zh-cn
 */
@Composable
fun ChipExamplesScreen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("1. AssistChip 辅助条状标签", fontSize = 18.sp)
        AssistChipExample()
        Text("2. FilterChip 过滤条状标签", fontSize = 18.sp)
        FilterChipExample()
        Text("3. InputChip 输入条状标签", fontSize = 18.sp)
        InputChipExample()
        Text("4. SuggestionChip 建议条状标签", fontSize = 18.sp)
        SuggestionChipExample()
        Text("5. Elevated*Chip 凸起样式", fontSize = 18.sp)
        ElevatedChipsExample()
    }
}

/**
 * 示例1：AssistChip 辅助条状标签
 */
@Composable
fun AssistChipExample() {
    AssistChip(
        onClick = { /* 处理点击 */ },
        label = { Text("Assist chip") }
    )
    Text("说明：用于提供辅助操作的条状标签。", fontSize = 13.sp, color = Color.Gray)
}

/**
 * 示例2：FilterChip 过滤条状标签
 */
@Composable
fun FilterChipExample() {
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        selected = selected,
        onClick = { selected = !selected },
        label = { Text("Filter chip") }
    )
    Text("说明：可选中/取消的过滤标签。", fontSize = 13.sp, color = Color.Gray)
}

/**
 * 示例3：InputChip 输入条状标签
 * 带头像和关闭图标
 */
@Composable
fun InputChipExample() {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return
    InputChip(
        onClick = { enabled = false },
        label = { Text("Input chip") },
        selected = enabled,
        avatar = {
            androidx.compose.material3.Icon(
                Icons.Filled.Person,
                contentDescription = "头像",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
        trailingIcon = {
            androidx.compose.material3.Icon(
                Icons.Filled.Close,
                contentDescription = "关闭",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
    )
    Text("说明：带头像和关闭按钮的输入标签。点击关闭。", fontSize = 13.sp, color = Color.Gray)
}

/**
 * 示例4：SuggestionChip 建议条状标签
 */
@Composable
fun SuggestionChipExample() {
    SuggestionChip(
        onClick = { /* 处理点击 */ },
        label = { Text("Suggestion chip") }
    )
    Text("说明：用于动态提示的建议标签。", fontSize = 13.sp, color = Color.Gray)
}

/**
 * 示例5：Elevated*Chip 凸起样式
 */
@Composable
fun ElevatedChipsExample() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ElevatedAssistChip(
            onClick = { },
            label = { Text("Elevated Assist") }
        )
        ElevatedFilterChip(
            selected = false,
            onClick = { },
            label = { Text("Elevated Filter") }
        )
        ElevatedSuggestionChip(
            onClick = { },
            label = { Text("Elevated Suggestion") }
        )
    }
    Text("说明：凸起样式的条状标签。", fontSize = 13.sp, color = Color.Gray)
} 