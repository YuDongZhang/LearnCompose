package com.example.learncompose.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.offset

// 这是一个演示如何自定义 Compose 动画的示例文件

@Composable
fun CustomizeAnimationExampleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "自定义 Compose 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TweenAnimationExample()
        Spacer(modifier = Modifier.height(16.dp))

        SpringAnimationExample()
        Spacer(modifier = Modifier.height(16.dp))

        KeyframesAnimationExample()
        Spacer(modifier = Modifier.height(16.dp))

        InfiniteTransitionExample()
        Spacer(modifier = Modifier.height(16.dp))

        AnimatableExample()
        Spacer(modifier = Modifier.height(16.dp))

        AnimateValueExample()
        Spacer(modifier = Modifier.height(16.dp))

        AnimateContentSizeExample()
        Spacer(modifier = Modifier.height(16.dp))

        // 共享元素动画示例入口
        SharedElementExampleEntry()
    }
}

@Composable
fun TweenAnimationExample() {
    var sizeState by remember { mutableStateOf(50.dp) }

    val size by animateDpAsState(
        targetValue = sizeState,
        animationSpec = tween(
            durationMillis = 1000, // 动画时长 1000ms
            delayMillis = 100, // 延迟 100ms 后开始
            easing = FastOutSlowInEasing // 使用加速然后减速的插值器
        ),
        label = "sizeAnimation"
    )

    Column {
        Text(
            "Tween 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(size)
                .background(Color.Blue)
                .clickable { sizeState += 50.dp }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { sizeState = if (sizeState == 50.dp) 150.dp else 50.dp }) {
            Text("切换大小")
        }
    }
}

@Composable
fun SpringAnimationExample() {
    var sizeState by remember { mutableStateOf(50.dp) }
    val size by animateDpAsState(
        targetValue = sizeState,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, // 阻尼比，控制弹性程度
            stiffness = Spring.StiffnessLow // 刚度，控制动画速度
        ),
        label = "springAnimation"
    )

    Column {
        Text(
            "Spring 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(size)
                .background(Color.Green)
                .clickable { sizeState += 50.dp }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { sizeState = if (sizeState == 50.dp) 150.dp else 50.dp }) {
            Text("切换大小")
        }
    }
}

@Composable
fun KeyframesAnimationExample() {
    var colorState by remember { mutableStateOf(Color.Red) }
    val color by animateColorAsState(
        targetValue = colorState,
        animationSpec = keyframes {
            durationMillis = 1000 // 动画总时长
            Color.Red at 0 // 在 0ms 时颜色为红色
            Color.Yellow at 200 with LinearEasing // 在 200ms 时颜色为黄色，使用线性插值
            Color.Green at 500 with FastOutLinearInEasing // 在 500ms 时颜色为绿色
            Color.Blue at 1000 with LinearOutSlowInEasing // 在 1000ms 时颜色为蓝色
        },
        label = "colorAnimation"
    )

    Column {
        Text(
            "Keyframes 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color)
                .clickable { colorState = if (colorState == Color.Red) Color.Blue else Color.Red }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { colorState = if (colorState == Color.Red) Color.Blue else Color.Red }) {
            Text("切换颜色")
        }
    }
}

@Composable
fun InfiniteTransitionExample() {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite color transition")
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Blue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "infinite color"
    )

    Column {
        Text(
            "Infinite Transition 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color)
        )
    }
}

