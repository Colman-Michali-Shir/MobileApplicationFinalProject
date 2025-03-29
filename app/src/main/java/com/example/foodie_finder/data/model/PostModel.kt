package com.example.foodie_finder.data.model

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.base.Callback
import com.example.foodie_finder.data.local.AppLocalDb
import com.example.foodie_finder.data.local.AppLocalDbRepository
import com.example.foodie_finder.data.local.FirebasePost
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.remote.CloudinaryModel
import com.example.foodie_finder.data.remote.FirebaseModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executors


class PostModel private constructor() {

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database: AppLocalDbRepository = AppLocalDb.database
    private val firebaseModel = FirebaseModel.getInstance()
    private val cloudinaryModel = CloudinaryModel.getInstance()

    private var executor = Executors.newSingleThreadExecutor()

    val savedPosts: MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()
    val allPosts: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()
    val usersPosts: MutableLiveData<List<Post>> = MutableLiveData<List<Post>>()

    init {
        UserModel.shared.loggedInUser.observeForever { user ->
            if (user == null) {
                allPosts.postValue(emptyList())
                usersPosts.postValue(emptyList())
                savedPosts.postValue(emptyList())
            } else {
                database.postDao().getAllPosts().observeForever { allPosts.postValue(it) }
                database.postDao().getPostsByUser(user.id)
                    .observeForever { usersPosts.postValue(it) }
                database.savedPostDao().getSavedPostsByUser(user.id)
                    .observeForever { posts -> savedPosts.postValue(posts) }
            }
        }
    }

    companion object {
        val shared = PostModel()
    }

    fun savePost(postId: String, callback: (Boolean) -> Unit) {
        firebaseModel.savePost(postId) { success, savedPost ->
            if (success && savedPost != null) {
                executor.execute {
                    database.savedPostDao().savePost(savedPost)
                }
            }
            callback(success)
        }
    }

    fun removeSavedPost(postId: String, callback: (Boolean) -> Unit) {
        firebaseModel.removeSavedPost(postId) { success ->
            if (success) {
                val userId =
                    FirebaseAuth.getInstance().currentUser?.uid ?: return@removeSavedPost callback(
                        false
                    )
                executor.execute {
                    database.savedPostDao()
                        .removeSavedPost(userId, postId)
                }
            }
            callback(success)
        }
    }

    fun getSavedPosts() {
        firebaseModel.getSavedPosts { posts ->
            savedPosts.postValue(posts)
        }
    }

    fun refreshAllPosts() {
        loadingState.postValue(LoadingState.LOADING)
        val lastUpdated: Long = Post.lastUpdated
        firebaseModel.getAllPosts(lastUpdated) { posts ->
            executor.execute {
                var currentTime = lastUpdated
                for (post in posts) {
                    if (post.deleted) {
                        database.postDao().deletePost(post.id)
                    } else {
                        database.postDao().createPost(post)
                        post.lastUpdateTime?.let {
                            if (currentTime < it) {
                                currentTime = it
                            }
                        }
                    }
                }

                Post.lastUpdated = currentTime
                loadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun refreshUsersPosts() {
        loadingState.postValue(LoadingState.LOADING)
        val lastUpdated: Long = Post.lastUpdated
        firebaseModel.getPostsByUser(lastUpdated) { posts ->
            executor.execute {
                var currentTime = lastUpdated
                for (post in posts) {
                    if (post.deleted) {
                        database.postDao().deletePost(post.id)
                    } else {
                        database.postDao().createPost(post)
                        post.lastUpdateTime?.let {
                            if (currentTime < it) {
                                currentTime = it
                            }
                        }
                    }
                }

                Post.lastUpdated = currentTime
                loadingState.postValue(LoadingState.LOADED)
            }
        }
    }

    fun createPost(post: FirebasePost, image: Bitmap?, callback: Callback<Pair<Boolean, String?>>) {

        firebaseModel.createPost(post) { isSuccessful ->
            if (isSuccessful) {
                image?.let {
                    cloudinaryModel.uploadImageToCloudinary(
                        image = it,
                        name = post.id,
                        onSuccess = { url ->
                            val newPost = post.copy(imgUrl = url)
                            firebaseModel.createPost(newPost) { isSuccessful ->
                                callback(
                                    Pair(
                                        isSuccessful,
                                        if (isSuccessful) null else "Can't create post, please try again"
                                    )
                                )
                            }
                        },
                        onError = { callback(Pair(true, null)) }
                    )
                } ?: callback(Pair(true, null))

            } else {
                callback(Pair(false, "Can't create post, please try again"))
            }
        }
    }

    fun updatePost(post: Post, image: Bitmap?, callback: Callback<Pair<Boolean, String?>>) {
        val userRef = UserModel.shared.getConnectedUserRef() ?: return
        val editedPost = FirebasePost(
            id = post.id,
            postedBy = userRef,
            title = post.title,
            content = post.content,
            rating = post.rating,
            imgUrl = post.imgUrl,
            lastUpdateTime = Timestamp.now().toDate().time,
            creationTime = post.creationTime
        )

        createPost(editedPost, image) { (isSuccessful, errorMessage) ->
            callback(
                Pair(
                    isSuccessful,
                    if (errorMessage == null) null else "Can't update post, please try again"
                )
            )
        }
    }

    fun deletePost(postId: String, callback: Callback<Pair<Boolean, String?>>) {
        firebaseModel.deletePost(postId) { isSuccessful ->
            if (isSuccessful) {
                executor.execute {
                    database.postDao().deletePost(postId)
                }
            }
            callback(
                Pair(
                    isSuccessful,
                    if (isSuccessful) null else "Can't delete post, please try again"
                )
            )
        }
    }

}