package com.example.foodie_finder

import androidx.lifecycle.ViewModel
import com.example.foodie_finder.model.UserModel

class MainActivityViewModel : ViewModel() {

    fun signOut() {
        UserModel.shared.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return UserModel.shared.isUserLoggedIn()
    }
}