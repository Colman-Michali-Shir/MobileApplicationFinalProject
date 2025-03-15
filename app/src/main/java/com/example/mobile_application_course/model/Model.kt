package com.example.mobile_application_course.model

import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.example.mobile_application_course.model.dao.AppLocalDb
import com.example.mobile_application_course.model.dao.AppLocalDbRepository
import java.util.concurrent.Executors

typealias GetAllStudentsCallback = (List<Student>) -> Unit
typealias GetStudentByIdCallback = (Student) -> Unit

typealias EmptyCallback = () -> Unit


class Model private constructor() {

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val database: AppLocalDbRepository = AppLocalDb.database

    companion object {
        val shared = Model()
    }

    fun deleteStudent(student: Student, callback: EmptyCallback) {
        executor.execute {
            database.studentDao().delete(student)
            Thread.sleep(1000)
            mainHandler.post {
                callback()
            }
        }
    }

    fun addStudent(student: Student, callback: EmptyCallback) {
        executor.execute {
            database.studentDao().addStudent(student)
            Thread.sleep(1000)

            mainHandler.post {
                callback()
            }
        }
    }

    fun updateStudent(student: Student, callback: EmptyCallback) {
        executor.execute {
            database.studentDao().updateStudent(student)
            Thread.sleep(1000)

            mainHandler.post {
                callback()
            }
        }
    }

    fun getStudentById(id: String, callback: GetStudentByIdCallback) {
        executor.execute {
            val student = database.studentDao().getStudentById(id)
            mainHandler.post {
                callback(student)
            }
        }
    }


    fun getAllStudents(callback: GetAllStudentsCallback) {
        executor.execute {
            val students = database.studentDao().getAllStudents()
            Thread.sleep(1000)

            mainHandler.post {
                callback(students)
            }
        }
    }
}