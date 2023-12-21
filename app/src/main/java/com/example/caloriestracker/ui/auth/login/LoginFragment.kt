package com.example.caloriestracker.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.caloriestracker.MainActivity
import com.example.caloriestracker.databinding.FragmentLoginBinding
import com.example.caloriestracker.ui.admin.AdminMainActivity
import com.example.caloriestracker.ui.auth.session.SessionManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            binding.progressBar.visibility = View.VISIBLE

            viewModel.loginUser(
                email,
                password,
                { userId -> checkUserRole(userId) },
                { errorMessage -> showToast("Login Gagal: $errorMessage")
                    binding.progressBar.visibility = View.INVISIBLE}

            )
        }

        return view
    }

    private fun checkUserRole(userId: String) {
        viewModel.getUserRole(
            userId,
            { role ->
                saveUserInfoAndNavigate(userId, role)
            },
            { errorMessage ->
                showToast("Failed to get user role: $errorMessage")
            }
        )
    }

    private fun saveUserInfoAndNavigate(userId: String, role: String) {
        val email = binding.loginEmail.text.toString()

        SessionManager(requireContext()).saveUserInfo(
            userId, email, "", "", "", "", "", role
        )

        navigateToCorrectPage(role)
    }


    private fun navigateToCorrectPage(role: String) {
        if (role == "admin") {
            startActivity(Intent(activity, AdminMainActivity::class.java))
        } else {
            startActivity(Intent(activity, MainActivity::class.java))
        }
        activity?.finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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

