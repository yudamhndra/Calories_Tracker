package com.example.caloriestracker.data.model.room

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "adminmenu")
data class AdminMenu (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    val nama: String,
    val kalori: Int = 0,
    val isSynced: Boolean = false,
    val firebaseId: String? = null
)