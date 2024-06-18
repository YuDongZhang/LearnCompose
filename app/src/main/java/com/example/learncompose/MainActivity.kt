package com.example.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learncompose.ui.theme.LearnComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                            name = "Android 测试",
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

//@Composable 如需使函数成为可组合函数，请添加 @Composable 注解。
//Modifier 就是一个控制边界的东西
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background) ,
            contentDescription = "你好肾宝",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        //边距
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
            Spacer(modifier = Modifier.height(4.dp))//还是间距
            Text(text = "hahha")
        }
    }

}

//@Preview  用来预览的
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearnComposeTheme {
        Greeting("Android")
    }
}