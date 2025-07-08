package com.example.learncompose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

@Composable
internal fun CheckboxExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. 基础复选框
        ComponentExampleCard(title = "基础复选框") {
            var checked by remember { mutableStateOf(true) }
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }

        // 2. 带标签的复选框
        ComponentExampleCard(title = "带标签的复选框") {
            var checked by remember { mutableStateOf(true) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it }
                )
                Spacer(Modifier.width(8.dp))
                Text("同意条款和条件")
            }
        }

        // 3. 禁用的复选框
        ComponentExampleCard(title = "禁用的复选框") {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = null,
                        enabled = false
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("已禁用 (已选中)")
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = null,
                        enabled = false
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("已禁用 (未选中)")
                }
            }
        }

        // 4. 三态复选框
        ComponentExampleCard(title = "三态复选框") {
            val (state, onStateChange) = remember { mutableStateOf(true) }
            val (state2, onStateChange2) = remember { mutableStateOf(true) }

            // TriStateCheckbox 的状态由其子项的状态驱动
            val parentState = remember(state, state2) {
                if (state && state2) ToggleableState.On
                else if (!state && !state2) ToggleableState.Off
                else ToggleableState.Indeterminate
            }
            val onParentClick = {
                val s = parentState != ToggleableState.On
                onStateChange(s)
                onStateChange2(s)
            }

            Column {
                Text("当子选项的状态不一致时，父复选框可以显示为“不确定”状态。")
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TriStateCheckbox(
                        state = parentState,
                        onClick = onParentClick,
                    )
                    Text("所有选项")
                }
                Column(Modifier.padding(start = 32.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(state, onStateChange)
                        Text("选项 A")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(state2, onStateChange2)
                        Text("选项 B")
                    }
                }
            }
        }
    }
}