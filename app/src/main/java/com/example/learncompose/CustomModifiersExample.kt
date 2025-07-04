package com.example.learncompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CustomModifiersExample() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ComposedModifierExample()
        ModifierNodeExample()
    }
}

@Composable
fun ComposedModifierExample() {
    LayoutTopicExampleCard("自定义修饰符 (Modifier.composed)") {
        Text("`Modifier.composed` 工厂函数用于创建有状态的修饰符。其 lambda 在组合期间执行，并且可以调用其他 @Composable 函数，例如 `remember` 和 `LaunchedEffect`。")
        Spacer(Modifier.height(8.dp))
        Text("缺点：每次重组时，`composed` 的 lambda 都会被重新执行，可能导致不必要的性能开销。")
        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .blinkingEffectWithComposed()
        )
    }
}

fun Modifier.blinkingEffectWithComposed(): Modifier = composed {
    // 使用 remember 来存储动画值
    val alpha = remember { Animatable(1f) }

    // 使用 LaunchedEffect 来启动协程执行动画
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // 应用动画值
    this.graphicsLayer {
        this.alpha = alpha.value
    }
}

@Composable
fun ModifierNodeExample() {
    LayoutTopicExampleCard("自定义修饰符 (Modifier.Node)") {
        Text("`Modifier.Node` 是创建自定义修饰符的现代、高效方式。它将有状态的逻辑与修饰符的创建分开，从而避免了 `composed` 的性能问题。")
        Spacer(Modifier.height(8.dp))
        Text("优点：性能更高，因为与状态相关的代码只在需要时（例如附加或更新时）运行，而不是在每次重组时都运行。")
        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
//                .blinkingEffectWithNode()
        )
    }
}

/*// 1. 创建一个 Modifier.Node 实现，它委托给一个 GraphicsLayerModifierNode
private class BlinkingNode : Modifier.Node(), DelegatingNode() {
    // 动画值
    private val alpha = Animatable(1f)

    // 委托给一个 GraphicsLayerModifierNode 来处理图形层变换。
    // 当 alpha.value 改变时，Compose 的快照系统会检测到，
    // 并重新执行这个 lambda 来更新图形层，从而触发重绘。
    private val graphicsLayerNode = delegate(GraphicsLayerModifierNode {
        this.alpha = this@BlinkingNode.alpha.value
    })

    override fun onAttach() {
        // 当节点附加到布局树时，在与节点生命周期绑定的协程作用域中启动动画。
        // 当节点分离时，该协程会自动取消。
        coroutineScope.launch {
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }
}

// 2. 创建一个 ModifierNodeElement 来实例化 Node
private data object BlinkingElement : ModifierNodeElement<BlinkingNode>() {
    override fun create(): BlinkingNode = BlinkingNode()
    override fun update(node: BlinkingNode) { }
    override fun InspectorInfo.inspectableProperties() { name = "blinking" }
}

// 3. 创建一个易于使用的扩展函数
fun Modifier.blinkingEffectWithNode() = this.then(BlinkingElement)*/
