package com.example.foodie_finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.foodie_finder.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var binding: FragmentRegisterBinding? = null
    private var viewModel: RegisterViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding?.loginButton?.setOnClickListener {
            binding?.root?.let { Navigation.findNavController(it).popBackStack() }
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

        viewModel?.signUp(firstName, lastName, email, password) { success, message, errorFields ->
            if (success) {
                Toast.makeText(requireContext(), "Register successful!", Toast.LENGTH_LONG)
                    .show()

                val action =
                    RegisterFragmentDirections.actionRegisterFragmentToStudentsListFragment()
                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
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
        }
    }
}
