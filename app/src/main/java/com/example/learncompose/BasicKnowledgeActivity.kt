package com.example.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learncompose.ui.theme.LearnComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 讲师点评：
 * 这里是我们的主 Activity，也是 App 的唯一入口。
 * 它继承自 ComponentActivity，这是 Jetpack Compose 应用的标准做法。
 */
class BasicKnowledgeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                // AppNavigator 是我们导航系统的核心
                AppNavigator()
            }
        }
    }
}

/**
 * 讲师点评：
 * 定义一个数据类来表示我们的每一个课程主题。
 * 包含标题、描述和导航用的路由（route）。
 */
data class Topic(
    val title: String,
    val description: String,
    val route: String
)

/**
 * 讲师点评：
 * 在这里我们定义了所有的课程主题。
 * 以后想添加新的学习页面，只需要在这里增加一个 Topic 即可。
 */
val topics = listOf(
    Topic("remember & mutableStateOf", "学习 Compose 如何记忆状态", "topic/remember"),
    Topic("LaunchedEffect", "在 Composable 中安全地执行挂起函数", "topic/launchedeffect"),
    Topic("rememberCoroutineScope", "获取一个感知生命周期的协程作用域", "topic/rememberscope"),
    Topic("rememberUpdatedState", "在 Effect 中引用最新的值", "topic/rememberupdatedstate"),
    Topic("DisposableEffect", "需要清理资源的 Effect", "topic/disposableeffect"),
    Topic("derivedStateOf", "将一个或多个状态对象转换为其他状态", "topic/derivedstateof")
)

/**
 * 讲师点评：
 * 这是导航系统的核心 Composable。
 * NavController 用于导航控制。
 * NavHost 是一个容器，用于显示当前路由对应的 Composable 页面。
 */
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "topic_list") {
        // 主题列表页
        composable("topic_list") {
            TopicListScreen(navController = navController, topics = topics)
        }
        // 各个主题的详情页
        composable(topics[0].route) { RememberScreen() }
        composable(topics[1].route) { LaunchedEffectScreen() }
        composable(topics[2].route) { RememberCoroutineScopeScreen() }
        composable(topics[3].route) { RememberUpdatedStateScreen() }
        composable(topics[4].route) { DisposableEffectScreen() }
        composable(topics[5].route) { DerivedStateOfScreen() }
    }
}

/**
 * 讲师点评：
 * 我们的“目录”页面。
 * 使用 LazyColumn 来高效地显示一个可滚动的列表。
 */
