package com.example.foodie_finder.fragments.editPost

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.base.Callback
import com.example.foodie_finder.data.entities.Post
import com.example.foodie_finder.data.model.PostModel

class EditPostViewModel : ViewModel() {

    fun deletePost(postId: String, callback: Callback<Pair<Boolean, String?>>) {
        PostModel.shared.deletePost(postId, callback)
    }

    fun updatePost(
        post: Post,
        image: Bitmap?,
        callback: Callback<Pair<Boolean, String?>>
    ) {
        PostModel.shared.updatePost(post, image, callback)
    }

}