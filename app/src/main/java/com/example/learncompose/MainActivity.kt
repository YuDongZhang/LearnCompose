package com.example.learncompose

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learncompose.ui.theme.LearnComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data class to represent a navigation destination
data class NavigationItem(val name: String, val destination: Class<out Activity>)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Define your navigation list here
        // This makes it easy to add, remove, or change navigation targets
        val navigationItems = listOf(
            NavigationItem("Go to A Activity", AMainActivity::class.java),
            NavigationItem("Go to B Activity", BMainActivity::class.java),
            NavigationItem("Go to C Activity", CMainActivity::class.java),
            NavigationItem("Go to D Activity", DMainActivity::class.java),
            NavigationItem("Go to AuthActivity", AuthActivity::class.java),
            NavigationItem("基础知识", BasicKnowledgeActivity::class.java),
        )

        setContent {
            LearnComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Conversation(
                            messages = SampleData.conversationSample,
                            modifier = Modifier.weight(1f)
                        )
                        // Pass the navigation list to the composable
                        ActivityNavigationList(items = navigationItems)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityNavigationList(items: List<NavigationItem>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Text(
                text = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val intent = Intent(context, item.destination)
                            context.startActivity(intent)
                        } catch (e: ClassNotFoundException) {
                            Toast.makeText(
                                context,
                                "Activity not found: ${item.destination.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .padding(16.dp)
            )
        }
    }
}


