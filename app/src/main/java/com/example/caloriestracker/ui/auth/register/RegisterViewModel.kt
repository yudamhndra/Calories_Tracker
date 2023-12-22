package com.example.caloriestracker.ui.auth.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caloriestracker.ui.auth.session.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel(private val context: Context) : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val sessionManager = SessionManager(context)

    fun registerUser(
        email: String,
        password: String,
        username: String,
        phone: String,
        bbTarget: Double,
        bb: Double,
        tb: Double,
        onRegisterComplete: (Boolean, String?) -> Unit
    ) {
        // Validasi data
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()
            || phone.isEmpty()
        ) {
            onRegisterComplete(false, "Isi semua data yang diperlukan")
            return
        }

        // Memulai coroutine
        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                if (authResult.user != null) {
                    val userId = authResult.user!!.uid

                    // Memastikan langkah pembuatan pengguna selesai sebelum melanjutkan
                    authResult.user!!.getIdToken(true).await()

                    // Simpan data pengguna ke Firestore
                    saveUserDataToFirestore(userId, email, username, phone, bbTarget, bb, tb, "user")

                    // Convert Double values to String before saving to SharedPreferences
                    val bbTargetString = bbTarget.toString()
                    val bbString = bb.toString()
                    val tbString = tb.toString()

                    // Simpan informasi pengguna ke SharedPreferences
                    sessionManager.saveUserInfo(userId, email, username, phone, bbTargetString, bbString, tbString, "user")

                    // Pendaftaran berhasil
                    onRegisterComplete(true, "Pendaftaran Berhasil")
                } else {
                    // Handle kesalahan dalam mendapatkan userId
                    onRegisterComplete(false, "Gagal mendapatkan ID pengguna setelah pendaftaran")
                }
            } catch (e: Exception) {
                // Handle kesalahan pendaftaran
                onRegisterComplete(false, "Pendaftaran Gagal: ${e.message}")
            }
        }
    }

    private suspend fun saveUserDataToFirestore(
        userId: String,
        email: String,
        username: String,
        phone: String,
        bbTarget: Double,
        bb: Double,
        tb: Double,
        role: String
    ) {
        // Data pengguna yang akan disimpan di Firestore
        val user = hashMapOf(
            "userId" to userId, // Tambahkan userId ke data pengguna
            "email" to email,
            "username" to username,
            "phone" to phone,
            "bbTarget" to bbTarget,
            "bb" to bb,
            "tb" to tb,
            "role" to role
        )

        val userRef = firestore.collection("users").document(userId)

        userRef.set(user, SetOptions.merge()).await()
    }
}
