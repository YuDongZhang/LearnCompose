package com.example.learncompose.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

// 用于 updateTransition 示例的状态枚举
private enum class BoxState {
    Collapsed,
    Expanded
}

@Composable
internal fun AnimationExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. animate*AsState: 简单的值动画
        ComponentExampleCard(title = "animate*AsState (值动画)") {
            var enabled by remember { mutableStateOf(true) }

            // animateColorAsState 用于在颜色值之间平滑过渡
            val color by animateColorAsState(
                targetValue = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                animationSpec = tween(durationMillis = 500),
                label = "color animation"
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("点击方块切换颜色")
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .clickable { enabled = !enabled }
                )
            }
        }

        // 2. AnimatedVisibility: 内容出现和消失动画
        ComponentExampleCard(title = "AnimatedVisibility (可见性动画)") {
            var visible by remember { mutableStateOf(true) }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { visible = !visible }) {
                    Text(if (visible) "隐藏" else "显示")
                }
                Spacer(Modifier.height(16.dp))

                // AnimatedVisibility 会在其内容出现或消失时应用动画
                AnimatedVisibility(
                    visible = visible,
                    // enter 定义了进入动画
                    enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(initialAlpha = 0.3f),
                    // exit 定义了退出动画
                    exit = slideOutVertically(targetOffsetY = { -40 }) + fadeOut(targetAlpha = 0f)
                ) {
                    Text(
                        "这段文字会带有动画效果的出现和消失",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(16.dp)
                    )
                }
            }
        }

        // 3. animateContentSize: 内容尺寸变化动画
        ComponentExampleCard(title = "animateContentSize (尺寸变化动画)") {
            var expanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    // animateContentSize 修饰符可以使内容的尺寸变化过程更加平滑自然
                    .animateContentSize(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
                    .clickable { expanded = !expanded }
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "点击我展开或收起。\n".repeat(if (expanded) 5 else 1) +
                            "当内容大小改变时，此修饰符会平滑地动画化其尺寸。",
                    maxLines = if (expanded) Int.MAX_VALUE else 2
                )
            }
        }

        // 4. Crossfade: 内容切换动画
        ComponentExampleCard(title = "Crossfade (交叉淡入淡出)") {
            var currentPage by remember { mutableStateOf("A") }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { currentPage = "A" }) { Text("页面 A") }
                    Button(onClick = { currentPage = "B" }) { Text("页面 B") }
                }
                Spacer(Modifier.height(16.dp))

                // Crossfade 用于在两个布局之间平滑地进行交叉淡入淡出切换
                Crossfade(targetState = currentPage, animationSpec = tween(durationMillis = 500), label = "crossfade") { screen ->
                    when (screen) {
                        "A" -> Text("这是页面 A", style = MaterialTheme.typography.headlineMedium)
                        "B" -> Text("这是页面 B", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }

        // 5. updateTransition: 多个值同时动画
        ComponentExampleCard(title = "updateTransition (多值动画)") {
            var boxState by remember { mutableStateOf(BoxState.Collapsed) }
            // 创建一个 Transition 对象，它会跟踪 boxState 的变化
            val transition = updateTransition(targetState = boxState, label = "box state transition")

            // 使用 transition.animate* API 来为每个属性创建动画
            // 当 boxState 变化时，这些值会平滑地过渡到新状态的目标值
            val color by transition.animateColor(label = "color") { state ->
                when (state) {
                    BoxState.Collapsed -> MaterialTheme.colorScheme.primary
                    BoxState.Expanded -> MaterialTheme.colorScheme.secondary
                }
            }
            val size by transition.animateDp(label = "size") { state ->
                when (state) {
                    BoxState.Collapsed -> 64.dp
                    BoxState.Expanded -> 128.dp
                }
            }
            val cornerRadius by transition.animateDp(label = "corner radius") { state ->
                when (state) {
                    BoxState.Collapsed -> 8.dp
                    BoxState.Expanded -> 24.dp
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("点击方块来同时改变颜色、大小和圆角")
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(color)
                        .clickable {
                            boxState = if (boxState == BoxState.Expanded) BoxState.Collapsed else BoxState.Expanded
                        }
                )
            }
        }

        // 6. rememberInfiniteTransition: 无限循环动画
        ComponentExampleCard(title = "rememberInfiniteTransition (无限循环动画)") {
            // 创建一个无限循环的 transition
            val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

            // 使用 infiniteTransition.animate* API 创建一个在两个值之间无限循环的动画
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000),
                    repeatMode = RepeatMode.Reverse // 动画会正向播放，然后反向播放
                ), label = "alpha"
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("这是一个无限循环的呼吸效果")
                Spacer(Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Pulsing Icon",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                )
            }
        }
    }
}