package com.example.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
//CounterViewModel 中有一个 viewState，用于暴露当前的 CounterViewState，
// 它会随着用户的意图变化而更新。通过 processIntent 方法处理用户的 CounterIntent。
class CounterViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(CounterViewState())
    val viewState: StateFlow<CounterViewState> = _viewState

    fun processIntent(intent: CounterIntent) {
        viewModelScope.launch {
            when (intent) {
                is CounterIntent.Increment -> incrementCounter()
                is CounterIntent.Decrement -> decrementCounter()
            }
        }
    }

    private fun incrementCounter() {
        val currentCounter = _viewState.value.counter
        _viewState.value = _viewState.value.copy(counter = currentCounter + 1)
    }

    private fun decrementCounter() {
        val currentCounter = _viewState.value.counter
        _viewState.value = _viewState.value.copy(counter = currentCounter - 1)
    }
}