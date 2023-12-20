package com.example.caloriestracker.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun registerUser(
        email: String,
        password: String,
        username: String,
        phone: String,
        bbTarget: String,
        bb: String,
        tb: String,
        onRegisterComplete: (Boolean, String?) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()
            || phone.isEmpty() || bbTarget.isEmpty() || bb.isEmpty() || tb.isEmpty()
        ) {
            onRegisterComplete(false, "Isi semua data yang diperlukan")
            return
        }

        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid

                if (userId != null) {
                    saveUserDataToFirestore(userId, email, username, phone, bbTarget, bb, tb, "user")
                }

                onRegisterComplete(true, "Pendaftaran Berhasil")
            } catch (e: Exception) {
                onRegisterComplete(false, "Pendaftaran Gagal: ${e.message}")
            }
        }
    }

    private suspend fun saveUserDataToFirestore(
        userId: String,
        email: String,
        username: String,
        phone: String,
        bbTarget: String,
        bb: String,
        tb: String,
        role: String
    ) {
        val user = hashMapOf(
            "email" to email,
            "username" to username,
            "phone" to phone,
            "bbTarget" to bbTarget,
            "bb" to bb,
            "tb" to tb,
            "role" to role
        )

        val userRef = firestore.collection("users").document(userId)

        userRef.set(user).await()
    }
}
