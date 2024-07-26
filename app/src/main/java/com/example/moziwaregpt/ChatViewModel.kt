package com.example.moziwaregpt

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Add the initial assistant message
        addMessage(ChatMessage("您好，我是您的AI助手\n有什么可以帮您？快来向我提问吧～", false))
    }

    fun addMessage(message: ChatMessage) {
        _messages.add(message)
    }

    fun clearMessages(){
        _messages.clear()
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}