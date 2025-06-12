package com.example.learncompose.network

import com.google.gson.annotations.SerializedName

// Common response structure for API calls
data class ApiResponse<T>( // Generic API response wrapper
    val code: Int,
    val message: String,
    val result: T? // Nullable for responses that don't return specific data
)

// Request body for sending verification code
data class SendVerificationCodeRequest(
    val mail: String
)

// Response for sending verification code (result field can be null or a simple string message)
typealias SendVerificationCodeResponse = ApiResponse<String>

// Request body for user registration
data class RegisterRequest(
    val account: String,
    val password: String,
    val code: String,
)

// Response for registration (result field can be null or a simple string message)
typealias RegisterResponse = ApiResponse<String>

// Request body for user login
data class LoginRequest(
    val account: String,
    val password: String
)

// Response for login (result field contains a token or user info)
data class LoginResponseResult(
    val token: String,
    val userId: String,
    // Add other user-related data you might receive upon successful login
)

typealias LoginResponse = ApiResponse<LoginResponseResult>

// Response for getting user info
data class UserInfoResult(
    val birthday: String?,
    val blurb: String?,
    val head_url: String?,
    val name: String?,
    val updatedAt: String?
)

typealias GetUserInfoResponse = ApiResponse<UserInfoResult>

// Response for Upload File API
data class UploadFileResponseResult(
    val size: Long,
    val name: String
)

typealias UploadFileResponse = ApiResponse<UploadFileResponseResult>

// Request body for publishing dynamic content
data class PublishDynamicRequest(
    val images: List<String>,
    val text: String
)

// Response for publishing dynamic content
data class PublishDynamicResponseResult(
    val images: List<String>,
    val text: String
)

typealias PublishDynamicResponse = ApiResponse<PublishDynamicResponseResult>

//// Response for Get User Info API
//data class GetUserInfoResponseResult(
//    val birthday: String,
//    val blurb: String,
//    @SerializedName("head_url") val headUrl: String,
//    val name: String,
//    val updatedAt: String
//)
//
//typealias GetUserInfoResponse = ApiResponse<GetUserInfoResponseResult>