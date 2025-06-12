package com.example.learncompose.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Query
import retrofit2.http.Header
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

// 定义 API 接口
interface ApiService {

    @GET("api/getHaoKanVideo")
    suspend fun getHaoKanVideos(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): VideoResponse // 返回我们定义的数据类

    @POST("api/sendVerificationCode")
    suspend fun sendVerificationCode(@Body request: SendVerificationCodeRequest): SendVerificationCodeResponse

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/getUserInfo")
    suspend fun getUserInfo(@Header("token") token: String): GetUserInfoResponse

}

// 创建 Retrofit 实例的单例对象
object RetrofitClient {
    private const val BASE_URL = "https://api.apiopen.top/"

    val apiService: ApiService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Optional: Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Optional: Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Optional: Set write timeout
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Set the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // 添加 Gson Converter
            .build()
            .create(ApiService::class.java) // 创建 ApiService 的实现
    }
}