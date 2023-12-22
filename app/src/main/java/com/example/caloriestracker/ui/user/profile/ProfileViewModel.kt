package com.example.caloriestracker.ui.user.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val _users = MutableLiveData<HashMap<String, String>>()
    val users: LiveData<HashMap<String, String>> get() = _users

    fun setUserProfile(context: Context, userDocRef: DocumentReference, email: String, username: String, phone: String) {
        val userProfile = hashMapOf(
            "email" to email,
            "username" to username,
            "phone" to phone
        )

        userDocRef.update(userProfile as Map<String, Any>)
            .addOnSuccessListener {
                Log.d(TAG, "Data pengguna berhasil diperbarui")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Gagal memperbarui data pengguna", e)
                Toast.makeText(context, "Gagal memperbarui data pengguna", Toast.LENGTH_SHORT).show()
            }

        _users.value = userProfile
    }


    fun createUserDocRef(userId: String): DocumentReference {
        val db = FirebaseFirestore.getInstance()
        return db.collection("users").document(userId)
    }



    companion object {
                private const val TAG = "ProfileViewModel"
    }
}
