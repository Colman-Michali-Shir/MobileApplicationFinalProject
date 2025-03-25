package com.example.foodie_finder.data.model

import android.graphics.Bitmap
import com.example.foodie_finder.base.EmptyCallback
import com.example.foodie_finder.base.GetStudentByIdCallback
import com.example.foodie_finder.data.local.Student
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel


class Model private constructor() {

    //    private val executor = Executors.newSingleThreadExecutor()
//    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
//    private val database: AppLocalDbRepository = AppLocalDb.database
    private val firebaseModel = FirebaseModel.getInstance()
    private val cloudinaryModel = CloudinaryModel.getInstance()

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

    fun getStudentById(id: String, callback: GetStudentByIdCallback) {
        firebaseModel.getStudentById(id, callback)
    }

    private fun uploadImageToCloudinary(
        image: Bitmap,
        name: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) {
        cloudinaryModel.uploadImageToCloudinary(
            image = image,
            name = name,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}