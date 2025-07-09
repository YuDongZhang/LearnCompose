package com.example.learncompose.component

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard
import kotlinx.coroutines.launch


@Composable
internal fun ValueBasedAnimationExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. Animatable: 提供了对动画更精细的控制
        AnimatableExample01()

        // 2. AnimationSpec: 自定义动画规格
        AnimationSpecExamples()
    }
}

@Composable
private fun AnimatableExample01() {
    ComponentExampleCard(title = "Animatable (可动画化)") {
        var isExpanded by remember { mutableStateOf(false) }
        // Animatable 可以在协程作用域内通过 animateTo 控制动画。
        // 这对于实现手势驱动的动画或需要中断动画的场景非常有用。
        // 它提供了比 animate*AsState 更灵活的控制。
        val color = remember { Animatable(Color.Gray) }
        val size: Animatable<Dp, AnimationVector1D> = remember { Animatable(100.dp, Dp.VectorConverter) }
        val coroutineScope = rememberCoroutineScope()
        val primaryColor = MaterialTheme.colorScheme.primary
        // 当 isExpanded 状态改变时，启动一个协程来执行动画。
        // LaunchedEffect 会在 isExpanded 变化时取消并重启协程。
        LaunchedEffect(isExpanded) {
            // animateTo 是一个挂起函数，它会按顺序执行动画。
            launch {
                color.animateTo(
                    if (isExpanded) primaryColor else Color.Gray,
                    animationSpec = tween(durationMillis = 500)
                )
            }
            launch {
                size.animateTo(
                    if (isExpanded) 200.dp else 100.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("Animatable 允许在协程中驱动动画，可以实现动画中断和更复杂的逻辑。")
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(size.value)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color.value)
                    .clickable { isExpanded = !isExpanded }
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { isExpanded = !isExpanded }) {
                    Text(if (isExpanded) "收起" else "展开")
                }
                // snapTo 可以立即将值设置为目标值，而没有动画效果。
                Button(onClick = {
                    coroutineScope.launch {
                        // 立即切换到收起状态
                        launch { size.snapTo(100.dp) }
                        launch { color.snapTo(Color.Gray) }
                        // 更新状态以匹配，这样下次 animateTo 才会正确
                        isExpanded = false
                    }
                }) {
                    Text("立即收起 (Snap)")
                }
            }
        }
    }
}

@Composable
private fun AnimationSpecExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Spring 动画
        ComponentExampleCard(title = "AnimationSpec - spring (弹簧动画)") {
            var isMoved by remember { mutableStateOf(false) }
            val offset by animateDpAsState(
                targetValue = if (isMoved) 48.dp else 0.dp,
                // spring 动画规格模拟物理弹簧效果
                // dampingRatio: 阻尼比，控制弹跳的程度
                // stiffness: 刚度，影响弹簧恢复到最终位置的速度
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "spring offset"
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Spring 动画基于物理模型，使动画看起来更自然、生动。")
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .offset(x = offset)
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { isMoved = !isMoved }) {
                    Text("移动")
                }
            }
        }

        // Keyframes 动画
        ComponentExampleCard(title = "AnimationSpec - keyframes (关键帧动画)") {
            var isToggled by remember { mutableStateOf(false) }
            val secondaryColor = MaterialTheme.colorScheme.primary
            val primaryContainerColor = MaterialTheme.colorScheme.primaryContainer

            val color by animateColorAsState(
                targetValue = if (isToggled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                // keyframes 允许你在动画的不同时间点指定特定的值
                animationSpec = keyframes {
                    durationMillis = 1500 // 动画总时长
                    // 在 500ms 时，颜色变为 a，并使用 LinearEasing
                    secondaryColor.at(500).with(LinearEasing)
                    // 在 1000ms 时，颜色变为 b，并使用 FastOutSlowInEasing
                    primaryContainerColor.at(1000).with(FastOutSlowInEasing)

                },
                label = "keyframes color"
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Keyframes 允许动画在不同时间点经过指定的值，实现更复杂的动画路径。")
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { isToggled = !isToggled }) {
                    Text("开始动画")
                }
            }
        }
    }
}