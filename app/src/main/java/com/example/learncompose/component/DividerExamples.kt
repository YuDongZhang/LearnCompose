package com.example.learncompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/components/divider?hl=zh-cn
 * Divider 用于分隔列表或容器中的项。Compose 提供了 HorizontalDivider 和 VerticalDivider。
 */

@Composable
fun DividerExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("HorizontalDivider 示例", style = MaterialTheme.typography.titleMedium)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("第一项")
            HorizontalDivider(thickness = 2.dp)
            Text("第二项")
        }
        Spacer(Modifier.height(16.dp))
        Text("VerticalDivider 示例", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("左侧项")
            VerticalDivider(color = MaterialTheme.colorScheme.secondary, thickness = 2.dp)
            Text("右侧项")
        }
    }
} 