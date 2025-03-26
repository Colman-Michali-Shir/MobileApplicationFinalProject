package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.model.PostModel


class PostsListViewModel : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    var posts: LiveData<List<Post>> = PostModel.shared.allPosts

    fun refreshAllPosts(){
        PostModel.shared.refreshAllPosts()
    }
}

