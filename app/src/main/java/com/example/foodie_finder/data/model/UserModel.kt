package com.example.foodie_finder.data.model

import android.graphics.Bitmap
import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel


class UserModel private constructor() {

    private val firebaseModel = FirebaseModel.getInstance()
    private val cloudinaryModel = CloudinaryModel.getInstance()

    var user: User? = null

    companion object {
        val shared = UserModel()
    }

    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        profileImage?.let {
            cloudinaryModel.uploadImageToCloudinary(
                image = it,
                name = user.id,
                onSuccess = { url ->
                    val userWithProfileImage = user.copy(avatarUrl = url)
                    firebaseModel.updateUser(userWithProfileImage, callback)
                },
                onError = { callback(true) },
                "profileImages"
            )

        } ?: callback(false)

    }

    fun isUserLoggedIn(): Boolean {
        return firebaseModel.isUserLoggedIn()
    }

    fun getUser(callback: (User?) -> Unit) {
        return firebaseModel.getUser(callback)
    }

    fun signIn(
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        firebaseModel.signIn(email, password, callback)
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        firebaseModel.signUp(firstName, lastName, email, password, callback)
    }

    fun signOut() {
        firebaseModel.signOut()
    }
}