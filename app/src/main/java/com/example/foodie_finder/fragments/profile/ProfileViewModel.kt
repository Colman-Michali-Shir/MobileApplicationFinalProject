package com.example.foodie_finder.fragments.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.entities.Post
import com.example.foodie_finder.data.entities.User
import com.example.foodie_finder.data.model.PostModel
import com.example.foodie_finder.data.model.UserModel

class ProfileViewModel : ViewModel() {

    var user: LiveData<User?> = UserModel.shared.loggedInUser
    var userPosts: LiveData<List<Post>> = PostModel.shared.usersPosts
    var savedPosts: LiveData<List<String>> = PostModel.shared.savedPosts

    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        UserModel.shared.updateUser(user, profileImage, callback)
    }

    fun refreshUsersPosts() {
        PostModel.shared.refreshUsersPosts()
    }

    fun refreshSavedPosts() {
        PostModel.shared.getSavedPosts()
    }

    fun savePost(postId: String, callback: (Boolean) -> Unit) {
        PostModel.shared.savePost(postId, callback)
    }

    fun removeSavedPost(postId: String, callback: (Boolean) -> Unit) {
        PostModel.shared.removeSavedPost(postId, callback)
    }
}