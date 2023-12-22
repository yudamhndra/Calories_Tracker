package com.example.caloriestracker.ui.admin.addFood

import com.example.caloriestracker.data.model.firestore.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FoodRepository(private val firestore: FirebaseFirestore) {

    suspend fun getFoodListFromFirestore(): List<FoodItem> {
        val result = firestore.collection("makanan").get().await()
        return result.documents.mapNotNull { document ->
            val id = document.id
            val nama = document.getString("nama")
            val kalori = document.getDouble("kalori")
            if (nama != null && kalori != null) {
                FoodItem(id, nama, kalori)
            } else {
                null
            }
        }
    }
}


