package com.example.foodie_finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.foodie_finder.databinding.FragmentLoginBinding
import com.example.foodie_finder.model.Model

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.loginButton?.setOnClickListener {
            loginAction()
        }

        binding?.registerButton?.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun loginAction() {
        val email = binding?.emailEditText?.text.toString().trim()
        val password = binding?.passwordEditText?.text.toString().trim()


        binding?.emailInputLayout?.error = null
        binding?.passwordInputLayout?.error = null

        var isValid = true

        if (email.isEmpty()) {
            binding?.emailInputLayout?.error = "Email is required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding?.passwordInputLayout?.error = "Password is required"
            isValid = false
        }

        if (!isValid) return


        Model.shared.signIn(email, password) { success, message, errorFields ->
            if (success) {
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_LONG).show()
                val action =
                    LoginFragmentDirections.actionLoginFragmentToStudentsListFragment()
                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
            } else {
                errorFields?.forEach { field ->
                    when (field) {
                        "password" -> binding?.passwordInputLayout?.error = message
                        "email" -> binding?.emailInputLayout?.error = message
                    }
                }

                // Show toast if no specific error fields
                if (errorFields.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        message ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
