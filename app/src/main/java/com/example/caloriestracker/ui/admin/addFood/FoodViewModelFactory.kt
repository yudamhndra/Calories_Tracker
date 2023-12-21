package com.example.caloriestracker.ui.admin.addFood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class FoodViewModelFactory(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



