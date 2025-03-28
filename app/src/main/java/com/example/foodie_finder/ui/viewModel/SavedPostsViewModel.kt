package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.model.PostModel


class SavedPostsViewModel : ViewModel() {

    var posts: LiveData<List<Post>> = PostModel.shared.allPosts
    var savedPosts: LiveData<List<String>> = PostModel.shared.savedPosts

    fun refreshSavedPosts() {
        PostModel.shared.getSavedPosts()
    }

    fun refreshAllPosts() {
        PostModel.shared.refreshAllPosts()
    }

    fun savePost(postId: String, callback: (Boolean) -> Unit) {
        PostModel.shared.savePost(postId, callback)
    }

    fun removeSavedPost(postId: String, callback: (Boolean) -> Unit) {
        PostModel.shared.removeSavedPost(postId, callback)
    }
}

