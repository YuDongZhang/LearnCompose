package com.example.learncompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learncompose.ui.theme.LearnComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 1. 定义一个密封类来表示不同的架构主题
sealed class ArchitectureTopic(val title: String, val description: String) {
    object Home : ArchitectureTopic("架构知识点", "Compose UI 架构核心概念")
    object Lifecycle : ArchitectureTopic("生命周期", "在 Composable 中安全地观察生命周期事件")
    object SideEffects : ArchitectureTopic("副作用", "管理 Composable 范围之外的操作")
    object Phases : ArchitectureTopic("阶段", "Compose 的三个主要阶段：组合、布局、绘制")
    object StateHoisting : ArchitectureTopic("状态提升", "使 Composable 可复用和无状态的模式")
    object SavingState : ArchitectureTopic("保存界面状态", "在配置更改后如何保留状态")
    object ViewModel : ArchitectureTopic("架构分层 (ViewModel)", "使用 ViewModel 作为状态容器")
    object CompositionLocal : ArchitectureTopic("CompositionLocal", "在 Composable 树中隐式传递数据")
}

// 2. 创建一个新的 Activity
class ArchitectureActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                ArchitectureApp()
            }
        }
    }
}

// 3. 应用主 Composable，用于导航
@Composable
fun ArchitectureApp() {
    var currentTopic by remember { mutableStateOf<ArchitectureTopic>(ArchitectureTopic.Home) }

    when (val topic = currentTopic) {
        is ArchitectureTopic.Home -> TopicListScreen(onTopicClick = { newTopic ->
            currentTopic = newTopic
        })
        else -> TopicDetailScreen(topic = topic, onBack = {
            currentTopic = ArchitectureTopic.Home
        })
    }
}

// 4. 主题列表屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicListScreen(onTopicClick: (ArchitectureTopic) -> Unit) {
    val topics = listOf(
        ArchitectureTopic.Lifecycle,
        ArchitectureTopic.SideEffects,
        ArchitectureTopic.Phases,
        ArchitectureTopic.StateHoisting,
        ArchitectureTopic.SavingState,
        ArchitectureTopic.ViewModel,
        ArchitectureTopic.CompositionLocal
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ArchitectureTopic.Home.title) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(topics) { topic ->
                Card(
                    onClick = { onTopicClick(topic) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(topic.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(4.dp))
                        Text(topic.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

// 5. 详情屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(topic: ArchitectureTopic, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topic.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                when (topic) {
                    is ArchitectureTopic.Lifecycle -> LifecycleExample()
                    is ArchitectureTopic.SideEffects -> SideEffectsExample()
                    is ArchitectureTopic.Phases -> PhasesExample()
                    is ArchitectureTopic.StateHoisting -> StateHoistingExample()
                    is ArchitectureTopic.SavingState -> SavingStateExample()
                    is ArchitectureTopic.ViewModel -> ViewModelExample()
                    is ArchitectureTopic.CompositionLocal -> CompositionLocalExample()
                    else -> {}
                }
            }
        }
    }
}

// 6. 各个知识点的示例 Composable

