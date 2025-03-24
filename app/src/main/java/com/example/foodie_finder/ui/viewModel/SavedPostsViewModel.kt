package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.model.PostModel


class SavedPostsViewModel : ViewModel() {

    private val _savedPosts = MutableLiveData<List<Post>>()
    var savedPosts: LiveData<List<Post>> = PostModel.shared.savedPosts

    fun refreshSavedPost() {
        PostModel.shared.getSavedPosts()
    }

    fun savePost(postId: String, callback: (Boolean) -> Unit) {
        PostModel.shared.savePost(postId, callback)
    }

    fun removeSavedPost(postId: String, callback: (Boolean) -> Unit) {
        PostModel.shared.removeSavedPost(postId, callback)
    }
}

