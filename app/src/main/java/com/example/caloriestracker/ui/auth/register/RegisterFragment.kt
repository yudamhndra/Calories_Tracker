package com.example.caloriestracker.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.caloriestracker.MainActivity
import com.example.caloriestracker.R
import com.example.caloriestracker.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

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
        val bbTarget = binding.etTargetBB.text.toString().trim()
        val bb = binding.bbField.text.toString().trim()
        val tb = binding.etTb.text.toString().trim()

        viewModel.registerUser(email, password, username, phone, bbTarget, bb, tb) { success, message ->
            if (success) {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
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
