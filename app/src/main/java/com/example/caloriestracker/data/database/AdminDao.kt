package com.example.caloriestracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.caloriestracker.data.model.firestore.FoodItem
import com.example.caloriestracker.data.model.room.AdminMenu

@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (menu : AdminMenu)

    @Update
    fun update(menu: AdminMenu)

    @Delete
    fun delete(menu: AdminMenu)

    @Query("SELECT * FROM adminmenu")
    fun getAllFoodItems(): List<AdminMenu>

    @Query("SELECT * FROM adminmenu WHERE id = :itemId")
    fun getFoodItemById(itemId: Long): AdminMenu?

    @get:Query("SELECT * from adminmenu ORDER BY id ASC")
    val allFoodItem: LiveData<List<AdminMenu>>

    @Query("SELECT * FROM adminmenu WHERE isSynced = 0")
    fun getUnsyncedFoodItems(): List<AdminMenu>

    @Query("UPDATE adminmenu SET isSynced = :isSynced WHERE id = :itemId")
    fun updateSyncStatus(itemId: Long, isSynced: Boolean)

    @Query("UPDATE adminmenu SET firebaseId = :firebaseId WHERE id = :itemId")
    fun updateFirebaseId(itemId: Long, firebaseId: String)

}