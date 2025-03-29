package com.example.foodie_finder.fragments.addPost

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.base.Callback
import com.example.foodie_finder.data.entities.FirebasePost
import com.example.foodie_finder.data.model.PostModel

class AddPostViewModel : ViewModel() {

    fun createPost(
        post: FirebasePost,
        image: Bitmap?,
        callback: Callback<Pair<Boolean, String?>>
    ) {
        PostModel.shared.createPost(post, image, callback)
    }

}