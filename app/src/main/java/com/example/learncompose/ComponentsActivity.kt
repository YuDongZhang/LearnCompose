package com.example.learncompose

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learncompose.component.CardExamples
import com.example.learncompose.component.ChipExamples
import com.example.learncompose.component.DialogExamples
import com.example.learncompose.component.CarouselExamples
import com.example.learncompose.component.CheckboxExamples
import com.example.learncompose.component.ImageExamples
import com.example.learncompose.component.DrawingExamples
import com.example.learncompose.component.AnimationExamples
import com.example.learncompose.component.ValueBasedAnimationExamples
import com.example.learncompose.component.AdvancedAnimationExamples
import com.example.learncompose.ui.theme.LearnComposeTheme

// 1. 定义一个密封类来表示不同的组件主题
sealed class ComponentTopic(val title: String, val description: String) {
    object Home : ComponentTopic("Compose 组件", "探索 Compose 中常用的 UI 组件。")
    object Cards : ComponentTopic("Cards - 卡片", "卡片是包含特定内容和操作的 Material Design 容器。")
    object Chips : ComponentTopic("Chips - 标签", "Chip 是表示输入、属性或操作的紧凑元素。")
    object Dialogs : ComponentTopic("Dialogs - 对话框", "对话框是通知用户特定任务并可以包含关键信息的小窗口。")
    object Carousel : ComponentTopic("Carousel - 轮播", "轮播组件允许用户在同一层级的内容之间进行水平翻页。")
    object Checkboxes : ComponentTopic("Checkboxes - 复选框", "复选框允许用户从一个集合中选择一个或多个项目。")
    object Image : ComponentTopic("Image - 图像", "图像是用于在屏幕上绘制图形的组件。")
    object Drawing : ComponentTopic("Drawing - 绘图", "使用 Canvas 和修饰符进行自定义绘图。")
    object Animation : ComponentTopic("Animation - 动画", "探索 Compose 中各种强大的动画 API。")
    object ValueBasedAnimation : ComponentTopic("Value-based Animation - 基于值的动画", "使用 Animatable 和 AnimationSpec 创建精细控制的动画。")
    object AdvancedAnimation : ComponentTopic("Advanced Animation - 高级动画", "探索手势驱动动画和 LookaheadLayout。")
    // 新增 Chip 组件
    object Material3Chips : ComponentTopic("Material3 Chips - 条状标签", "Material3 风格的 Chip 组件，包括 AssistChip、FilterChip、InputChip、SuggestionChip 等。")
    // 新增 DatePicker 组件
    object Material3DatePicker : ComponentTopic("Material3 DatePicker - 日期选择器", "Material3 风格的日期选择器，包括单选、范围、对话框等。")
    // 新增 Material3 Dialog 组件
    object Material3Dialog : ComponentTopic("Material3 Dialog - 对话框", "Material3 风格的对话框，包括 AlertDialog、自定义对话框等。")
    // 可以在这里添加更多组件...
}

// 2. 创建新的 Activity
class ComponentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                ComponentsApp()
            }
        }
    }
}

// 3. 应用主 Composable，用于导航
@Preview
@Composable
fun ComponentsApp() {
    var currentTopic by remember { mutableStateOf<ComponentTopic>(ComponentTopic.Home) }

    if (currentTopic is ComponentTopic.Home) {
        ComponentListScreen(onTopicClick = { newTopic ->
            currentTopic = newTopic
        })
    } else {
        ComponentDetailScreen(topic = currentTopic, onBack = {
            currentTopic = ComponentTopic.Home
        })
        // 处理返回按钮，以便导航回列表
        BackHandler {
            currentTopic = ComponentTopic.Home
        }
    }
}

// 4. 主题列表屏幕
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentListScreen(onTopicClick: (ComponentTopic) -> Unit) {
    val topics = listOf(
        ComponentTopic.Cards,
        ComponentTopic.Chips,
        // 新增 Material3Chips
        ComponentTopic.Material3Chips,
        // 新增 Material3DatePicker
        ComponentTopic.Material3DatePicker,
        // 新增 Material3Dialog
        ComponentTopic.Material3Dialog,
        ComponentTopic.Dialogs,
        ComponentTopic.Carousel,
        ComponentTopic.Checkboxes,
        ComponentTopic.Image,
        ComponentTopic.Drawing,
        ComponentTopic.Animation,
        ComponentTopic.ValueBasedAnimation,
        ComponentTopic.AdvancedAnimation
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ComponentTopic.Home.title) },
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
fun ComponentDetailScreen(topic: ComponentTopic, onBack: () -> Unit) {
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
                    is ComponentTopic.Cards -> CardExamples()
                    is ComponentTopic.Chips -> ChipExamples()
                    is ComponentTopic.Material3Chips -> ChipExamplesScreen()
                    is ComponentTopic.Material3DatePicker -> DatePickerExamplesScreen()
                    is ComponentTopic.Material3Dialog -> DialogExamplesMaterial3Screen()
                    is ComponentTopic.Dialogs -> DialogExamples()
                    is ComponentTopic.Carousel -> CarouselExamples()
                    is ComponentTopic.Checkboxes -> CheckboxExamples()
                    is ComponentTopic.Image -> ImageExamples()
                    is ComponentTopic.Drawing -> DrawingExamples()
                    is ComponentTopic.Animation -> AnimationExamples()
                    is ComponentTopic.ValueBasedAnimation -> ValueBasedAnimationExamples()
                    is ComponentTopic.AdvancedAnimation -> AdvancedAnimationExamples()
                    else -> {}
                }
            }
        }
    }
}
