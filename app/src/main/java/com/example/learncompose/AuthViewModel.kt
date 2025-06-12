package com.example.learncompose

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

class AuthViewModel(application: Application) : AndroidViewModel(application) {

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

    private val _navigateToMain = MutableStateFlow(false)
    val navigateToMain: StateFlow<Boolean> = _navigateToMain.asStateFlow()

    init {
        // Load saved credentials when the ViewModel is initialized
        val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE)
        _account.value = sharedPreferences.getString("saved_account", "") ?: ""
        _password.value = sharedPreferences.getString("saved_password", "") ?: ""
    }

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
                    // Save token and navigate
                    val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("auth_token", response.result.token)
                        putString("user_id", response.result.userId)
                        putString("saved_account", _account.value)
                        putString("saved_password", _password.value)
                        apply()
                    }
                    Log.d("AuthViewModel", "Logged in token: ${response.result.token}")
                    _navigateToMain.value = true
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

    fun resetNavigation() {
        _navigateToMain.value = false
    }
} 