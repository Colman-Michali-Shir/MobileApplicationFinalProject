package com.example.foodie_finder.base

import com.example.foodie_finder.model.Student


typealias GetAllStudentsCallback = (List<Student>) -> Unit
typealias GetStudentByIdCallback = (Student) -> Unit

typealias EmptyCallback = () -> Unit

object Constants {

    object COLLECTIONS {
        const val STUDENTS = "students"
        const val USERS = "users"

    }
}