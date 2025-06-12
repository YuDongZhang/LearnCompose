package com.example.learncompose

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log // 导入 Log 用于打印日志
import androidx.lifecycle.lifecycleScope // 导入 lifecycleScope
import com.example.learncompose.network.RetrofitClient // 导入 RetrofitClient
import kotlinx.coroutines.launch // 导入 launch
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

class CMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContentView(R.layout.activity_cmain) // Remove this line
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        //     val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //     insets
        // }

        // Remove existing network request logic
        // fetchVideos()

        setContent {
            val viewModel: VideoListViewModel = viewModel()
            VideoListScreen(viewModel = viewModel)
        }
    }

    // Remove existing fetchVideos function
    // private fun fetchVideos() { /* ... */ }
}