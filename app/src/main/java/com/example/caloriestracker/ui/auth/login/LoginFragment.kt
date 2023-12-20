package com.example.caloriestracker.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.caloriestracker.MainActivity
import com.example.caloriestracker.databinding.FragmentLoginBinding
import com.example.caloriestracker.ui.admin.AdminMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            // Melakukan proses login menggunakan Firebase Authentication
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Jika login berhasil, cek role pengguna dan navigasikan ke halaman yang sesuai
                        checkUserRole()
                    } else {
                        // Jika login gagal, tampilkan pesan kesalahan
                        Toast.makeText(
                            activity,
                            "Login Gagal: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        return view
    }

    private fun checkUserRole() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val role = document.getString("role")

                        if (role == "admin") {
                            startActivity(Intent(activity, AdminMainActivity::class.java))
                            activity?.finish()
                        } else {
                            // Navigasi ke halaman user
                            startActivity(Intent(activity, MainActivity::class.java))
                            activity?.finish()
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"

        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
