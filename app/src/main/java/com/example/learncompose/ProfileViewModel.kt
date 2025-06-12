package com.example.learncompose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.learncompose.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.learncompose.network.GetUserInfoResponse
import com.example.learncompose.network.UserInfoResult

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _userInfo = MutableStateFlow<UserInfoResult?>(null)
    val userInfo: StateFlow<UserInfoResult?> = _userInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchUserInfo()
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE)
                val token = sharedPreferences.getString("auth_token", null)

                if (token.isNullOrEmpty()) {
                    _errorMessage.value = "Authentication token not found. Please log in again."
                    _isLoading.value = false
                    return@launch
                }

                val response = RetrofitClient.apiService.getUserInfo(token = token)
                if (response.code == 200 && response.result != null) {
                    _userInfo.value = response.result
                    Log.d("ProfileViewModel", "User Info fetched: ${response.result}")
                } else {
                    _errorMessage.value = response.message ?: "Failed to fetch user info."
                    Log.e("ProfileViewModel", "Error fetching user info: ${response.code} - ${response.message}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                Log.e("ProfileViewModel", "Exception fetching user info", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
} 