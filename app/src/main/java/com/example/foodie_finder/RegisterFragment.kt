package com.example.foodie_finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.foodie_finder.databinding.FragmentRegisterBinding
import com.example.foodie_finder.model.Model

class RegisterFragment : Fragment() {

    private var binding: FragmentRegisterBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding?.loginButton?.setOnClickListener {
            // Navigate to RegisterFragment
            Toast.makeText(requireContext(), "Login", Toast.LENGTH_SHORT).show()

            val action =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
        }
        binding?.signUpButton?.setOnClickListener {
            val email = binding?.emailEditText?.text.toString().trim()
            val password = binding?.passwordEditText?.text.toString().trim()
            val firstName = binding?.firstNameEditText?.text.toString().trim()
            val lastName = binding?.lastNameEditText?.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email and password are required",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            Model.shared.signUp(firstName, lastName, email, password) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Register successful!", Toast.LENGTH_SHORT)
                        .show()
         
                    binding?.root?.let { Navigation.findNavController(it).popBackStack() }
                } else {
                    Toast.makeText(
                        requireContext(),
                        message ?: "Register failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
