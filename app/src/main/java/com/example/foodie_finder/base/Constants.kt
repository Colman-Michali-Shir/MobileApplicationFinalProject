package com.example.foodie_finder.base

import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.local.Student

typealias GetAllPostsCallback = (List<Post>) -> Unit
typealias CreatePostCallback = (Boolean) -> Unit
typealias GetStudentByIdCallback = (Student) -> Unit
typealias Callback<T> = (T) -> Unit
typealias EmptyCallback = () -> Unit

object Constants {

    object COLLECTIONS {
        const val STUDENTS = "students"
        const val USERS = "users"
        const val POSTS = "posts"
    }
}