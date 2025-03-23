package com.example.foodie_finder

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.model.User
import com.example.foodie_finder.model.UserModel

class ProfileViewModel : ViewModel() {

    var user: User?
        get() = UserModel.shared.user
        set(value) {
            UserModel.shared.user = value
        }

    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        UserModel.shared.updateUser(user, profileImage, callback)
    }

    fun getUser(callback: (User?) -> Unit) {
        return UserModel.shared.getUser(callback)
    }
}