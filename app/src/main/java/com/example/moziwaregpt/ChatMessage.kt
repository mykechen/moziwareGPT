package com.example.moziwaregpt

data class ChatMessage (val content: String, val isUser: Boolean, val advice: Advice? = null){

}