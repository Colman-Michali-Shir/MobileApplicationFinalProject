package com.example.foodie_finder.ui.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.data.model.PostModel
import com.example.foodie_finder.data.model.UserModel

class ProfileViewModel : ViewModel() {

    var user = UserModel.shared.user
    var userPosts: LiveData<List<Post>> = PostModel.shared.usersPosts

    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        UserModel.shared.updateUser(user, profileImage, callback)
    }

    fun getUser(callback: (User?) -> Unit) {
        return UserModel.shared.getUser(callback)
    }

    fun refreshUsersPosts() {
        PostModel.shared.refreshUsersPosts()
    }
}