@Composable
fun TopicListScreen(navController: NavController, topics: List<Topic>) {
    Scaffold(
        topBar = {
            Text(
                text = "Jetpack Compose 学习目录",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(topics) { topic ->
                TopicListItem(topic = topic) {
                    navController.navigate(topic.route)
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun TopicListItem(topic: Topic, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = topic.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = topic.description, style = MaterialTheme.typography.bodyMedium)
    }
}

// --- 以下是每个知识点的独立学习页面 ---

@Composable
fun TopicScreenScaffold(title: String, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun ExplanationCard(title: String, explanation: String, code: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = explanation, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "示例代码:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = code,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}


@Composable
fun RememberScreen() {
    TopicScreenScaffold(title = "remember & mutableStateOf") {
        ExplanationCard(
            title = "核心概念",
            explanation = """Compose 的核心思想是状态驱动UI更新。
`remember` 用于在多次重组之间“记住”一个值。
`mutableStateOf` 创建一个可被 Compose 观察的“状态”，当它的 `.value` 改变时，所有读取该 value 的 Composable 都会重组。""",
            code = """
@Composable
fun MyButton() {
    // 使用 remember 和 mutableStateOf 创建一个可观察的状态
    var num: Int by remember { mutableStateOf(0) }

    Column {
        Button(onClick = { num++ }) { // 点击时更新状态
            Text("点我加一")
        }
        // Text 会在 num 变化时自动重组，显示新值
        Text("当前点击次数：$/num")
    }
}
            """.trimIndent()
        )
        MyButtonExample()
    }
}

@Composable
fun LaunchedEffectScreen() {
    TopicScreenScaffold(title = "LaunchedEffect") {
        ExplanationCard(
            title = "核心概念",
            explanation = """用于在 Composable 的生命周期内启动一个协程。当 Composable 进入组合时，它会执行 block 中的代码。如果 key1 参数变化，它会取消之前的协程并重新启动一个新的。

常用于：
- 数据加载
- 只需执行一次的动画
- 与非 Compose 世界的交互""",
            code = """
@Composable
fun CoroutineTest() {
    var showText by remember { mutableStateOf("") }

    // 当这个 Composable 进入组合时，启动协程
    // key1 = Unit 表示这个 effect 只会执行一次
    LaunchedEffect(key1 = Unit) {
        delay(3000L) // 模拟网络请求或耗时操作
        showText = "3秒后，我显示了"
    }
    Text(text = showText)
}
            """.trimIndent()
        )
        CoroutineTestExample()
    }
}

@Composable
fun RememberCoroutineScopeScreen() {
    TopicScreenScaffold(title = "rememberCoroutineScope") {
        ExplanationCard(
            title = "核心概念",
            explanation = """获取一个与 Composable 生命周期绑定的协程作用域(CoroutineScope)。与 `LaunchedEffect` 不同，它给你一个 `scope` 对象，让你可以在非 Composable 上下文（如点击事件）中手动启动协程。

当 Composable 离开组合时，这个 scope 会被自动取消。""",
            code = """
@Composable
fun CoroutineTestBt() {
    val scope = rememberCoroutineScope()
    var showText by remember { mutableStateOf("") }

    Column {
        Button(onClick = {
            // 在点击事件这个“非组合”上下文中启动协程
            scope.launch {
                delay(3000L)
                showText = "点击按钮3秒后，我显示了"
            }
        }) {
            Text("点击开始协程")
        }
        Text(text = showText)
    }
}
            """.trimIndent()
        )
        CoroutineTestBtExample()
    }
}

@Composable
fun RememberUpdatedStateScreen() {
    TopicScreenScaffold(title = "rememberUpdatedState") {
        ExplanationCard(
            title = "核心概念",
            explanation = """当你的 `LaunchedEffect` 或 `DisposableEffect` 中需要引用一个可能会变化的参数，但又不希望因为这个参数的变化而重启 Effect 时，就用 `rememberUpdatedState`。

它会创建一个状态来“包装”你的参数，并始终返回最新的值，但读取它不会导致重组。""",
            code = """
@Composable
fun ShowTextWithDelay(text: String) {
    var showText by remember { mutableStateOf("") }
    
    // 关键点：用 rememberUpdatedState 包装 text
    val updatedText by rememberUpdatedState(newValue = text)

    // Effect 只在进入时启动一次
    LaunchedEffect(key1 = Unit) {
        delay(3000L)
        // 即使外部的 text 参数在3秒内变了，
        // updatedText.value 也能取到最新的值
        showText = "最新内容是: ${'$'}{updatedText}"
    }
    Text(text = showText)
}
            """.trimIndent()
        )
        var textToShow by remember { mutableStateOf("初始文本") }
        Button(onClick = { textToShow = "更新后的文本" }) {
            Text(text = "点击更新文本")
        }
        ShowTextWithDelay(text = textToShow)
    }
}

@Composable
fun DisposableEffectScreen() {
    TopicScreenScaffold(title = "DisposableEffect") {
        ExplanationCard(
            title = "核心概念",
            explanation = """和 `LaunchedEffect` 类似，但它强制你提供一个 `onDispose` 代码块。

当 Composable 离开组合，或者 key 发生变化导致 Effect 重启时，`onDispose` 会被执行。非常适合用于注册和注销监听器、订阅和取消订阅等需要成对出现的清理操作。""",
            code = """
@Composable
fun LifecycleLogger(name: String) {
    DisposableEffect(key1 = name) {
        println("Composable '$'name' 进入组合")

        onDispose {
            println("Composable '$'name' 离开组合")
        }
    }
    Text("这是一个带生命周期日志的 Composable")
}
            """.trimIndent()
        )
        var showLogger by remember { mutableStateOf(true) }
        Button(onClick = { showLogger = !showLogger }) {
            Text(if (showLogger) "隐藏 Logger" else "显示 Logger")
        }
        if (showLogger) {
            LifecycleLogger(name = "测试")
        }
    }
}

@Composable
fun DerivedStateOfScreen() {
    TopicScreenScaffold(title = "derivedStateOf") {
        ExplanationCard(
            title = "核心概念",
            explanation = """用于将一个或多个状态对象转换为另一个状态。只有当计算结果真正发生变化时，读取 `derivedStateOf` 结果的 Composable 才会重组，这是一种性能优化。

当你某个状态依赖于其他一个或多个状态的计算结果时，它非常有用。""",
            code = """
@Composable
fun FilteredList() {
    val list = remember { mutableStateListOf("Apple", "Banana", "Cherry", "Date") }
    var filter by remember { mutableStateOf("") }

    // 关键点：只有当过滤后的列表内容实际改变时，
    // 读取 filteredList 的 Composable 才会重组。
    val filteredList by remember(list, filter) {
        derivedStateOf {
            list.filter { it.contains(filter, ignoreCase = true) }
        }
    }

    Column {
        Button(onClick = { filter = "a" }) { Text("Filter 'a'") }
        Text("Filtered items:")
        filteredList.forEach { Text(it) }
    }
}
            """.trimIndent()
        )
        FilteredList()
    }
}


// --- 从 AMainActivity.kt 提取的示例 Composable ---

@Composable
fun MyButtonExample() {
    var num: Int by remember { mutableStateOf(0) }
    Column {
        Button(onClick = { num++ }) {
            Text("点我加一")
        }
        Text("当前点击次数：$num")
    }
}

@Composable
fun CoroutineTestExample() {
    var showText by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit) {
        delay(2000L)
        showText = "2秒后，我显示了"
    }
    Text(text = showText)
}

@Composable
fun CoroutineTestBtExample() {
    val scope = rememberCoroutineScope()
    var showText by remember { mutableStateOf("") }
    Column {
        Button(onClick = {
            scope.launch {
                delay(2000L)
                showText = "点击按钮2秒后，我显示了"
            }
        }) {
            Text("点击开始协程")
        }
        Text(text = showText)
    }
}

@Composable
fun ShowTextWithDelay(text: String) {
    var showText by remember { mutableStateOf("等待中...") }
    val updatedText by rememberUpdatedState(newValue = text)
    LaunchedEffect(key1 = Unit) {
        delay(2000L)
        showText = "最新内容是: ${updatedText}"
    }
    Text(text = showText)
}

@Composable
fun LifecycleLogger(name: String) {
    DisposableEffect(key1 = name) {
        println("Logger: Composable '$name' entered composition")
        onDispose {
            println("Logger: Composable '$name' left composition")
        }
    }
    Text("这是一个带生命周期日志的 Composable: $name")
}

@Composable
fun FilteredList() {
    val list = remember { mutableStateListOf("Apple", "Banana", "Cherry", "Date", "Avocado") }
    var filter by remember { mutableStateOf("") }

    val filteredList by remember {
        derivedStateOf {
            if (filter.isBlank()) {
                list
            } else {
                list.filter { it.contains(filter, ignoreCase = true) }
            }
        }
    }

    Column {
        Button(onClick = { filter = if (filter == "a") "" else "a" }) {
            Text(if (filter == "a") "清除过滤器" else "过滤含 'a' 的项")
        }
        Spacer(Modifier.height(8.dp))
        Text("列表项:")
        filteredList.forEach {
            Text(" - $it")
        }
    }
}
