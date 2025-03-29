package com.example.foodie_finder.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.foodie_finder.auth.AuthManager
import com.example.foodie_finder.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var binding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding?.loginButton?.setOnClickListener {
            binding?.root?.findNavController()?.popBackStack()
        }

        binding?.signUpButton?.setOnClickListener {
            signUpAction()
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun signUpAction() {
        val email = binding?.emailEditText?.text.toString().trim()
        val password = binding?.passwordEditText?.text.toString().trim()
        val passwordConfirm = binding?.confirmPasswordEditText?.text.toString().trim()
        val firstName = binding?.firstNameEditText?.text.toString().trim()
        val lastName = binding?.lastNameEditText?.text.toString().trim()

        binding?.progressBar?.visibility = View.VISIBLE

        binding?.emailInputLayout?.error = null
        binding?.passwordInputLayout?.error = null
        binding?.confirmPasswordInputLayout?.error = null

        var isValid = true

        if (email.isEmpty()) {
            binding?.emailInputLayout?.error = "Email is required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding?.passwordInputLayout?.error = "Password is required"
            isValid = false
        }

        if (passwordConfirm != password) {
            binding?.confirmPasswordInputLayout?.error = "Passwords do not match"
            binding?.passwordInputLayout?.error = "Passwords do not match"
            isValid = false
        }

        if (!isValid) return

        AuthManager.shared.signUp(
            firstName,
            lastName,
            email,
            password
        ) { success, message, errorFields ->
            if (success) {
                Toast.makeText(requireContext(), "Register successful!", Toast.LENGTH_LONG)
                    .show()

                val action =
                    RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                binding?.root?.findNavController()?.navigate(action)
            } else {
                errorFields?.forEach { field ->
                    when (field) {
                        "password" -> binding?.passwordInputLayout?.error = message
                        "email" -> binding?.emailInputLayout?.error = message
                    }
                }

                if (errorFields.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        message ?: "Register failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            binding?.progressBar?.visibility = View.GONE
        }
    }
}
