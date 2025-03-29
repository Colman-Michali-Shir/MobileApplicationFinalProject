package com.example.foodie_finder.base

import com.example.foodie_finder.data.entities.Post

typealias GetAllPostsCallback = (List<Post>) -> Unit
typealias CreatePostCallback = (Boolean) -> Unit
typealias UserRefPostCallback = (Post) -> Unit
typealias DeletePostCallback = (Boolean) -> Unit
typealias Callback<T> = (T) -> Unit

object Constants {
    object COLLECTIONS {
        const val USERS = "users"
        const val POSTS = "posts"
        const val SAVED_POSTS = "saved_posts"
    }
}