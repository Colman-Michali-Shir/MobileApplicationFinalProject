package com.example.foodie_finder.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.data.local.AppLocalDb
import com.example.foodie_finder.data.local.AppLocalDbRepository
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel
import java.util.concurrent.Executors

class PostModel private constructor(){

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val firebaseModel = FirebaseModel.getInstance()
    private val cloudinaryModel = CloudinaryModel.getInstance()

    private var executor = Executors.newSingleThreadExecutor()

    val allPosts: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    init {
        database.postDao().getAllPosts().observeForever{allPosts.postValue(it)}
    }

    companion object {
        val shared = PostModel()
    }


    fun getAllPosts(){
        val lastUpdated: Long = Post.lastUpdated
        firebaseModel.getAllPosts(lastUpdated) {posts ->
            executor.execute {
                var currentTime = lastUpdated

                for (post in posts) {
                    database.postDao().createPost(post)
                    post.lastUpdateTime?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }

                Post.lastUpdated = currentTime
            }
        }
    }

//    fun refreshAllPosts(){
//        loadingState.postValue(LoadingState.LOADING)
//    }

}