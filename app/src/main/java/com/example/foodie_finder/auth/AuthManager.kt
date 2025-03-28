package com.example.foodie_finder.auth

import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.data.model.UserModel
import com.google.firebase.auth.FirebaseAuth

class AuthManager private constructor() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    companion object {
        val shared = AuthManager()
    }

    fun signIn(
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                connectUser(it.user?.uid ?: "")
                callback(true, "User signed in successfully", null)
            }
            .addOnFailureListener {
                handleFirebaseError(callback, it.message)
            }
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it.user?.let { firebaseUser ->
                    val user = User(firebaseUser.uid, email, firstName, lastName, null)
                    UserModel.shared.createUser(user) { success, message, errorFields ->
                        if (success) {
                            connectUser(user.id)
                        }
                        callback(success, message, errorFields)

                    }
                }
            }
            .addOnFailureListener {
                handleFirebaseError(callback, it.message)
            }
    }

    fun signOut() {
        auth.signOut()
        UserModel.shared.connectedUser = null
    }

    fun connectUser(userId: String) {
        UserModel.shared.getUserById(userId) { user ->
            UserModel.shared.connectedUser = user
        }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserUid(): String? {
        return auth.uid
    }


    private fun handleFirebaseError(
        callback: (Boolean, String?, List<String>?) -> Unit,
        errorMessage: String?
    ) {
        when {
            errorMessage?.contains(
                "email address is already in use",
                ignoreCase = true
            ) == true -> {
                callback(false, "Email is already registered", listOf("email"))
            }

            errorMessage?.contains(
                "The email address is badly formatted",
                ignoreCase = true
            ) == true -> {
                callback(false, "Invalid email format", listOf("email"))
            }

            errorMessage?.contains(
                "Password should be at least 6 characters",
                ignoreCase = true
            ) == true -> {
                callback(false, "Password must be at least 6 characters", listOf("password"))
            }

            errorMessage?.contains(
                "The supplied auth credential is incorrect",
                ignoreCase = true
            ) == true -> {
                callback(false, "Email or password is incorrect", listOf("email", "password"))
            }

            else -> {
                callback(false, errorMessage ?: "Unknown error", null)
            }
        }
    }
}