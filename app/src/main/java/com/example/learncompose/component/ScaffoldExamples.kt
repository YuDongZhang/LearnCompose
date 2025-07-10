package com.example.learncompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/components/scaffold?hl=zh-cn
 * 本文件演示 Compose Scaffold 的典型用法，包含顶部应用栏、底部应用栏、悬浮操作按钮等。
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Scaffold（脚手架）", style = MaterialTheme.typography.titleMedium)
        SimpleScaffold()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScaffold() {
    var presses by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("顶部应用栏")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "底部应用栏",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { presses++ }) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = """
                    这是一个 Scaffold 的示例。它使用 Scaffold 可组合项的参数来创建一个包含简单顶部应用栏、底部应用栏和悬浮操作按钮的屏幕。

                    它还包含一些基本的内容，比如这段文本。

                    你已经按了悬浮操作按钮 $presses 次。
                """.trimIndent(),
            )
        }
    }
} 