package com.example.caloriestracker.ui.admin.addFood

import com.example.caloriestracker.data.model.FoodItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FoodRepository(private val firestore: FirebaseFirestore) {

    suspend fun getFoodListFromFirestore(): List<FoodItem> {
        val result = firestore.collection("makanan").get().await()
        return result.documents.mapNotNull { document ->
            val nama = document.getString("nama")
            val kalori = document.getDouble("kalori")
            if (nama != null && kalori != null) {
                FoodItem(nama, kalori)
            } else {
                null
            }
        }
    }
}

