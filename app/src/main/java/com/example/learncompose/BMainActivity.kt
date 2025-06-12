package com.example.learncompose

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// {{ edit_1 }}
import androidx.activity.compose.setContent // 导入 setContent
import androidx.compose.foundation.Image // 导入 Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // 导入所有布局相关的组件
import androidx.compose.foundation.lazy.LazyColumn // 导入 LazyColumn
import androidx.compose.foundation.lazy.items // 导入 LazyColumn 的 items
import androidx.compose.foundation.rememberScrollState // 导入 rememberScrollState 用于 Column 的滚动
import androidx.compose.foundation.verticalScroll // 导入 verticalScroll 修饰符
import androidx.compose.material3.* // 导入 Material3 组件
import androidx.compose.runtime.* // 导入状态相关的组件
import androidx.compose.ui.Alignment // 导入 Alignment
import androidx.compose.ui.Modifier // 导入 Modifier
import androidx.compose.ui.res.painterResource // 导入 painterResource
import androidx.compose.ui.tooling.preview.Preview // 导入 Preview
import androidx.compose.ui.unit.dp // 导入 dp
import com.example.learncompose.ui.theme.LearnComposeTheme // 导入主题
// {{ edit_1 }}
import android.widget.Toast // 导入 Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext // 导入 LocalContext
import androidx.compose.material3.ExposedDropdownMenuBox // 导入 ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults // 导入 ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField // 导入 TextField
import androidx.compose.material3.OutlinedTextField // 导入 OutlinedTextField
import androidx.compose.material3.DropdownMenuItem // 导入 DropdownMenuItem
import androidx.compose.material3.Icon // 导入 Icon
import androidx.compose.material.icons.Icons // 导入 Icons
import androidx.compose.material.icons.filled.ArrowDropDown // 导入 ArrowDropDown Icon
import androidx.compose.ui.text.input.ImeAction // 导入 ImeAction
// Add new animation imports here
import androidx.compose.animation.AnimatedVisibility // 导入 AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState // 导入 animateDpAsState
import androidx.compose.animation.core.tween // 导入 tween
import androidx.compose.foundation.background // 导入 background
// {{ /edit_1 }}

class BMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 启用边缘到边缘显示
        // {{ edit_2 }}
        setContent {
            LearnComposeTheme { // 应用您的主题
                BMainScreen() // 显示主屏幕的可组合函数
            }
        }
        // {{ /edit_2 }}
    }
}

