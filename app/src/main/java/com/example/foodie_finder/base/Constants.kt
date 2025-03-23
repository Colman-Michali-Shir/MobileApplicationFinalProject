package com.example.foodie_finder.base

import com.example.foodie_finder.data.local.Student


typealias GetAllStudentsCallback = (List<Student>) -> Unit
typealias GetStudentByIdCallback = (Student) -> Unit

typealias EmptyCallback = () -> Unit

object Constants {

    object COLLECTIONS {
        const val STUDENTS = "students"
    }
}