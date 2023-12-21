package com.example.caloriestracker.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun loginUser(email: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(firebaseAuth.currentUser?.uid ?: "")
                } else {
                    onError(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun getUserRole(userId: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val userRef = firestore.collection("users").document(userId)
        userRef.get()
            .addOnSuccessListener { document ->
                val role = document?.getString("role") ?: ""
                onSuccess(role)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Failed to get user role")
            }
    }
}
