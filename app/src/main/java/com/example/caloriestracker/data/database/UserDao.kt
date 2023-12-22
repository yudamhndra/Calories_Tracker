package com.example.caloriestracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.caloriestracker.data.model.room.UserMenu

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(menu: UserMenu)
    @Update
    fun update(menu: UserMenu)
    @Delete
    fun delete(menu: UserMenu)

    @Query("SELECT * FROM usermenu WHERE userId = :userId")
    fun getMakananByUserId(userId: String): LiveData<List<UserMenu>>

    @Query("SELECT * FROM usermenu")
    fun getAllMakanan(): LiveData<List<UserMenu>>

    @Query("SELECT SUM(foodCalorie) FROM usermenu WHERE userId = :userId")
    fun getTotalCalories(userId: String): Int

    @Query("SELECT * FROM usermenu WHERE userId = :userId AND foodName LIKE :searchQuery")
    fun searchMakananByUserId(userId: String, searchQuery: String): LiveData<List<UserMenu>>

}