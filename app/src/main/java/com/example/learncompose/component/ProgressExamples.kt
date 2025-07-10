package com.example.learncompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/components/progress?hl=zh-cn
 * 本文件演示 Compose 进度指示器（LinearProgressIndicator、CircularProgressIndicator）常见用法。
 */

@Composable
fun ProgressExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("LinearProgressIndicator（线性进度条）", style = MaterialTheme.typography.titleMedium)
        LinearDeterminateExample()
        LinearIndeterminateExample()
        Text("CircularProgressIndicator（圆形进度条）", style = MaterialTheme.typography.titleMedium)
        CircularDeterminateExample()
        CircularIndeterminateExample()
    }
}

// region LinearProgressIndicator
@Composable
fun LinearDeterminateExample() {
    var progress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            loading = true
            scope.launch {
                for (i in 1..100) {
                    progress = i / 100f
                    delay(20)
                }
                loading = false
            }
        }, enabled = !loading) {
            Text("开始加载（确定型）")
        }
        if (loading) {
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun LinearIndeterminateExample() {
    var loading by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { loading = true }, enabled = !loading) {
            Text("开始加载（不确定型）")
        }
        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
// endregion

// region CircularProgressIndicator
@Composable
fun CircularDeterminateExample() {
    var progress by remember { mutableStateOf(0f) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            loading = true
            scope.launch {
                for (i in 1..100) {
                    progress = i / 100f
                    delay(20)
                }
                loading = false
            }
        }, enabled = !loading) {
            Text("开始加载（确定型）")
        }
        if (loading) {
            CircularProgressIndicator(progress = { progress }, modifier = Modifier.size(48.dp))
        }
    }
}

@Composable
fun CircularIndeterminateExample() {
    var loading by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { loading = true }, enabled = !loading) {
            Text("开始加载（不确定型）")
        }
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
// endregion 