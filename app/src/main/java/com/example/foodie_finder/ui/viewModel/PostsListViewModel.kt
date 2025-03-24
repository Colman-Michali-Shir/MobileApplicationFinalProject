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
        PostModel.shared.getAllPosts()
    }


//    init {
//        loadMockPosts() // Load mock data when ViewModel is created
//    }
//
//    private fun loadMockPosts() {
//        val mockData = listOf(
//            Post(
//                id = "1",
//                postedBy = "user1",
//                title = "Amazing Pizza Place 🍕",
//                content = "This place has the best pepperoni pizza! Highly recommend it.",
//                rating = 5,
//                imgUrl = "https://source.unsplash.com/200x200/?pizza",
//                lastUpdateTime = System.currentTimeMillis(),
//                creationTime = System.currentTimeMillis()
//            ),
//            Post(
//                id = "2",
//                postedBy = "user2",
//                title = "Cozy Café ☕",
//                content = "A small café with amazing coffee and a great atmosphere.",
//                rating = 4,
//                imgUrl = "https://source.unsplash.com/200x200/?coffee",
//                lastUpdateTime = System.currentTimeMillis(),
//                creationTime = System.currentTimeMillis()
//            ),
//            Post(
//                id = "3",
//                postedBy = "user3",
//                title = "Best Sushi in Town 🍣",
//                content = "Fresh and delicious sushi. A bit pricey but worth it!",
//                rating = 5,
//                imgUrl = "https://source.unsplash.com/200x200/?sushi",
//                lastUpdateTime = System.currentTimeMillis(),
//                creationTime = System.currentTimeMillis()
//            )
//        )
//
//        _posts.value = mockData
//
//        Log.d("PostsListViewModel", "Mock posts loaded: ${_posts.value}")
//    }
}

