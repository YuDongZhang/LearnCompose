package com.example.learncompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learncompose.network.RetrofitClient
import com.example.learncompose.network.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class VideoListViewModel : ViewModel() {

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _canLoadMore = MutableStateFlow(true)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private var currentPage = 0
    private val pageSize = 10 // Define your page size

    init {
        loadMoreVideos()
    }

    fun refreshVideos() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentPage = 0 // Reset page for refreshing
            _canLoadMore.value = true // Allow loading more after refresh
            fetchVideos(isRefresh = true)
            _isRefreshing.value = false
        }
    }

    fun loadMoreVideos() {
        if (_isLoading.value || !_canLoadMore.value) return

        viewModelScope.launch {
            _isLoading.value = true
            fetchVideos(isRefresh = false)
            _isLoading.value = false
        }
    }

    private suspend fun fetchVideos(isRefresh: Boolean) {
        try {
            val response = RetrofitClient.apiService.getHaoKanVideos(page = currentPage, size = pageSize)
            if (response.code == 200) {
                Log.d("VideoListViewModel", "API call successful. Current page: $currentPage")
                val newVideos = response.result.list
                if (isRefresh) {
                    _videos.value = newVideos
                } else {
                    _videos.value = _videos.value + newVideos
                }
                currentPage++
                // If the number of new videos is less than the page size, it means there are no more videos to load.
                _canLoadMore.value = newVideos.size == pageSize
            } else {
                Log.e("VideoListViewModel", "Error fetching videos: ${response.code} - ${response.message}")
                _canLoadMore.value = false // Stop loading more on error
            }
        } catch (e: Exception) {
            Log.e("VideoListViewModel", "Exception fetching videos", e)
            _canLoadMore.value = false // Stop loading more on exception
        }
    }
} 