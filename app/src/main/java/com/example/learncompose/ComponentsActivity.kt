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
    // 新增 Divider 组件
    object Divider : ComponentTopic("Divider - 分隔线", "用于分隔列表或容器中的项，支持水平和垂直分隔线。")
    // 新增 Menu 组件
    object Menu : ComponentTopic("Menu - 菜单", "用于显示一组选项，包括下拉菜单和暴露式下拉菜单。")
    // 新增 List 组件
    object List : ComponentTopic("List - 列表", "Compose 中的 LazyColumn、LazyRow、LazyVerticalGrid、分组、嵌套、footer、divider 等官方列表用法。")
    // 新增 Navigation 组件
    object Navigation : ComponentTopic("Navigation - 导航", "Compose 官方 NavigationBar、Drawer、NavigationRail 三大导航组件用法与交互演示。")
    // 新增 Progress 组件
    object Progress : ComponentTopic("Progress - 进度指示器", "Compose 官方 Linear/CircularProgressIndicator 进度条用法，支持确定/不确定进度。")
    // 新增 PullToRefresh 组件
    object PullToRefresh : ComponentTopic("PullToRefresh - 下拉刷新", "Compose 官方 PullToRefreshBox 下拉刷新与自定义指示器用法演示。")
    // 新增 RadioButtonAndResources 组件
    object RadioButtonAndResources : ComponentTopic("RadioButton & Resources - 单选按钮与资源", "Compose 官方 RadioButton 单选按钮和 Resources 资源访问用法演示。")
    // 新增 Scaffold 组件
    object Scaffold : ComponentTopic("Scaffold - 脚手架", "Compose 官方 Scaffold 脚手架用法，包含顶部应用栏、底部应用栏、悬浮操作按钮等。")
    // 新增 SearchBar 组件
    object SearchBar : ComponentTopic("SearchBar - 搜索栏", "Compose 官方 SearchBar 搜索栏用法，包含搜索建议、搜索结果等。")
    // 新增 SliderAndSnackbar 组件
    object SliderAndSnackbar : ComponentTopic("Slider & Snackbar - 滑块与快捷信息栏", "Compose 官方 Slider、RangeSlider 和 Snackbar 用法演示。")
    // 新增 TabsAndTimePickersAndTooltip 组件
    object TabsAndTimePickersAndTooltip : ComponentTopic("Tabs & TimePicker & Tooltip - 标签页、时间选择器与工具提示", "Compose 官方 TabRow、TimePicker、Tooltip 用法演示。")
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
        // 新增 Divider
        ComponentTopic.Divider,
        // 新增 Menu
        ComponentTopic.Menu,
        // 新增 List
        ComponentTopic.List,
        // 新增 Navigation
        ComponentTopic.Navigation,
        // 新增 Progress
        ComponentTopic.Progress,
        // 新增 PullToRefresh
        ComponentTopic.PullToRefresh,
        // 新增 RadioButtonAndResources
        ComponentTopic.RadioButtonAndResources,
        // 新增 Scaffold
        ComponentTopic.Scaffold,
        // 新增 SearchBar
        ComponentTopic.SearchBar,
        // 新增 SliderAndSnackbar
        ComponentTopic.SliderAndSnackbar,
        // 新增 TabsAndTimePickersAndTooltip
        ComponentTopic.TabsAndTimePickersAndTooltip,
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
                    is ComponentTopic.Divider -> com.example.learncompose.component.DividerExamples()
                    is ComponentTopic.Menu -> com.example.learncompose.component.MenuExamples()
                    is ComponentTopic.List -> com.example.learncompose.component.ListExamples()
                    is ComponentTopic.Navigation -> com.example.learncompose.component.NavigationExamples()
                    is ComponentTopic.Progress -> com.example.learncompose.component.ProgressExamples()
                    is ComponentTopic.PullToRefresh -> com.example.learncompose.component.PullToRefreshExamples()
                    is ComponentTopic.RadioButtonAndResources -> com.example.learncompose.component.RadioButtonAndResourcesExamples()
                    is ComponentTopic.Scaffold -> com.example.learncompose.component.ScaffoldExamples()
                    is ComponentTopic.SearchBar -> com.example.learncompose.component.SearchBarExamples()
                    is ComponentTopic.SliderAndSnackbar -> com.example.learncompose.component.SliderAndSnackbarExamples()
                    is ComponentTopic.TabsAndTimePickersAndTooltip -> com.example.learncompose.component.TabsAndTimePickersAndTooltipExamples()
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