@Composable
fun LifecycleExample() {
    val lifecycleOwner = LocalLifecycleOwner.current
    // DisposableEffect 用于需要在 Composable 离开组合时进行清理的副作用。
    // 它非常适合注册和注销监听器或观察者。
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            // 在 Logcat 中查看生命周期事件
            Log.d("LifecycleExample", "Lifecycle Event: ${event.name}")
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // onDispose block 会在 Composable 离开组合时执行
        onDispose {
            Log.d("LifecycleExample", "Disposing effect, removing observer.")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Text("请在 Logcat 中查看 \"LifecycleExample\" 标签的日志，观察 Activity 的生命周期事件（如 onResume, onPause）。")
}

@Composable
fun SideEffectsExample() {
    var trigger by remember { mutableIntStateOf(0) }

    // 1. LaunchedEffect: 当 key1 (这里是 trigger) 发生变化时，启动一个协程。
    // 它会在 Composable 进入组合时启动，并在离开时取消。
    // 适用于需要在 Composable 显示时执行的异步任务，如网络请求。
    LaunchedEffect(key1 = trigger) {
        Log.d("SideEffects", "LaunchedEffect started (trigger: $trigger).")
        delay(1000) // 模拟耗时操作
        Log.d("SideEffects", "LaunchedEffect finished.")
    }

    // 2. rememberCoroutineScope: 获取一个与 Composable 绑定的协程作用域。
    // 这个作用域会在 Composable 离开组合时自动取消。
    // 适用于由用户事件（如点击）触发的协程。
    val scope = rememberCoroutineScope()

    // 3. SideEffect: 在每次重组成功后执行。
    // 适用于将 Compose 的状态发布给非 Compose 代码。
    SideEffect {
        Log.d("SideEffects", "SideEffect executed on successful recomposition.")
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("副作用是 Compose 中处理 Composable 范围之外操作的方式。请查看 Logcat 中的 \"SideEffects\" 日志。")
        Button(onClick = { trigger++ }) {
            Text("触发 LaunchedEffect (当前: $trigger)")
        }
        Button(onClick = {
            scope.launch {
                Log.d("SideEffects", "Coroutine launched from Button click.")
                delay(1000)
                Log.d("SideEffects", "Coroutine from Button click finished.")
            }
        }) {
            Text("使用 rememberCoroutineScope 启动协程")
        }
    }
}

@Composable
fun PhasesExample() {
    Column {
        Text("Compose UI 渲染分为三个阶段：")
        Text("1. 组合 (Composition): 运行 Composable 函数，生成 UI 描述。")
        Text("2. 布局 (Layout): 测量和放置 UI 元素。")
        Text("3. 绘制 (Drawing): 在画布上绘制 UI 元素。")
        Spacer(Modifier.height(16.dp))

        // 使用 Layout Composable 来观察布局阶段
        Layout(
            content = {
                // 这个 Text 是 Layout 的子元素
                Text("观察日志", Modifier.padding(8.dp))
            },
            modifier = Modifier.drawBehind {
                // Modifier.drawBehind 在绘制阶段执行
                Log.d("PhasesExample", "3. Drawing Phase: 正在绘制背景")
            }
        ) { measurables, constraints ->
            // 这个 lambda 在布局阶段执行
            Log.d("PhasesExample", "2. Layout Phase: 正在测量和放置")
            val placeable = measurables.first().measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }
        Log.d("PhasesExample", "1. Composition Phase: 正在组合")
    }
}

@Composable
fun StateHoistingExample() {
    var count by remember { mutableIntStateOf(0) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("状态提升是一种将状态上移，使组件变为无状态的模式。这使得组件更易于重用和测试。")
        Divider()
        Text("下面是一个无状态的计数器组件，它的状态（count）和逻辑（onCountChange）都由其父组件提供。")
        // StatelessCounter 是无状态的，它只负责显示和回调
        StatelessCounter(
            count = count,
            onCountChange = { count++ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// 无状态（Stateless）组件
@Composable
fun StatelessCounter(count: Int, onCountChange: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("你点击了 $count 次", fontSize = 18.sp)
        Button(onClick = onCountChange) {
            Text("增加")
        }
    }
}

@Composable
fun SavingStateExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("`remember` 会在重组时保留状态，但无法在配置更改（如屏幕旋转）或进程终止后保留。")
        Text("`rememberSaveable` 可以跨越配置更改和进程终止来保存状态。")
        Divider()

        var normalCount by remember { mutableIntStateOf(0) }
        var saveableCount by rememberSaveable { mutableIntStateOf(0) }

        Text("使用 remember 的计数器: $normalCount")
        Button(onClick = { normalCount++ }) { Text("增加 (remember)") }

        Spacer(Modifier.height(16.dp))

        Text("使用 rememberSaveable 的计数器: $saveableCount")
        Button(onClick = { saveableCount++ }) { Text("增加 (rememberSaveable)") }

        Spacer(Modifier.height(16.dp))
        Text("提示：旋转你的设备，观察两个计数器的区别。")
    }
}

// 7. ViewModel 示例
class MyViewModel : ViewModel() {
    // 使用 StateFlow 或 MutableState 来持有状态
    var count by mutableIntStateOf(0)
        private set // 使外部只能读取，不能修改

    fun increment() {
        count++
    }
}

@Composable
fun ViewModelExample(myViewModel: MyViewModel = viewModel()) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("推荐的架构是将UI状态和业务逻辑放在 ViewModel 中。")
        Text("Composable 只负责观察 ViewModel 中的状态并显示它，将用户事件通知给 ViewModel。")
        Divider()
        Text("来自 ViewModel 的计数值: ${myViewModel.count}")
        Button(onClick = { myViewModel.increment() }) {
            Text("通过 ViewModel 增加")
        }
    }
}

// 8. CompositionLocal 示例

// 创建一个 CompositionLocal，提供一个默认值
val LocalAppColor = compositionLocalOf { Color.Black }

@Composable
fun CompositionLocalExample() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("CompositionLocal 允许我们将数据“隐式”地向下传递到 Composable 树中，而无需作为参数在每个 Composable 中显式传递。")
        Text("这对于主题、本地化等非常有用。")
        Divider()

        Text("未使用 CompositionLocalProvider 的文本 (默认颜色)")

        // 使用 CompositionLocalProvider 来提供一个新的值
        CompositionLocalProvider(LocalAppColor provides MaterialTheme.colorScheme.primary) {
            ThemedComponent()
        }

        CompositionLocalProvider(LocalAppColor provides MaterialTheme.colorScheme.tertiary) {
            ThemedComponent()
        }
    }
}

@Composable
fun ThemedComponent() {
    // 在子 Composable 中通过 .current 来访问值
    val color = LocalAppColor.current
    Text(
        "这个组件的文本颜色由 CompositionLocal 提供。",
        color = color,
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, color)
            .padding(8.dp)
    )
}