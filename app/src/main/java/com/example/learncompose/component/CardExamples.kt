package com.example.learncompose.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

@Composable
internal fun CardExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ComponentExampleCard("填充卡片 (默认)") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(Modifier.fillMaxSize().padding(16.dp).align(Alignment.CenterHorizontally)) {
                    Text("这是一个标准的填充卡片。")
                }
            }
        }

        ComponentExampleCard("悬浮卡片") {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(Modifier.fillMaxSize().padding(16.dp).align(Alignment.CenterHorizontally)) {
                    Text("这张卡片有更高的悬浮效果。")
                }
            }
        }

        ComponentExampleCard("描边卡片") {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Box(Modifier.fillMaxSize().padding(16.dp).align(Alignment.CenterHorizontally)) {
                    Text("这张卡片带有一个描边。")
                }
            }
        }
    }
}