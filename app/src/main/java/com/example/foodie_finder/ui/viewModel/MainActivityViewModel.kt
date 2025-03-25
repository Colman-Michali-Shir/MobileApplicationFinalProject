package com.example.foodie_finder.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.model.UserModel

class MainActivityViewModel : ViewModel() {

    fun initUser() {
        UserModel.shared.loadUser { user ->
            if (user != null) {
                Log.d("UserModel", "Logged in as: ${user.id}")
            } else {
                Log.d("UserModel", "No user logged in")
            }
        }
    }

    fun signOut() {
        UserModel.shared.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return UserModel.shared.isUserLoggedIn()
    }
}