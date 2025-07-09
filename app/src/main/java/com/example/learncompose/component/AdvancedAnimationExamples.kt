package com.example.learncompose.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.example.learncompose.ComponentExampleCard
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun AdvancedAnimationExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // 1. 手势驱动的动画 (Gesture-based animation)
        GestureAnimationExample()

        // 2. 前瞻布局 (LookaheadLayout)
        
        LookaheadLayoutExample()
    }
}

@Composable
private fun GestureAnimationExample() {
    ComponentExampleCard(title = "手势驱动动画") {
        Text("使用 pointerInput 和 Animatable 实现可拖拽和带回弹效果的动画。")
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            DraggableBall()
        }
    }
}

@Composable
private fun DraggableBall() {
    // Animatable 用于存储和动画化一个值（这里是 Offset）。
    // 它可以在协程作用域内被控制，非常适合手势驱动的动画。
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            // 使用 offset 修饰符根据 Animatable 的当前值来移动组件
            .offset { offset.value.round() }
            // pointerInput 用于捕获原始的指针事件（如触摸、拖动）
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        // 等待用户按下屏幕
                        val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                        // 停止任何正在进行的动画，以响应新的用户输入
                        offset.stop()
                        // 启动一个协程来处理拖动事件
                        launch {
                            // 开始拖动
                            awaitPointerEventScope {
                                drag(pointerId) { change ->
                                    // 在协程中根据用户手指的移动来更新偏移量
                                    launch {
                                        offset.snapTo(offset.value + change.positionChange())
                                    }
                                }
                            }
                        }
                        // 当用户抬起手指时，上面的 drag 会结束
                        // 启动一个新协程，让小球带动画地返回中心点 (Offset.Zero)
                        launch {
                            offset.animateTo(
                                targetValue = Offset.Zero,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                }
            }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LookaheadLayoutExample() {
    ComponentExampleCard(title = "LookaheadLayout (前瞻布局)") {
        var isExpanded by remember { mutableStateOf(false) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
        ) {
            Text("点击此区域可在两种布局间切换。LookaheadLayout 可以在布局变化时平滑地为子元素添加动画。")
            Spacer(Modifier.height(16.dp))

            // LookaheadLayout 允许其子项在布局变化前后“预先计算”它们的目标位置和大小。
            // 这使得像 animateContentSize 和 intermediateLayout 这样的修饰符能够创建平滑的过渡动画。
            LookaheadScope {
                val containerModifier = if (isExpanded) {
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                } else {
                    Modifier.fillMaxWidth()
                }
                SharedElementContainer(isExpanded, containerModifier)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LookaheadScope.SharedElementContainer(isExpanded: Boolean, modifier: Modifier) {
    val contentModifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .animateContentSize() // 当内容大小改变时，平滑地过渡
        .padding(16.dp)

    if (isExpanded) {
        Column(modifier.then(contentModifier)) {
            CircleImage()
            Spacer(Modifier.height(16.dp))
            Text("这是一个展开后的布局，元素垂直排列。")
        }
    } else {
        Row(
            modifier.then(contentModifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleImage()
            Spacer(Modifier.width(16.dp))
            Text("这是一个收起后的布局。")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LookaheadScope.CircleImage() {
    Box(
        modifier = Modifier
            // 这个修饰符是 LookaheadLayout 的一部分，用于标记共享元素
            // 它会测量元素在 Lookahead pass 中的大小和位置，
            // 并在实际布局 pass 中使用这些信息来动画化到目标状态。
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
            .size(80.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}
