package com.example.learncompose

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.learncompose.ui.theme.LearnComposeTheme

// 1. 定义一个密封类来表示不同的布局主题
sealed class LayoutTopic(val title: String, val description: String) {
    object Home : LayoutTopic("Compose 布局基础", "学习 Compose 中布局的核心组件和概念。")
    object StandardLayouts : LayoutTopic("标准布局组件", "了解 Column, Row, 和 Box 的用法。")
    object Modifiers : LayoutTopic("修饰符 (Modifiers)", "使用修饰符来装饰或增强可组合项。")
    object ConstraintModifiers : LayoutTopic("尺寸和位置修饰符", "学习 fillMaxSize, wrapContentSize, offset 等。")
    object MaterialLayouts : LayoutTopic("Material Design 布局", "探索 Scaffold, TopAppBar 等 Material 组件。")
    object ConstraintLayout : LayoutTopic("ConstraintLayout", "为具有复杂要求的界面创建布局。")
    object CustomLayouts : LayoutTopic("自定义布局", "学习如何创建自己的自定义布局。")
    object CustomModifiers : LayoutTopic("自定义修饰符", "学习如何创建自己的修饰符以封装重用逻辑。")
    object Pager : LayoutTopic("Pager", "实现可滑动的屏幕或项目轮播。")

}

// 2. 创建新的 Activity
class LayoutsBasicsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                LayoutsBasicsApp()
            }
        }
    }
}

// 3. 应用主 Composable，用于导航
@Preview
@Composable
fun LayoutsBasicsApp() {
    var currentTopic by remember { mutableStateOf<LayoutTopic>(LayoutTopic.Home) }

    if (currentTopic is LayoutTopic.Home) {
        LayoutsTopicListScreen(onTopicClick = { newTopic ->
            currentTopic = newTopic
        })
    } else {
        LayoutsTopicDetailScreen(topic = currentTopic, onBack = {
            currentTopic = LayoutTopic.Home
        })
    }
}

// 4. 主题列表屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutsTopicListScreen(onTopicClick: (LayoutTopic) -> Unit) {
    val topics = listOf(
        LayoutTopic.StandardLayouts,
        LayoutTopic.Modifiers,
        LayoutTopic.ConstraintModifiers,
        LayoutTopic.MaterialLayouts,
        LayoutTopic.ConstraintLayout,
        LayoutTopic.CustomLayouts,
        LayoutTopic.CustomModifiers,
        LayoutTopic.Pager
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LayoutTopic.Home.title) },
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LayoutsTopicDetailScreen(topic: LayoutTopic, onBack: () -> Unit) {
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (topic) {
                    is LayoutTopic.StandardLayouts -> StandardLayoutsExample()
                    is LayoutTopic.Modifiers -> ComprehensiveModifiersExample()
                    is LayoutTopic.ConstraintModifiers -> ConstraintModifiersExample()
                    is LayoutTopic.CustomModifiers -> CustomModifiersExample()
                    is LayoutTopic.MaterialLayouts -> MaterialLayoutsExample()
                    is LayoutTopic.ConstraintLayout -> ConstraintLayoutExample()
                    is LayoutTopic.CustomLayouts -> CustomLayoutsExample()
                    is LayoutTopic.Pager -> PagerExamples()
                    else -> {}
                }
            }
        }
    }
}

// 6. 各个知识点的示例 Composable

@Composable
fun LayoutTopicExampleCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

@Composable
fun StandardLayoutsExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LayoutTopicExampleCard("Column: 垂直排列") {
            Text("Column 用于将其子项垂直地放置在屏幕上。")
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(8.dp),
                // horizontalAlignment 控制子项的水平对齐方式
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("第一项")
                Text("第二项 (居中对齐)")
                Text("第三项")
            }
        }

        LayoutTopicExampleCard("Row: 水平排列") {
            Text("Row 用于将其子项水平地放置在屏幕上。")
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(8.dp),
                // verticalAlignment 控制子项的垂直对齐方式
                verticalAlignment = Alignment.CenterVertically,
                // horizontalArrangement 控制子项的水平排列方式（如间距）
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text("左")
                Text("中")
                Text("右")
            }
        }

        LayoutTopicExampleCard("Box: 堆叠排列") {
            Text("Box 用于将其子项像堆叠一样放置，后声明的子项会盖在先声明的子项之上。")
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(1.dp, Color.Gray)
            ) {
                Box(
                    Modifier
                        .size(100.dp)
                        .background(Color.Blue)
                        .align(Alignment.TopStart)
                )
                Box(
                    Modifier
                        .size(80.dp)
                        .background(Color.Green)
                        .align(Alignment.Center)
                )
                Text(
                    "顶层",
                    Modifier
                        .background(Color.Red)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
fun ConstraintLayoutExample() {
    LayoutTopicExampleCard("ConstraintLayout: 约束布局") {
        Text("当布局比较复杂，需要灵活地相对于其他元素或父布局来定位时，可以使用 ConstraintLayout。")
        Spacer(Modifier.height(8.dp))
        Text("注意：需要添加 'androidx.constraintlayout:constraintlayout-compose' 依赖。")
        Spacer(Modifier.height(16.dp))
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {
            // 1. 使用 createRefs() 创建引用
            val (button, text) = createRefs()

            // 2. 为每个 Composable 添加约束
            Button(
                onClick = { /* ... */ },
                // 3. 将 button 的约束关联到其引用
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(parent.top, margin = 16.dp)
                }
            ) {
                Text("按钮")
            }

            Text("文本", Modifier.constrainAs(text) {
                // 将文本的顶部链接到按钮的底部
                top.linkTo(button.bottom, margin = 16.dp)
                // 将文本在父布局中水平居中
                centerHorizontallyTo(parent)
            })
        }
    }
}

@Composable
fun CustomLayoutsExample() {
    LayoutTopicExampleCard("自定义布局") {
        Text("你可以通过 Layout Composable 创建自己的自定义布局。你需要手动测量和放置子项。")
        Spacer(Modifier.height(8.dp))
        Text("下面是一个自定义的 Column，它会将子项交错排列：")
        Spacer(Modifier.height(16.dp))
        MyStaggeredColumn(modifier = Modifier.border(1.dp, Color.Gray)) {
            Text("短文本")
            Text("这是一个比较长的文本")
            Text("短的")
            Text("又一个长长长长长长长长的文本")
        }
    }
}

@Composable
fun MyStaggeredColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Layout Composable 是自定义布局的核心
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // 1. 测量 (Measure)
        // 测量每个子 Composable，获取它们的 Placeable
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        // 2. 布局 (Layout)
        // layout() 函数定义了父布局的大小，并提供了放置子项的 lambda
        layout(
            width = constraints.maxWidth,
            height = placeables.sumOf { it.height }
        ) {
            var yPosition = 0
            var xOffset = 0
            // 放置每个子项
            placeables.forEach { placeable ->
                placeable.placeRelative(x = xOffset, y = yPosition)
                // 更新下一个子项的位置
                yPosition += placeable.height
                // 交错排列
                xOffset = if (xOffset == 0) 50 else 0
            }
        }
    }
}
