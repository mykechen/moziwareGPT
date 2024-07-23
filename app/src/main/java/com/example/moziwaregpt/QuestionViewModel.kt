package com.example.moziwaregpt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionViewModel : ViewModel() {
    private val _apiResponse = MutableStateFlow<APIResponse?>(null)
    val apiResponse: StateFlow<APIResponse?> = _apiResponse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getAnswer(question: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiInterface.getAnswer(question)
                if (response.isSuccessful) {
                    _apiResponse.value = response.body()
                } else {
                    // TODO: Handle Error
                }
            } catch (e: Exception){
                // TODO: Handle Exception
            } finally {
                _isLoading.value = false
            }
        }
    }
}