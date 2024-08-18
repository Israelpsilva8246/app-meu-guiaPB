package com.ufpb.meuguia.viewmodel

import ApiService
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufpb.meuguia.model.Attraction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttractionViewModel() : ViewModel() {

    var attractionListResponse: List<Attraction> by mutableStateOf(listOf())
    var errorMessage: String by mutableStateOf("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getAttractionList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val apiService = ApiService.getInstance()
                val attractionList = apiService.getAttractions()
                attractionListResponse = attractionList
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
