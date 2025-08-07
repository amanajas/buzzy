package com.buzzy.frequencyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buzzy.frequencyapp.data.DataManager

class FrequencyViewModelFactory(
    private val dataManager: DataManager
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FrequencyViewModel::class.java)) {
            return FrequencyViewModel(dataManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}