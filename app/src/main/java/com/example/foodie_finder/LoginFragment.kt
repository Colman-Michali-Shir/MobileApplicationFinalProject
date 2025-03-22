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
            val email = binding?.emailEditText?.text.toString().trim()
            val password = binding?.passwordEditText?.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email and password are required",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            Model.shared.signIn(email, password) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToStudentsListFragment()
                    binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                } else {
                    Toast.makeText(requireContext(), message ?: "Login failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding?.registerButton?.setOnClickListener {
            // Navigate to RegisterFragment
            Toast.makeText(requireContext(), "Register", Toast.LENGTH_SHORT).show()

            val action =
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
