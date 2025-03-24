package com.example.foodie_finder.base

import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.local.Student

typealias GetAllPostsCallback = (List<Post>) -> Unit
typealias GetAllStudentsCallback = (List<Student>) -> Unit
typealias GetStudentByIdCallback = (Student) -> Unit

typealias EmptyCallback = () -> Unit

object Constants {

    object COLLECTIONS {
        const val STUDENTS = "students"
        const val USERS = "users"
        const val POSTS = "posts"
        const val SAVED_POSTS = "saved_posts"

    }
}