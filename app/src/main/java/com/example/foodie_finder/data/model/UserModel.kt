package com.example.foodie_finder.data.model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference


class UserModel private constructor() {

    private val firebaseModel = FirebaseModel.getInstance()
    private val cloudinaryModel = CloudinaryModel.getInstance()

    private val _connectedUser = MutableLiveData<User?>()
    val loggedInUser: LiveData<User?> get() = _connectedUser

    var connectedUser: User?
        get() = _connectedUser.value
        set(value) {
            _connectedUser.postValue(value)
        }

    companion object {
        val shared = UserModel()
    }

    fun loadUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            firebaseModel.getConnectedUser { fetchedUser ->
                connectedUser = fetchedUser
            }
        } else {
            connectedUser = null
        }
    }


    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        firebaseModel.updateUser(user) {
            connectedUser = user
            profileImage?.let {
                cloudinaryModel.uploadImageToCloudinary(
                    image = it,
                    name = user.id,
                    onSuccess = { url ->
                        val userWithProfileImage = user.copy(avatarUrl = url)
                        firebaseModel.updateUser(userWithProfileImage, callback)
                        connectedUser = userWithProfileImage
                    },
                    onError = { callback(true) },
                    "profileImages"
                )

            } ?: callback(false)
        }
    }

    fun getConnectedUserRef(): DocumentReference? {
        return firebaseModel.getConnectedUserRef()
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        return firebaseModel.getUserById(userId, callback)
    }


    fun createUser(user: User, callback: (Boolean, String?, List<String>?) -> Unit) {
        firebaseModel.createUser(user) { isSuccessful ->
            if (isSuccessful) {
                if (connectedUser?.id == user.id) {
                    connectedUser = user
                }
                callback(true, "User created", null)
            } else {
                callback(false, "There was a creating the user", null)
            }
        }
    }
}