@Composable
fun MessageCard(msg: Message) {
    // {{ edit_3 }}
    // 添加一个状态变量 isExpanded，用于跟踪消息是否展开
    // remember 用于在重组时记住状态
    // mutableStateOf 创建一个可观察的状态变量
    var isExpanded by remember { mutableStateOf(false) }

    // 获取 Context 用于显示 Toast
    val context = LocalContext.current
    // 获取 CoroutineScope 用于在点击事件中启动协程
    val scope = rememberCoroutineScope()

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                // 在图片周围添加边框
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // 添加水平间距
        Spacer(modifier = Modifier.width(8.dp))

        // 我们在这里跟踪 isExpanded 状态，并处理点击事件
        Column(modifier = Modifier.clickable {
            // 根据消息内容处理点击事件
            when (msg.body) {
                "普通组合函数" -> {
                    // 普通组合函数：点击切换展开/折叠状态
                    isExpanded = !isExpanded
                }

                "LaunchedEffect" -> {
                    // LaunchedEffect 通常用于在 Composable 进入组合或 key 变化时执行异步操作
                    Toast.makeText(
                        context,
                        "点击了 LaunchedEffect (通常由状态变化触发)",
                        Toast.LENGTH_SHORT
                    ).show()
                    /*
                    // 示例结构 (非点击直接触发):
                    LaunchedEffect(key1 = someState) {
                        // 当 Composable 进入组合或 someState 变化时执行
                        // 这是一个协程作用域
                        // 例如: fetchData()
                    }
                    */
                }

                "rememberCoroutineScope" -> {
                    // rememberCoroutineScope 用于获取一个与 Composable 生命周期绑定的 Scope
                    // 可以在事件回调中启动协程
                    Toast.makeText(
                        context,
                        "点击了 rememberCoroutineScope (用于在事件中启动协程)",
                        Toast.LENGTH_SHORT
                    ).show()
                    // 示例用法: 在点击事件中启动一个协程
                    scope.launch {
                        // 这个协程与当前 Composable 的生命周期绑定
                        kotlinx.coroutines.delay(500) // 模拟耗时操作
                        // 在协程中完成任务，例如更新状态或执行其他操作
                        println("Coroutine launched by rememberCoroutineScope finished.")
                        // 如果需要在主线程更新 UI (例如显示 Toast)，需要切换到主线程或使用其他机制
                        // Toast.makeText(context, "协程任务完成!", Toast.LENGTH_SHORT).show() // 注意: 直接在协程中显示 Toast 可能需要主线程上下文
                    }
                }

                "DisposableEffect" -> {
                    // DisposableEffect 用于需要在 Composable 离开组合时进行清理的附带效应
                    Toast.makeText(
                        context,
                        "点击了 DisposableEffect (用于资源管理/清理)",
                        Toast.LENGTH_SHORT
                    ).show()
                    /*
                    // 示例结构 (非点击直接触发):
                    DisposableEffect(key1 = resource) {
                        // Setup: 获取资源, 注册监听器等
                        // 例如: val listener = MyListener(); listener.register()

                        onDispose {
                            // Cleanup: 释放资源, 注销监听器等
                            // 例如: listener.unregister()
                        }
                    }
                    */
                }

                "SideEffect" -> {
                    // SideEffect 用于将 Compose 状态发布到非 Compose 代码
                    // 它会在每次成功重组后同步执行
                    Toast.makeText(
                        context,
                        "点击了 SideEffect (用于同步 Compose 状态到外部)",
                        Toast.LENGTH_SHORT
                    ).show()
                    /*
                    // 示例结构 (在每次成功重组后执行):
                    SideEffect {
                        // 在这里访问 Compose 状态并传递给外部系统
                        // 例如: analyticsManager.logState(currentState)
                    }
                    */
                }

                "rememberUpdatedState" -> {
                    // rememberUpdatedState 用于在 LaunchedEffect 或 DisposableEffect 中引用可能随时间变化的值
                    // 确保 Effect 使用的是最新的值，而无需重新启动 Effect
                    Toast.makeText(
                        context,
                        "点击了 rememberUpdatedState (用于在 Effect 中引用最新状态)",
                        Toast.LENGTH_SHORT
                    ).show()
                    /*
                    // 示例结构 (在 LaunchedEffect/DisposableEffect 内部使用):
                    val latestValue by rememberUpdatedState(valueThatCanChange)
                    LaunchedEffect(Unit) { // Effect 的 key 固定，只启动一次
                        kotlinx.coroutines.delay(someTime)
                        // 在这里使用 latestValue，即使 valueThatCanChange 在 Effect 启动后发生了变化
                        // 例如: performAction(latestValue)
                    }
                    */
                }

                else -> {
                    // 默认行为：切换展开/折叠状态
                    isExpanded = !isExpanded
                }
            }
        }) { // 添加 clickable 修饰符，点击时触发上述逻辑
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            // 添加垂直间距
            Spacer(modifier = Modifier.height(4.dp))

            // Surface 包裹文本，提供背景和阴影
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // 在展开时添加边框
                // color = surfaceColor, // 使用动画颜色
                modifier = Modifier.border(
                    // 根据 isExpanded 状态决定是否添加边框
                    if (isExpanded) 4.dp else 0.dp, // 展开时边框宽度为 4dp，否则为 0
                    MaterialTheme.colorScheme.primary, // 边框颜色
                    MaterialTheme.shapes.medium // 边框形状与 Surface 形状一致
                )
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // 根据 isExpanded 状态决定文本的最大行数
                    // 如果展开，则显示所有行 (Int.MAX_VALUE)；否则只显示一行
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class Message(val author: String, val body: String)

/**
 * SampleData for Jetpack Compose Tutorial
 * 示例数据，用于演示消息列表
 */
object SampleData {
    // Sample conversation data
    // 示例对话数据列表
    val conversationSample = listOf(
        // {{ edit_2 }}
        Message("概念", "普通组合函数"),
        Message("概念", "LaunchedEffect"),
        Message("概念", "rememberCoroutineScope"),
        Message("概念", "DisposableEffect"),
        Message("概念", "SideEffect"),
        Message("概念", "rememberUpdatedState")
        // {{ /edit_2 }}
    )
}

@Composable
// Conversation 函数接受一个 Modifier 参数
fun Conversation(messages: List<Message>, modifier: Modifier = Modifier) {
    // 将传入的 modifier 应用到 LazyColumn，这样 Scaffold 的内边距就会作用于列表
    LazyColumn(modifier = modifier) {
        // {{ /edit_2 }}
        items(messages) { message ->
            MessageCard(message)
        }
    }
}


// 预览 Conversation
@Preview
@Composable
fun PreviewConversation() {
    // 使用您的项目主题 LearnComposeTheme
    LearnComposeTheme {
        // 预览包含示例数据的消息列表
        Conversation(SampleData.conversationSample)
    }
}

//这个没有达到教程的结果，而且发现 @Preview 加一个会复制一份
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    Surface {
        MessageCard(
            msg = Message("Lexi", "Hey, take a look at Jetpack Compose, it's great!")
        )
    }
}


//@Composable 如需使函数成为可组合函数，请添加 @Composable 注解。
//Modifier 就是一个控制边界的东西
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.padding(all = 1.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "你好肾宝",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        //边距
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
            Spacer(modifier = Modifier.height(4.dp))//还是间距
            Text(text = "hahha")
        }
    }
}

//@Preview  用来预览的
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearnComposeTheme {
        Greeting("Android")
    }
}
