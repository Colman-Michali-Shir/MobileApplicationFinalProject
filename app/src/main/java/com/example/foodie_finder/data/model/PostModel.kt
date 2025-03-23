package com.example.foodie_finder.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.data.local.AppLocalDb
import com.example.foodie_finder.data.local.AppLocalDbRepository
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel

class PostModel private constructor(){

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val firebaseModel = FirebaseModel()
    private val cloudinaryModel = CloudinaryModel()

    val allPosts: LiveData<List<Post>> = database.PostDao().getAllPosts()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    companion object {
        val shared = PostModel()
    }

}