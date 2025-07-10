package com.example.learncompose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/components/menu?hl=zh-cn
 * Menu 用于显示一组选项。Compose 提供 DropdownMenu、ExposedDropdownMenuBox 等。
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("DropdownMenu 示例", style = MaterialTheme.typography.titleMedium)
        DropdownMenuExample()
        Spacer(Modifier.height(16.dp))
        Text("ExposedDropdownMenuBox 示例", style = MaterialTheme.typography.titleMedium)
        ExposedDropdownMenuBoxExample()
    }
}

@Composable
fun DropdownMenuExample() {
    var expanded by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "更多")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { Text("选项 1") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text("选项 2") }, onClick = { expanded = false })
            DropdownMenuItem(text = { Text("选项 3") }, onClick = { expanded = false })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxExample() {
    val options = listOf("苹果", "香蕉", "橙子")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOptionText,
            onValueChange = {},
            readOnly = true,
            label = { Text("选择水果") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
} 