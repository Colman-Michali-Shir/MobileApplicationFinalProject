package com.example.foodie_finder.model

import com.example.foodie_finder.base.Constants
import com.example.foodie_finder.base.EmptyCallback
import com.example.foodie_finder.base.GetAllStudentsCallback
import com.example.foodie_finder.base.GetStudentByIdCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {

    private val database = Firebase.firestore
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }

        database.firestoreSettings = setting
    }

    fun getAllStudents(callback: GetAllStudentsCallback) {
        database.collection(Constants.COLLECTIONS.STUDENTS).get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val students: MutableList<Student> = mutableListOf()
                        for (json in it.result) {
                            students.add(Student.fromJSON(json.data))
                        }
                        callback(students)
                    }

                    false -> callback(listOf())
                }
            }
    }

    fun getStudentById(id: String, callback: GetStudentByIdCallback) {
        database.collection(Constants.COLLECTIONS.STUDENTS).document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result.exists()) {
                    callback(Student.fromJSON(it.result.data ?: emptyMap()))
                } else {
                    throw NoSuchElementException("Student with ID $id not found")
                }
            }
    }

    fun add(student: Student, callback: EmptyCallback) {
        database.collection(Constants.COLLECTIONS.STUDENTS).document(student.id)
            .set(student.json)
            .addOnCompleteListener {
                callback()
            }
    }

    fun delete(student: Student, callback: EmptyCallback) {
        database.collection(Constants.COLLECTIONS.STUDENTS).document(student.id)
            .delete()
            .addOnCompleteListener {
                callback()
            }
    }

    fun update(student: Student, callback: EmptyCallback) {
        database.collection(Constants.COLLECTIONS.STUDENTS).document(student.id)
            .update(student.json)
            .addOnCompleteListener {
                callback()
            }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signIn(
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    callback(true, "Login successful: ${user?.email}", null)
                } else {
                    handleFirebaseError(callback, task.exception?.message)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        avatarUrl = null
                    )
                    database.collection("users").document(userId)
                        .set(user.json)
                        .addOnSuccessListener {
                            callback(true, "User registered successfully", null)
                        }
                        .addOnFailureListener { e ->
                            callback(false, "Failed to save user data: ${e.message}", null)
                        }
                } else {
                    handleFirebaseError(callback, task.exception?.message)
                }
            }
    }

    private fun handleFirebaseError(
        callback: (Boolean, String?, List<String>?) -> Unit,
        errorMessage: String?,
    ) {
        when {
            errorMessage?.contains("email address is already in use") == true -> {
                callback(false, "Email is already registered", listOf("email"))
            }

            errorMessage?.contains("The email address is badly formatted") == true -> {
                callback(false, "Invalid email format", listOf("email"))

            }

            errorMessage?.contains("Password should be at least 6 characters") == true -> {
                callback(false, "Password must be at least 6 characters", listOf("password"))
            }

            errorMessage?.contains("The supplied auth credential is incorrect") == true -> {
                callback(false, "Email or password is incorrect", listOf("email", "password"))
            }

            else -> {
                callback(false, errorMessage, null)
            }
        }
    }
}