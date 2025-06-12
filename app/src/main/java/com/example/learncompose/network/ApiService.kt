package com.example.learncompose.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 定义 API 接口
interface ApiService {

    @GET("api/getHaoKanVideo")
    suspend fun getHaoKanVideos(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): VideoResponse // 返回我们定义的数据类

}

// 创建 Retrofit 实例的单例对象
object RetrofitClient {
    private const val BASE_URL = "https://api.apiopen.top/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // 添加 Gson Converter
            .build()
            .create(ApiService::class.java) // 创建 ApiService 的实现
    }
}