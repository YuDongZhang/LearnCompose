package com.example.learncompose

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learncompose.network.DynamicItem
import com.example.learncompose.network.PublishDynamicRequest
import com.example.learncompose.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris: StateFlow<List<Uri>> = _selectedImageUris.asStateFlow()

    private val _postText = MutableStateFlow("")
    val postText: StateFlow<String> = _postText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    // Dynamic list states
    private val _dynamicList = MutableStateFlow<List<DynamicItem>>(emptyList())
    val dynamicList: StateFlow<List<DynamicItem>> = _dynamicList.asStateFlow()

    private val _isListLoading = MutableStateFlow(false)
    val isListLoading: StateFlow<Boolean> = _isListLoading.asStateFlow()

    private val _canLoadMoreList = MutableStateFlow(true)
    val canLoadMoreList: StateFlow<Boolean> = _canLoadMoreList.asStateFlow()

    private var currentPageList = 0
    private val pageSizeList = 5 // Define page size for dynamic list

    init {
        fetchDynamicList()
    }

    fun onImageSelected(uri: Uri) {
        _selectedImageUris.value = _selectedImageUris.value + uri
    }

    fun onPostTextChange(text: String) {
        _postText.value = text
    }

    fun publishDynamic() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = ""
            try {
                val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE)
                val token = sharedPreferences.getString("auth_token", null)

                if (token.isNullOrEmpty()) {
                    _message.value = "Authentication token not found. Please log in again."
                    _isLoading.value = false
                    return@launch
                }

                val uploadedImageUrls = mutableListOf<String>()

                _selectedImageUris.value.forEach { uri ->
                    val compressedImageBytes = compressImage(uri) // Compress image
                    if (compressedImageBytes != null) {
                        val requestBody = compressedImageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                        val multipartBodyPart = MultipartBody.Part.createFormData("file", "${java.util.UUID.randomUUID()}.jpg", requestBody)
                        val uploadResponse = RetrofitClient.apiService.uploadFile(multipartBodyPart)
                        if (uploadResponse.code == 200 && uploadResponse.result != null) {
                            val imageUrl = RetrofitClient.BASE_URL + uploadResponse.result.name.replaceFirst("/api/", "")
                            uploadedImageUrls.add(imageUrl)
                        } else {
                            _message.value = uploadResponse.message ?: "Image upload failed."
                            _isLoading.value = false
                            return@launch // Stop if any image upload fails
                        }
                    } else {
                        _message.value = "Failed to compress image."
                        _isLoading.value = false
                        return@launch
                    }
                }

                val publishRequest = PublishDynamicRequest(
                    images = uploadedImageUrls,
                    text = _postText.value
                )

                val publishResponse = RetrofitClient.apiService.publishDynamic(token = token, request = publishRequest)

                if (publishResponse.code == 200) {
                    _message.value = publishResponse.message ?: "Dynamic post published successfully!"
                    _selectedImageUris.value = emptyList() // Clear images after successful post
                    _postText.value = "" // Clear text after successful post
                    currentPageList = 0 // Reset page to refresh list after publishing
                    fetchDynamicList(isRefresh = true) // Refresh dynamic list
                } else {
                    _message.value = publishResponse.message ?: "Failed to publish dynamic post."
                }
            } catch (e: Exception) {
                Log.e("ActivityViewModel", "Error publishing dynamic post", e)
                _message.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchDynamicList(isRefresh: Boolean = false) {
        if (_isListLoading.value && !isRefresh) return

        viewModelScope.launch {
            _isListLoading.value = true
            try {
                val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE)
                val token = sharedPreferences.getString("auth_token", null)

                if (token.isNullOrEmpty()) {
                    _message.value = "Authentication token not found. Please log in again."
                    _isListLoading.value = false
                    return@launch
                }

                if (isRefresh) {
                    currentPageList = 0
                }

                val response = RetrofitClient.apiService.getPersonDynamicList(page = currentPageList, size = pageSizeList, token = token)
                if (response.code == 200 && response.result != null) {
                    val newItems = response.result.list // Access the list from the result object
                    if (newItems != null) {
                        if (isRefresh) {
                            _dynamicList.value = newItems
                        } else {
                            _dynamicList.value = _dynamicList.value + newItems
                        }
                        currentPageList++
                        _canLoadMoreList.value = newItems.size == pageSizeList
                    } else {
                        _message.value = response.message ?: "Failed to fetch dynamic list: Result list is null."
                        _canLoadMoreList.value = false
                    }
                } else {
                    _message.value = response.message ?: "Failed to fetch dynamic list."
                    _canLoadMoreList.value = false
                }
            } catch (e: Exception) {
                Log.e("ActivityViewModel", "Error fetching dynamic list", e)
                _message.value = "Network error: ${e.localizedMessage}"
                _canLoadMoreList.value = false
            } finally {
                _isListLoading.value = false
            }
        }
    }

    private fun compressImage(uri: Uri): ByteArray? {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2 // Scale down the image by 1/2
        val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        return if (bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            // Compress to JPEG with 80% quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.toByteArray()
        } else {
            null
        }
    }

    fun clearMessage() {
        _message.value = ""
    }

    fun removeImage(uri: Uri) {
        _selectedImageUris.value = _selectedImageUris.value.filter { it != uri }
    }
} 