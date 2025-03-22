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

    fun signIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    callback(true, "Login successful: ${user?.email}")
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(email = email, firstName = firstName, lastName = lastName)
                    database.collection("users").document(userId)
                        .set(user.json)
                        .addOnSuccessListener {
                            callback(true, "User registered successfully")
                        }
                        .addOnFailureListener { e ->
                            callback(false, "Failed to save user data: ${e.message}")
                        }
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }
}