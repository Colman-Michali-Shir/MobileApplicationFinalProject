package com.example.foodie_finder

import androidx.lifecycle.ViewModel
import com.example.foodie_finder.model.UserModel

class RegisterViewModel : ViewModel() {
    
    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        UserModel.shared.signUp(firstName, lastName, email, password, callback)
    }
}