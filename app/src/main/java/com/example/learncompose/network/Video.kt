package com.example.learncompose.network

import com.google.gson.annotations.SerializedName

// 整体响应结构
data class VideoResponse(
    val code: Int,
    val message: String,
    val result: VideoResult
)

// result 字段的结构
data class VideoResult(
    val total: Int,
    val list: List<Video>
)

// list 中的每个视频对象结构
data class Video(
    val id: Int,
    val title: String,
    val userName: String,
    val userPic: String,
    val coverUrl: String,
    val playUrl: String,
    val duration: String
)