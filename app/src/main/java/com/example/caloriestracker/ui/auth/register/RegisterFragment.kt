package com.example.caloriestracker.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.caloriestracker.MainActivity
import com.example.caloriestracker.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val registerBtn = binding.registerBtn
        registerBtn.setOnClickListener {
            registerUser()
        }

        return binding.root
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val phone = binding.etPhonenum.text.toString().trim()
        val bbTarget = binding.etTargetBB.text.toString().toDoubleOrNull() ?: 0.0
        val bb = binding.bbField.toString().toDoubleOrNull() ?: 0.0
        val tb = binding.etTb.text.toString().toDoubleOrNull() ?: 0.0

        binding.progressBar.visibility = View.VISIBLE

        viewModel.registerUser(email, password, username, phone, bbTarget, bb, tb) { success, message ->
            binding.progressBar.visibility = View.INVISIBLE
            if (success) {
                navigateToMainActivity()
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

