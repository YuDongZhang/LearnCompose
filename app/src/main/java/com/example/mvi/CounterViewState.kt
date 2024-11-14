package com.example.mvi
//这个 CounterViewState 类用于表示 UI 的状态，包括计数器的值、加载状态和可能的错误信息。
data class CounterViewState(
    val counter: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
