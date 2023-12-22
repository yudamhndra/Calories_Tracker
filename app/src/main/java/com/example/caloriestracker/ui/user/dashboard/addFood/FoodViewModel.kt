package com.example.caloriestracker.ui.user.dashboard.addFood

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.caloriestracker.data.database.AppDatabase
import com.example.caloriestracker.data.model.room.UserMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<UserMenu>>
    private val repository: FoodRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDAO()
        repository = userDao?.let { FoodRepository(it) }!!
        readAllData = repository.readAllData
    }

    fun addMakanan(userMenu: UserMenu) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMakanan(userMenu)
        }
    }
}
