package com.example.foodie_finder.model

import android.util.Log
import com.example.foodie_finder.base.Constants
import com.example.foodie_finder.base.EmptyCallback
import com.example.foodie_finder.base.GetAllStudentsCallback
import com.example.foodie_finder.base.GetStudentByIdCallback
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {

    private val database = Firebase.firestore

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }

        database.firestoreSettings = setting

//        val auth = Firebase.auth
//
////        auth.createUserWithEmailAndPassword("talzi@colman.ac.il", "supperStrong")
//
//        auth.currentUser?.uid?.let {
//
//            Log.i("TAG", auth.currentUser?.uid ?: "No use uuid")
//
//            val json = hashMapOf(
//                "name" to "Tal",
//                "email" to "talzi@colman.ac.il"
//            )
//            database.collection("users").document(it).set(json)
//                .addOnCompleteListener {
//                    Log.i("TAG", auth.currentUser?.uid + "Saved" ?: "No use uuid")
//                }
//        }
    }

    fun getAllStudents(callback: GetAllStudentsCallback) {
        database.collection(Constants.COLLECTIONS.STUDENTS).get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        Log.d("TAG", it.result.toString())
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
}

/*
val db = Firebase.firestore

// Create a new user with a first and last name
val user = hashMapOf(
    "first" to "Ada",
    "last" to "Lovelace",
    "born" to 1815,
)

// Add a new document with a generated ID
db.collection("users")
.add(user)
.addOnSuccessListener { documentReference ->
    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
}
.addOnFailureListener { e ->
    Log.w("TAG", "Error adding document", e)
}

 */