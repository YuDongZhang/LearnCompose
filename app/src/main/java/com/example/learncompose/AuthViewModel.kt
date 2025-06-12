package com.example.learncompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.learncompose.network.RetrofitClient
import com.example.learncompose.network.SendVerificationCodeRequest
import com.example.learncompose.network.RegisterRequest
import com.example.learncompose.network.LoginRequest

class AuthViewModel : ViewModel() {

    private val _account = MutableStateFlow("")
    val account: StateFlow<String> = _account.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _verificationCode = MutableStateFlow("")
    val verificationCode: StateFlow<String> = _verificationCode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    fun onAccountChange(newAccount: String) {
        _account.value = newAccount
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onVerificationCodeChange(newCode: String) {
        _verificationCode.value = newCode
    }

    fun sendVerificationCode() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""
            try {
                val request = SendVerificationCodeRequest(mail = _account.value)
                val response = RetrofitClient.apiService.sendVerificationCode(request)
                if (response.code == 200) {
                    _message.value = response.message ?: "Verification code sent successfully!"
                } else {
                    _message.value = response.message ?: "Failed to send verification code."
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error sending verification code", e)
                _message.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""
            try {
                val request = RegisterRequest(
                    account = _account.value,
                    password = _password.value,
                    code = _verificationCode.value
                )
                val response = RetrofitClient.apiService.register(request)
                if (response.code == 200) {
                    _message.value = response.message ?: "Registration successful!"
                    // Optionally navigate to login or main screen
                } else {
                    _message.value = response.message ?: "Registration failed."
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error registering user", e)
                _message.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""
            try {
                val request = LoginRequest(
                    account = _account.value,
                    password = _password.value
                )
                val response = RetrofitClient.apiService.login(request)
                if (response.code == 200 && response.result != null) {
                    _message.value = response.message ?: "Login successful!"
                    // TODO: Handle successful login (e.g., save token, navigate to main screen)
                    Log.d("AuthViewModel", "Logged in token: ${response.result.token}")
                } else {
                    _message.value = response.message ?: "Login failed."
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Error logging in user", e)
                _message.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
} 