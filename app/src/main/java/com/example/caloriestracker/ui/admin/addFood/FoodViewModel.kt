package com.example.caloriestracker.ui.admin.addFood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriestracker.data.model.firestore.FoodItem
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _foodList = MutableLiveData<List<FoodItem>>()
    val foodList: LiveData<List<FoodItem>> get() = _foodList

    init {
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        viewModelScope.launch {
            val result = repository.getFoodListFromFirestore()
            _foodList.postValue(result)
        }
    }


}


