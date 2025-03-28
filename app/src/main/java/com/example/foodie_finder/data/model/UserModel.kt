package com.example.foodie_finder.data.model

import android.graphics.Bitmap
import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference


class UserModel private constructor() {

    private val firebaseModel = FirebaseModel.getInstance()
    private val cloudinaryModel = CloudinaryModel.getInstance()

    var user: User? = null
        private set

    companion object {
        val shared = UserModel()
    }

    fun loadUser(callback: (User?) -> Unit) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            firebaseModel.getConnectedUser { fetchedUser ->
                user = fetchedUser
                callback(fetchedUser)
            }
        } else {
            user = null
            callback(null)
        }
    }


    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        firebaseModel.updateUser(user) {
            this.user = user
            profileImage?.let {
                cloudinaryModel.uploadImageToCloudinary(
                    image = it,
                    name = user.id,
                    onSuccess = { url ->
                        val userWithProfileImage = user.copy(avatarUrl = url)
                        firebaseModel.updateUser(userWithProfileImage, callback)
                        this.user = userWithProfileImage
                    },
                    onError = { callback(true) },
                    "profileImages"
                )

            } ?: callback(false)
        }
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseModel.isUserLoggedIn()
    }

    fun getUser(callback: (User?) -> Unit) {
        return firebaseModel.getConnectedUser(callback)
    }

    fun getConnectedUserRef(): DocumentReference? {
        return firebaseModel.getConnectedUserRef()
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