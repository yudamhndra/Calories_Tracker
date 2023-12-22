package com.example.caloriestracker.data.model.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "usermenu")
data class UserMenu (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    var userId: String = "",
    val foodName: String = "",
    @ColumnInfo(name = "foodCalorie")
    val foodCalorie: Int = 0,
    val serving: Int = 0,
    val type: String //waktu makan: pagi, siang, malam
    )