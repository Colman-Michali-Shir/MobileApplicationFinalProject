package com.example.foodie_finder.interfaces

import com.example.foodie_finder.data.entities.Post

interface OnItemClickListener {
    fun onEditPost(post: Post?)
}
