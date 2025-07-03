package com.example.learncompose

import android.os.Bundle
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
        LayoutTopic.CustomLayouts
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
@OptIn(ExperimentalMaterial3Api::class)
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
                    is LayoutTopic.MaterialLayouts -> MaterialLayoutsExample()
                    is LayoutTopic.ConstraintLayout -> ConstraintLayoutExample()
                    is LayoutTopic.CustomLayouts -> CustomLayoutsExample()
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
fun ComprehensiveModifiersExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ModifierOrderExample()
        CommonModifiersExample()
        ScopeSafeModifiersExample()
    }
}

@Composable
fun ModifierOrderExample() {
    LayoutTopicExampleCard("修饰符顺序 (Order Matters)") {
        Text("修饰符的调用顺序至关重要。由于每个函数都会更改前一个函数返回的修饰符，因此顺序会影响最终结果。")
        Spacer(Modifier.height(16.dp))

        Text("示例 1: 先 padding 后 background", fontWeight = FontWeight.Bold)
        Text(
            "Text with padding and background",
            modifier = Modifier
                .padding(16.dp) // 1. 在元素周围应用内边距
                .background(Color.Yellow) // 2. 将黄色背景应用到内边距内的区域
        )

        Spacer(Modifier.height(16.dp))

        Text("示例 2: 先 background 后 padding", fontWeight = FontWeight.Bold)
        Text(
            "Text with background and padding",
            modifier = Modifier
                .background(Color.Blue)
                // 1. 先将蓝色背景应用到整个元素
                .padding(16.dp)
                // 2. 在蓝色背景的外部应用内边距，将文本推离边缘
                .background(Color.Yellow)
                // 3. 再给内边距内的区域加上黄色背景
        )
    }
}

@Composable
fun CommonModifiersExample() {
    LayoutTopicExampleCard("常用修饰符") {
        Text("Compose 提供了大量开箱即用的修饰符来满足常见需求。")
        Spacer(Modifier.height(16.dp))

        var clickCount by remember { mutableStateOf(0) }

        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 100.dp) // size: 指定首选尺寸
                .background(Color.LightGray)
        ) {
            Text(
                "点我! (次数: $clickCount)",
                modifier = Modifier
                    .offset(x = 20.dp, y = 10.dp) // offset: 将元素从其原始位置偏移
                    .border(1.dp, Color.Red)
                    .background(Color.Cyan)
                    .padding(8.dp)
                    .clickable { clickCount++ } // clickable: 使元素可点击
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ScopeSafeModifiersExample() {
    LayoutTopicExampleCard("作用域安全修饰符") {
        Text("某些修饰符只能在特定的布局组件（如 Column, Row, Box）的子级中使用。这可以确保修饰符只在有意义的地方使用。")
        Spacer(Modifier.height(16.dp))
        Text("例如，`weight` 修饰符只能在 `RowScope` 或 `ColumnScope` 的子项上使用，用于分配剩余空间。而 `align` 只能在 `BoxScope` 中使用。")
        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text(
                "占 1 份",
                modifier = Modifier
                    .weight(1f) // weight: 分配剩余宽度的 1/3
                    .background(Color.Red)
                    .padding(4.dp)
            )
            Text(
                "占 2 份",
                modifier = Modifier
                    .weight(2f) // weight: 分配剩余宽度的 2/3
                    .background(Color.Green)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ConstraintModifiersExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FillSizeModifierExample()
        WrapContentModifierExample()
        MatchParentSizeModifierExample()
        DefaultMinSizeModifierExample()
    }
}

@Composable
fun FillSizeModifierExample() {
    LayoutTopicExampleCard("fillMaxSize, fillMaxWidth, fillMaxHeight") {
        Text("这些修饰符使可组合项填充父项允许的所有可用空间。")
        Spacer(Modifier.height(8.dp))
        Text("`fillMaxSize(fraction: Float)` 可以用来填充一部分空间。")
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.5f) // 填充父项 50% 的宽高
                    .background(Color.Blue)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun WrapContentModifierExample() {
    LayoutTopicExampleCard("wrapContentSize") {
        Text("此修饰符会将其子项在其可用空间内对齐，而不会影响子项的大小。")
        Spacer(Modifier.height(16.dp))
        Box(
            Modifier
                .size(150.dp)
                .background(Color.LightGray)
                .wrapContentSize(Alignment.TopEnd) // 将子项对齐到右上角
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .background(Color.Blue)
            )
        }
    }
}

@Composable
fun DefaultMinSizeModifierExample() {
    LayoutTopicExampleCard("defaultMinSize") {
        Text("此修饰符为可组合项设置最小尺寸。如果可组合项的内容小于最小尺寸，它将被迫扩展到最小尺寸。")
        Spacer(Modifier.height(16.dp))

        Text("内容小于最小尺寸:", fontWeight = FontWeight.Bold)
        Text(
            "Hi",
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp, minHeight = 50.dp)
                .background(Color.Cyan)
                .border(1.dp, Color.DarkGray)
                .padding(4.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialLayoutsExample() {
    LayoutTopicExampleCard("Scaffold: Material 布局结构") {
        Text("Scaffold 实现了一个基本的 Material Design 布局结构。它提供了多个槽位（Slot），你可以用它来放置 TopAppBar, BottomAppBar, FloatingActionButton 等。")
        Spacer(Modifier.height(8.dp))
        Text("我们当前这个详情页本身就是一个 Scaffold 布局，它包含了 TopAppBar 和一个 LazyColumn 作为内容。")
    }
}

@Composable
fun MatchParentSizeModifierExample() {
    LayoutTopicExampleCard("matchParentSize (在 BoxWithConstraints 中)") {
        Text("`matchParentSize` 修饰符使子项的大小与父项 `BoxWithConstraints` 的大小相同。它只能在 `BoxWithConstraints` 作用域内使用。")
        Spacer(Modifier.height(8.dp))
        Text("`BoxWithConstraints` 提供了父布局的约束信息，例如 `maxWidth` 和 `maxHeight`，子项可以根据这些信息来决定自己的布局。")
        Spacer(Modifier.height(16.dp))
        BoxWithConstraints(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
        ) { // this: BoxWithConstraintsScope
            // 这个 Box 的大小将与 BoxWithConstraints 相同
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Yellow.copy(alpha = 0.5f))
            )
            Text("这个文本在 BoxWithConstraints 的左上角", modifier = Modifier.align(Alignment.TopStart))

            // 明确使用 BoxWithConstraintsScope 提供的约束信息
            Text(
                "最大宽度: ${this.maxWidth}",
                modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
            )
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
