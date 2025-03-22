package com.example.foodie_finder.model

import android.graphics.Bitmap
import com.example.foodie_finder.base.EmptyCallback
import com.example.foodie_finder.base.GetAllStudentsCallback
import com.example.foodie_finder.base.GetStudentByIdCallback


class Model private constructor() {

    //    private val executor = Executors.newSingleThreadExecutor()
//    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
//    private val database: AppLocalDbRepository = AppLocalDb.database
    private val firebaseModel = FirebaseModel()
    private val cloudinaryModel = CloudinaryModel()

    companion object {
        val shared = Model()
    }

    fun deleteStudent(student: Student, callback: EmptyCallback) {
        firebaseModel.delete(student, callback)
    }

    fun addStudent(student: Student, profileImage: Bitmap?, callback: EmptyCallback) {
        firebaseModel.add(student) {
            profileImage?.let {
                uploadImageToCloudinary(
                    image = it,
                    name = student.id,
                    onSuccess = { url ->
                        val st = student.copy(avatarUrl = url)
                        firebaseModel.add(st, callback)
                    },
                    onError = { callback() }
                )

            } ?: callback()
        }

    }

    fun updateStudent(student: Student, callback: EmptyCallback) {
        firebaseModel.update(student, callback)
    }

    fun updateUser(user: User, profileImage: Bitmap?, callback: (Boolean) -> Unit) {
        firebaseModel.updateUser(user) {
            profileImage?.let {
                uploadImageToCloudinary(
                    image = it,
                    name = user.id,
                    onSuccess = { url ->
                        val userWithProfileImage = user.copy(avatarUrl = url)
                        firebaseModel.updateUser(userWithProfileImage, callback)
                    },
                    onError = { callback(true) }
                )

            } ?: callback(false)
        }
    }

    fun getStudentById(id: String, callback: GetStudentByIdCallback) {
        firebaseModel.getStudentById(id, callback)
    }


    fun getAllStudents(callback: GetAllStudentsCallback) {
        firebaseModel.getAllStudents(callback)
    }

    fun signOut() {
        firebaseModel.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseModel.isUserLoggedIn()
    }

    fun getUser(callback: (User?) -> Unit) {
        return firebaseModel.getUser(callback)
    }

    private fun uploadImageToCloudinary(
        image: Bitmap,
        name: String?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        cloudinaryModel.uploadBitmap(
            bitmap = image,
            name = name,
            onSuccess = onSuccess,
            onError = onError
        )
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
}