// {{ edit_3 }}
// 定义 BMainScreen 可组合函数
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMainScreen() {
    // {{ edit_2 }}
    // 获取 Context 用于显示 Toast
    val context = LocalContext.current
    // {{ /edit_2 }}

    // Scaffold 提供基本的 Material Design 布局结构，并处理内边距
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // Column 结合 verticalScroll 实现垂直滚动
        Column(
            modifier = Modifier
                .padding(innerPadding) // 应用 Scaffold 提供的内边距
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // 使 Column 可垂直滚动
                .padding(16.dp), // 添加一些整体内边距
            horizontalAlignment = Alignment.CenterHorizontally, // 子元素水平居中
            verticalArrangement = Arrangement.spacedBy(16.dp) // 子元素之间添加垂直间距
        ) {
            // TextView 对应 Compose 的 Text
            Text("这是一个 Compose Text (对应 TextView)")

            // Button 对应 Compose 的 Button
            Button(onClick = {
                // {{ edit_3 }}
                // 在按钮点击时显示 Toast
                Toast.makeText(context, "Button 被点击了!", Toast.LENGTH_SHORT).show()
                // {{ /edit_3 }}
            }) {
                Text("这是一个 Compose Button (对应 Button)")
            }

            // ImageView 对应 Compose 的 Image
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // 使用一个示例图片资源
                contentDescription = "示例图片",
                modifier = Modifier.size(100.dp) // 设置图片大小
            )

            // RecyclerView 对应 Compose 的 LazyColumn 或 LazyRow
            Text("这是一个 Compose LazyColumn (对应 RecyclerView)")
            // LazyColumn 需要一个固定的高度或在可滚动的父容器中
            // 在这里我们给它一个固定高度作为示例
            LazyColumn(
                modifier = Modifier
                    .height(200.dp) // 给 LazyColumn 一个固定高度
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary), // 添加边框以便区分
                verticalArrangement = Arrangement.spacedBy(4.dp) // 列表项之间添加间距
            ) {
                items(List(10) { "列表项 $it" }) { item ->
                    Text(item, modifier = Modifier.padding(horizontal = 8.dp))
                }
            }

            // Dialog 对应 Compose 的 AlertDialog 或自定义 Dialog
            // 使用状态变量控制 Dialog 的显示
            var showDialog by remember { mutableStateOf(false) }

            Button(onClick = { showDialog = true }) {
                Text("点击显示 Compose Dialog (对应 Dialog)")
            }

            // 根据状态变量显示 Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        // 点击对话框外部或返回键时关闭对话框
                        showDialog = false
                    },
                    title = {
                        Text("Compose 对话框")
                    },
                    text = {
                        Text("这是一个使用 Jetpack Compose 实现的对话框示例。")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                // 处理确认按钮点击
                                showDialog = false
                            }) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                // 处理取消按钮点击
                                showDialog = false
                            }) {
                            Text("取消")
                        }
                    }
                )
            }
            // {{ edit_2 }}
            // 输入框 (对应 EditText)
            Text("这是一个 Compose OutlinedTextField (对应 EditText)")
            // 使用状态变量来保存输入框的文本
            var textInput by remember { mutableStateOf("") }
            OutlinedTextField(
                value = textInput, // 当前文本值
                onValueChange = { textInput = it }, // 文本改变时的回调
                label = { Text("请输入文本") }, // 提示文本
                modifier = Modifier.fillMaxWidth(), // 填充父容器宽度
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done) // 键盘选项，例如完成按钮
            )

            // 下拉选择类似 spinner (对应 Spinner)
            Text("这是一个 Compose ExposedDropdownMenuBox (对应 Spinner)")
            // 下拉菜单的状态变量
            var expanded by remember { mutableStateOf(false) }
            // 选中的项目状态变量
            val options = listOf("选项 1", "选项 2", "选项 3", "选项 4", "选项 5")
            var selectedOptionText by remember { mutableStateOf(options[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded, // 控制菜单是否展开
                onExpandedChange = { expanded = !expanded }, // 点击时切换展开状态
                modifier = Modifier.fillMaxWidth()
            ) {
                // 作为下拉菜单的锚点，通常是一个 TextField
                TextField(
                    // 修饰符必须包含 .menuAnchor()
                    modifier = Modifier.menuAnchor(),
                    readOnly = true, // 设置为只读，因为值是通过选择菜单项改变的
                    value = selectedOptionText, // 显示当前选中的文本
                    onValueChange = {}, // 只读，所以 onValueChange 为空
                    label = { Text("选择一个选项") },
                    trailingIcon = {
                        // 添加一个下拉箭头图标
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors() // 使用默认颜色
                )
                // 下拉菜单本身
                ExposedDropdownMenu(
                    expanded = expanded, // 控制菜单是否展开
                    onDismissRequest = { expanded = false } // 点击菜单外部时关闭菜单
                ) {
                    // 遍历选项列表，为每个选项创建菜单项
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) }, // 菜单项文本
                            onClick = {
                                // 点击菜单项时更新选中的文本并关闭菜单
                                selectedOptionText = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding // 使用默认内边距
                        )
                    }
                }
            }
            // {{ /edit_2 }}
            //实现android常用的几种动画 ， 按钮点击显示
            // {{ edit_3 }}
            // 动画示例 1: AnimatedVisibility (按钮点击显示/隐藏)
            Text("动画示例 1: AnimatedVisibility")
            var showAnimatedText by remember { mutableStateOf(false) }
            Button(onClick = { showAnimatedText = !showAnimatedText }) {
                Text("点击切换动画文本显示")
            }
            AnimatedVisibility(visible = showAnimatedText) {
                Text("这是一个动画显示的文本！")
            }

            Spacer(modifier = Modifier.height(16.dp)) // 添加间距

            // 动画示例 2: animateDpAsState (按钮点击改变大小)
            Text("动画示例 2: animateDpAsState")
            var isLarge by remember { mutableStateOf(false) }
            val animatedSize by animateDpAsState(
                targetValue = if (isLarge) 100.dp else 50.dp,
                animationSpec = tween(durationMillis = 500), // 动画时长 500ms
                label = "sizeAnimation" // 添加 label 用于调试
            )
            Button(onClick = { isLarge = !isLarge }) {
                Text("点击切换方块大小")
            }
            Box(
                modifier = Modifier
                    .size(animatedSize) // 应用动画大小
                    .background(MaterialTheme.colorScheme.primary) // 使用主题颜色作为背景
            )
            // {{ /edit_3 }}

            // 添加更多内容以确保滚动
            Spacer(modifier = Modifier.height(50.dp))
            Text("更多内容...")
            Spacer(modifier = Modifier.height(50.dp))
            Text("滚动到底部")
        }
    }
}

// 预览 BMainScreen
@Preview(showBackground = true)
@Composable
fun PreviewBMainScreen() {
    LearnComposeTheme {
        BMainScreen()
    }
}
// {{ edit_4 }}