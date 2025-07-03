package com.example.learncompose

import android.os.Bundle
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learncompose.ui.theme.LearnComposeTheme

// 定义一个密封类来表示不同的屏幕，方便进行类型安全的导航
// 将 Screen 重命名为 ComposeTopic，使其名称更具体，避免与其他库或代码中的 "Screen" 冲突
sealed class ComposeTopic(val title: String) {
    object Home : ComposeTopic("Compose 知识点列表")
    object TextView : ComposeTopic("Text - 文本")
    object ButtonView : ComposeTopic("Button - 按钮")
    object LayoutView : ComposeTopic("Layout - 布局")
    object StateView : ComposeTopic("State - 状态管理")
    object TextFieldView : ComposeTopic("TextField - 输入框")
    object ImageView : ComposeTopic("Image - 图像")
    object Architecture : ComposeTopic("UI Architecture - 架构")
    object LayoutsBasics : ComposeTopic("Layouts - 基础")
}

class DMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent 是 Compose 的入口点，替代了 XML 布局
        setContent {
            LearnComposeTheme {
                ComposeTutorialApp()
            }
        }
    }
}

@Preview
@Composable
fun ComposeTutorialApp() {
    // remember 和 mutableStateOf 用于创建和记住一个状态。当状态改变时，UI 会自动更新（重组）。
    // 这里我们用它来管理当前显示的屏幕。
    var currentTopic by remember { mutableStateOf<ComposeTopic>(ComposeTopic.Home) }

    // 根据当前屏幕状态，显示不同的 Composable
    when (val topic = currentTopic) {
        is ComposeTopic.Home -> HomeScreen(onTopicClick = { newTopic ->
            currentTopic = newTopic
        })
        else -> DetailScreen(topic = topic, onBack = {
            currentTopic = ComposeTopic.Home
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onTopicClick: (ComposeTopic) -> Unit) {
    // 知识点列表
    val topics = listOf(
        ComposeTopic.TextView,
        ComposeTopic.ButtonView,
        ComposeTopic.LayoutView,
        ComposeTopic.StateView,
        ComposeTopic.TextFieldView,
        ComposeTopic.ImageView,
        ComposeTopic.Architecture,
        ComposeTopic.LayoutsBasics
    )

    // LocalContext.current 用于获取当前的 Context，这在需要启动 Activity 或访问系统服务时非常有用
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ComposeTopic.Home.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        // LazyColumn 用于显示可滚动的列表，性能更高，因为它只渲染屏幕上可见的项
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // 设置列表项之间的垂直间距
        ) {
            items(topics) { topic ->
                Button(
                    onClick = {
                        if (topic is ComposeTopic.Architecture) {
                            context.startActivity(Intent(context, ArchitectureActivity::class.java))
                        } else if (topic is ComposeTopic.LayoutsBasics) {
                            context.startActivity(Intent(context, LayoutsBasicsActivity::class.java))
                        } else {
                            onTopicClick(topic)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(topic.title, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(topic: ComposeTopic, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topic.title) },
                navigationIcon = {
                    // 返回按钮
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        // 根据不同的屏幕类型，显示对应的内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (topic) {
                is ComposeTopic.TextView -> TextExampleScreen()
                is ComposeTopic.ButtonView -> ButtonExampleScreen()
                is ComposeTopic.LayoutView -> LayoutExampleScreen()
                is ComposeTopic.StateView -> StateExampleScreen()
                is ComposeTopic.TextFieldView -> TextFieldExampleScreen()
                is ComposeTopic.ImageView -> ImageExampleScreen()
                else -> {} // Home, Architecture, LayoutsBasics screen 在此不处理
            }
        }
    }
}

@Composable
fun ExampleCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun TextExampleScreen() {
    ExampleCard("Text: 用于显示文本") {
        // 这是最基本的 Text Composable
        Text("这是一个简单的文本。")
        Spacer(modifier = Modifier.height(16.dp))
        // 你可以像这样自定义颜色、字体大小和字重
        Text(
            text = "这是自定义样式的文本。",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ImageExampleScreen() {
    ExampleCard("Image: 用于显示图像") {
        Text("Image Composable 用于显示图片资源，例如来自 drawable 或网络的图片。")
        Spacer(modifier = Modifier.height(16.dp))
        // 1. 使用 painterResource 从 drawable 加载图片
        // 2. contentDescription 对于无障碍服务非常重要，它描述了图像的内容
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Android Logo",
            modifier = Modifier.size(100.dp) // 设置图片大小
        )
    }
}

@Composable
fun ButtonExampleScreen() {
    ExampleCard("Button: 用于用户交互") {
        // Button Composable，onClick lambda 定义了点击时执行的操作
        Button(onClick = { /* 在这里处理点击事件 */ }) {
            Text("点我!")
        }
    }
}

@Composable
fun LayoutExampleScreen() {
    ExampleCard("Layouts: Column, Row") {
        Text("Column: 子项垂直排列", fontWeight = FontWeight.Bold)
        // Column 将其子项垂直堆叠
        Column(modifier = Modifier.padding(8.dp)) {
            Text("第一项")
            Text("第二项")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Row: 子项水平排列", fontWeight = FontWeight.Bold)
        // Row 将其子项水平排列
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("左侧")
            Spacer(modifier = Modifier.width(16.dp)) // Spacer 用于在项之间创建空间
            Text("右侧")
        }
    }
}

@Composable
fun StateExampleScreen() {
    ExampleCard("State: 状态驱动UI") {
        // 1. 使用 `remember` 和 `mutableStateOf` 创建一个可变状态
        // `remember` 会在重组（recomposition）之间保持这个状态
        var count by remember { mutableStateOf(0) }

        Text("Compose 的 UI 是由状态驱动的。当状态改变时，引用该状态的 Composable 会自动重组（重新绘制）。")
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 2. 这个 Button 点击时会改变 `count` 的值
            Button(onClick = { count++ }) {
                Text("点击增加")
            }
            Spacer(modifier = Modifier.width(16.dp))
            // 3. 这个 Text 显示 `count` 的当前值。当 `count` 改变，它会自动更新。
            Text("计数器: $count", fontSize = 20.sp)
        }
    }
}

@Composable
fun TextFieldExampleScreen() {
    ExampleCard("TextField: 文本输入") {
        // 1. 为输入框创建一个状态来持有输入的文本
        var text by remember { mutableStateOf("") }

        Text("TextField 用于从用户那里获取文本输入。它通常与一个状态变量一起使用。")
        Spacer(modifier = Modifier.height(16.dp))
        // 2. TextField Composable
        TextField(
            value = text, // 显示状态变量的值
            onValueChange = { newText -> text = newText }, // 当用户输入时，更新状态变量
            label = { Text("请输入你的名字") }, // 提示标签
            modifier = Modifier.fillMaxWidth()
        )
    }
}
