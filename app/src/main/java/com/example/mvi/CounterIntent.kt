package com.example.mvi
//CounterIntent 是一个密封类，用于表示用户在界面上的操作意图。这里定义了两个意图：增加计数和减少计数。
sealed class CounterIntent {
    object Increment : CounterIntent()
    object Decrement : CounterIntent()
}