@Composable
fun AnimatableExample() {
    val animatedSize = remember { Animatable(50f) }
    val coroutineScope = rememberCoroutineScope()
    Column {
        Text(
            "Animatable 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(animatedSize.value.dp)
                .background(Color.Magenta)
                .clickable {
                    // 使用 launch 启动一个协程来运行动画
                    // targetValue 可以是任何 Float
                    // animationSpec 可以是 tween, spring, keyframes 等
                    // initialVelocity 是动画的初始速度
                    // block 动画结束后执行的代码块
                    // suspend fun animateTo(targetValue: T, animationSpec: AnimationSpec<T>, initialVelocity: T = targetValue) {

                    // suspend fun animateDecay(
                    //     initialVelocity: T,
                    //     animationSpec: DecayAnimationSpec<T>,
                    //     block: (value: T, velocity: T) -> Unit = { _, _ -> }
                    // ) {

                    // suspend fun snapTo(targetValue: T)
                    // suspend fun stop()

                    // 使用 launch 启动一个协程来运行动画
                    // targetValue 可以是任何 Float
                    // animationSpec 可以是 tween, spring, keyframes 等
                    // initialVelocity 是动画的初始速度
                    // block 动画结束后执行的代码块
                    coroutineScope.launch {
                        animatedSize.animateTo(
                            targetValue = if (animatedSize.value == 50f) 150f else 50f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                    }
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                animatedSize.animateTo(
                    targetValue = if (animatedSize.value == 50f) 150f else 50f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            }
        }) {
            Text("切换大小")
        }
    }
}

@Composable
fun AnimateValueExample() {
    var scaleState by remember { mutableStateOf(1f) }
    val animatedScale by animateValueAsState(
        targetValue = scaleState,
        typeConverter = Float.VectorConverter, // 使用 Float 的 VectorConverter
        animationSpec = tween(durationMillis = 500),
        label = "scaleAnimation"
    )

    Column {
        Text(
            "animateValueAsState 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(100.dp * animatedScale) // 将动画值应用于大小
                .background(Color.Cyan)
                .clickable { scaleState = if (scaleState == 1f) 1.5f else 1f }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { scaleState = if (scaleState == 1f) 1.5f else 1f }) {
            Text("切换缩放")
        }
    }
}

@Composable
fun AnimateContentSizeExample() {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            "animateContentSize 动画示例",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .background(Color.Yellow)
                .clickable { expanded = !expanded }
                .animateContentSize() // 对内容大小变化应用动画
                .padding(16.dp)
        ) {
            Text(
                text = "点击这里来展开或折叠内容。".repeat(if (expanded) 3 else 1),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "折叠" else "展开")
        }
    }
}

/**
 * 共享元素动画示例
 * 参考：https://developer.android.com/develop/ui/compose/animation/shared-elements?hl=zh-cn
 *
 * 该示例演示了两个 Box 之间的共享元素动画，点击切换时，红色方块会平滑移动到蓝色方块的位置。
 * 使用 SharedTransitionLayout 和 sharedElementWithCallerManagedVisibility 实现。
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementExampleEntry() {
    var selectFirst by remember { mutableStateOf(true) }
    val key = remember { Any() }

    Text(
        "共享元素动画示例 (点击下方区域切换)",
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(8.dp))
    androidx.compose.animation.SharedTransitionLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color(0xFFF5F5F5))
            .clickable { selectFirst = !selectFirst }
            .padding(10.dp)
    ) {
        // 第一个 Box
        if (selectFirst) {
            Box(
                Modifier
                    .sharedElementWithCallerManagedVisibility(
                        rememberSharedContentState(key = key),
                        visible = true
                    )
                    .background(Color.Red)
                    .size(100.dp)
            ) {
                Text("A", color = Color.White, modifier = Modifier.align(Alignment.Center))
            }
        }
        // 第二个 Box
        if (!selectFirst) {
            Box(
                Modifier
                    .offset(100.dp, 80.dp)
                    .sharedElementWithCallerManagedVisibility(
                        rememberSharedContentState(key = key),
                        visible = true
                    )
                    .background(Color.Blue)
                    .size(100.dp)
            ) {
                Text("B", color = Color.White, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        "点击上方灰色区域，红蓝方块会平滑切换位置，演示共享元素动画。",
        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomizeAnimationExampleScreen() {
    CustomizeAnimationExampleScreen()
}
