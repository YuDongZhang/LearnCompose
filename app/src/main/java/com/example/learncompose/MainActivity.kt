package com.example.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background) ,
            contentDescription = "你好肾宝")
        Column {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
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