package com.example.mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect

class CounterActivity : ComponentActivity() {

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState by viewModel.viewState.collectAsState()

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Counter: ${viewState.counter}", style = MaterialTheme.typography.headlineLarge)

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { viewModel.processIntent(CounterIntent.Decrement) }) {
                        Text("Decrement")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { viewModel.processIntent(CounterIntent.Increment) }) {
                        Text("Increment")
                    }
                }
                
                if (viewState.isLoading) {
                    CircularProgressIndicator()
                }
                
                viewState.error?.let { error ->
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

//CounterActivity 使用 Compose 构建 UI。在 onCreate 中，我们订阅了 viewModel.viewState，并根据状态动态更新 UI。
// 当用户点击按钮时，会发送对应的意图给 ViewModel 进行处理。   整个流程总结 • CounterIntent 表示用户意图； •
// CounterViewState 表示 View 的状态； • CounterViewModel 接收意图，更新状态，然后通过 viewState 发布更新后的状态；
// • CounterActivity 订阅 viewState 并更新 UI。  这个简单的示例展示了 MVI 的核心流程，希望对你有帮助！
