package com.example.foodie_finder.model

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.foodie_finder.base.EmptyCallback
import com.example.foodie_finder.base.GetAllStudentsCallback
import com.example.foodie_finder.base.GetStudentByIdCallback
import com.example.foodie_finder.model.dao.AppLocalDb
import com.example.foodie_finder.model.dao.AppLocalDbRepository
import java.util.concurrent.Executors


class Model private constructor() {

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val database: AppLocalDbRepository = AppLocalDb.database
    private val firebaseModel = FirebaseModel()

    companion object {
        val shared = Model()
    }

    fun deleteStudent(student: Student, callback: EmptyCallback) {
        firebaseModel.delete(student, callback)

//        executor.execute {
//            database.studentDao().delete(student)
//            Thread.sleep(1000)
//            mainHandler.post {
//                callback()
//            }
//        }
    }

    fun addStudent(student: Student, callback: EmptyCallback) {
        firebaseModel.add(student, callback)

//        executor.execute {
//            database.studentDao().addStudent(student)
//            Thread.sleep(1000)
//
//            mainHandler.post {
//                callback()
//            }
//        }
    }

    fun updateStudent(student: Student, callback: EmptyCallback) {
        firebaseModel.update(student, callback)

        executor.execute {
            database.studentDao().updateStudent(student)
            Thread.sleep(1000)

            mainHandler.post {
                callback()
            }
        }
    }

    fun getStudentById(id: String, callback: GetStudentByIdCallback) {
        firebaseModel.getStudentById(id, callback)
//        executor.execute {
//            val student = database.studentDao().getStudentById(id)
//            mainHandler.post {
//                callback(student)
//            }
//        }
    }


    fun getAllStudents(callback: GetAllStudentsCallback) {
        firebaseModel.getAllStudents(callback)

//        executor.execute {
//            val students = database.studentDao().getAllStudents()
//            Thread.sleep(1000)
//
//            mainHandler.post {
//                callback(students)
//            }
//        }
    }
}