package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.local.UserModel

class MainActivityViewModel : ViewModel() {

    fun signOut() {
        UserModel.shared.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return UserModel.shared.isUserLoggedIn()
    }
}