package com.example.learncompose.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.learncompose.ComponentExampleCard
import com.example.learncompose.R

@Composable
internal fun ImageExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 1. 基础图像
        ComponentExampleCard(title = "基础图像") {
            Text("从 Drawable 资源加载图像。")
            Spacer(Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "应用启动图标", // contentDescription 对无障碍性很重要
                modifier = Modifier.size(100.dp)
            )
        }

        // 2. 网络图像 (使用 Coil)
        ComponentExampleCard(title = "网络图像 (Coil)") {
            Text("使用 Coil 库从网络加载图像。需要 INTERNET 权限。")
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = "https://picsum.photos/300",
                contentDescription = "随机网络图片",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Crop
            )
        }

        // 3. 图像形状
        ComponentExampleCard(title = "圆形图像") {
            Text("使用 clip 修饰符将图像裁剪为圆形。")
            Spacer(Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.dog),
                contentDescription = "一只可爱的狗",
                contentScale = ContentScale.Crop, // 裁剪以填充圆形
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }

        // 4. ContentScale 示例
        ComponentExampleCard(title = "ContentScale (内容缩放)") {
            Text("控制图像如何适应其布局空间。")
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Crop (裁剪)")
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = "裁剪",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Fit (适应)")
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = "适应",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("FillBounds (填充)")
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = "填充",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }
    }
}
