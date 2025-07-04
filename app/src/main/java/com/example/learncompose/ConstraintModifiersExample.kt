package com.example.learncompose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialLayoutsExample() {
    LayoutTopicExampleCard("Scaffold: Material 布局结构") {
        Text("Scaffold 实现了一个基本的 Material Design 布局结构。它提供了多个槽位（Slot），你可以用它来放置 TopAppBar, BottomAppBar, FloatingActionButton 等。")
        Spacer(Modifier.height(8.dp))
        Text("我们当前这个详情页本身就是一个 Scaffold 布局，它包含了 TopAppBar 和一个 LazyColumn 作为内容。")
    }
}
