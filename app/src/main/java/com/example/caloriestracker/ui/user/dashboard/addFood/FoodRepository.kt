package com.example.caloriestracker.ui.user.dashboard.addFood

import androidx.lifecycle.LiveData
import com.example.caloriestracker.data.database.UserDao
import com.example.caloriestracker.data.model.room.UserMenu

class FoodRepository(private val userDao: UserDao) {
    val readAllData: LiveData<List<UserMenu>> = userDao.getAllMakanan()

    fun addMakanan (userMenu: UserMenu){
        userDao.insert(userMenu)
    }
}