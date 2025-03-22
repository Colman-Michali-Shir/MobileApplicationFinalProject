package com.example.foodie_finder.model

import android.graphics.Bitmap
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.example.foodie_finder.base.EmptyCallback
import com.example.foodie_finder.base.GetAllStudentsCallback
import com.example.foodie_finder.base.GetStudentByIdCallback
import com.example.foodie_finder.model.dao.AppLocalDb
import com.example.foodie_finder.model.dao.AppLocalDbRepository
import java.util.concurrent.Executors


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

    fun getStudentById(id: String, callback: GetStudentByIdCallback) {
        firebaseModel.getStudentById(id, callback)

    }


    fun getAllStudents(callback: GetAllStudentsCallback) {
        firebaseModel.getAllStudents(callback)
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
}