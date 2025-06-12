package com.example.learncompose

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// {{ edit_1 }}
import android.util.Log // 导入 Log 用于打印日志
import androidx.lifecycle.lifecycleScope // 导入 lifecycleScope
import com.example.learncompose.network.RetrofitClient // 导入 RetrofitClient
import kotlinx.coroutines.launch // 导入 launch
// {{ /edit_1 }}

class CMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cmain)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // {{ edit_2 }}
        // 在 onCreate 中执行网络请求
        fetchVideos()
        // {{ /edit_2 }}
    }

    // {{ edit_3 }}
    // 定义一个函数来执行网络请求
    private fun fetchVideos() {
        // 使用 lifecycleScope 在 Activity 的生命周期内启动协程
        lifecycleScope.launch {
            try {
                // 调用 Retrofit API 服务的方法
                val response = RetrofitClient.apiService.getHaoKanVideos(page = 0, size = 2)

                // 检查响应码或直接处理数据
                if (response.code == 200) {
                    Log.d("NetworkRequest", "请求成功: ${response.message}")
                    Log.d("NetworkRequest", "视频列表: ${response.result.list}")
                    // TODO: 在这里处理获取到的视频列表数据，例如更新 UI
                } else {
                    Log.e("NetworkRequest", "请求失败: ${response.code} - ${response.message}")
                    // TODO: 处理请求失败的情况
                }
            } catch (e: Exception) {
                // 处理网络请求过程中发生的异常
                Log.e("NetworkRequest", "网络请求异常", e)
                // TODO: 处理异常情况，例如显示错误信息给用户
            }
        }
    }
    // {{ /edit_3 }}
}