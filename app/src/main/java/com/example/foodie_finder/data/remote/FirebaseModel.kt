package com.example.foodie_finder.data.remote

import com.example.foodie_finder.auth.AuthManager
import com.example.foodie_finder.base.Constants
import com.example.foodie_finder.base.CreatePostCallback
import com.example.foodie_finder.base.DeletePostCallback
import com.example.foodie_finder.base.GetAllPostsCallback
import com.example.foodie_finder.base.UserRefPostCallback
import com.example.foodie_finder.data.local.FirebasePost
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.local.SavedPost
import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.utils.extensions.toFirebaseTimestamp
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel private constructor() {

    private val database: FirebaseFirestore by lazy { Firebase.firestore }

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        database.firestoreSettings = setting
    }

    companion object {
        @Volatile
        private var instance: FirebaseModel? = null

        fun getInstance(): FirebaseModel {
            return instance ?: synchronized(this) {
                instance ?: FirebaseModel().also { instance = it }
            }
        }
    }

    fun getAllPosts(sinceLastUpdated: Long, callback: GetAllPostsCallback) {
        database.collection(Constants.COLLECTIONS.POSTS)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATE_TIME, sinceLastUpdated.toFirebaseTimestamp)
            .get()
            .addOnSuccessListener { postsJson ->
                val postsList: MutableList<Post> = mutableListOf()
                val firebaseCallsTasks = mutableListOf<Task<DocumentSnapshot>>()

                for (postDoc in postsJson) {
                    val post = Post.fromJSON(postDoc.data)
                    val userRef = postDoc.getDocumentReference("postedBy")
                    if (userRef != null) {
                        val firebaseUserFetch = TaskCompletionSource<DocumentSnapshot>()
                        addUserInfoToPost(userRef, post) { fullPost ->
                            postsList.add(fullPost)
                            firebaseUserFetch.setResult(null) // Mark task as completed
                        }
                        firebaseCallsTasks.add(firebaseUserFetch.task)
                    }
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(firebaseCallsTasks).addOnSuccessListener {
                    callback(postsList) // Return the full list after all user data is fetched
                }.addOnFailureListener {
                    callback(emptyList()) // Handle failure case
                }
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun getPostsByUser(sinceLastUpdated: Long, callback: GetAllPostsCallback) {
        database.collection(Constants.COLLECTIONS.POSTS)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATE_TIME, sinceLastUpdated.toFirebaseTimestamp)
            .get()
            .addOnSuccessListener { postsJson ->
                val postsList: MutableList<Post> = mutableListOf()
                val firebaseCallsTasks = mutableListOf<Task<DocumentSnapshot>>()

                for (postDoc in postsJson) {
                    val post = Post.fromJSON(postDoc.data)
                    val userRef = postDoc.getDocumentReference("postedBy")

                    if (userRef != null && getConnectedUserRef() == userRef) {
                        val firebaseUserFetch = TaskCompletionSource<DocumentSnapshot>()
                        addUserInfoToPost(userRef, post) { fullPost ->
                            postsList.add(fullPost)
                            firebaseUserFetch.setResult(null) // Mark task as completed
                        }
                        firebaseCallsTasks.add(firebaseUserFetch.task)
                    }
                }

                // Wait until all user data fetches are completed
                Tasks.whenAllSuccess<DocumentSnapshot>(firebaseCallsTasks).addOnSuccessListener {
                    callback(postsList)
                }.addOnFailureListener {
                    callback(emptyList()) // Handle failure case
                }
            }.addOnFailureListener { callback(listOf()) }
    }

    private fun addUserInfoToPost(
        userRef: DocumentReference,
        post: Post,
        callback: UserRefPostCallback
    ) {
        userRef.get().addOnSuccessListener { userDoc ->
            if (userDoc.exists()) {
                val userId = userDoc.getString("id") ?: ""
                val fullName = userDoc.getString("email") ?: ""
                val profilePic = userDoc.getString("avatarUrl") ?: ""
                post.username = fullName
                post.userProfileImg = profilePic
                post.postedBy = userId

                callback(post)
            }
        }
    }

    fun createPost(post: FirebasePost, callback: CreatePostCallback) {
        database.collection(Constants.COLLECTIONS.POSTS)
            .document(post.id)
            .set(post.json)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun deletePost(postId: String, callback: DeletePostCallback) {
        database.collection(Constants.COLLECTIONS.POSTS)
            .document(postId)
            .delete()
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        database.collection(Constants.COLLECTIONS.USERS).document(user.id)
            .update(user.json)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }


    fun getConnectedUserRef(): DocumentReference? {
        val userId = AuthManager.shared.getCurrentUserUid() ?: return null
        return (database.collection(Constants.COLLECTIONS.USERS).document(userId))
    }

    fun getConnectedUser(callback: (User?) -> Unit) {
        val userId = AuthManager.shared.getCurrentUserUid() ?: return callback(null)

        database.collection(Constants.COLLECTIONS.USERS).document(userId)
            .get()
            .addOnSuccessListener { document ->
                callback(
                    if (document.exists()) User.fromJSON(
                        document.data ?: emptyMap()
                    ) else null
                )
            }
            .addOnFailureListener { callback(null) }
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        database.collection(Constants.COLLECTIONS.USERS).document(userId)
            .get()
            .addOnSuccessListener { document ->
                callback(
                    if (document.exists()) User.fromJSON(
                        document.data ?: emptyMap()
                    ) else null
                )
            }
            .addOnFailureListener { callback(null) }
    }


    fun createUser(user: User, callback: (Boolean) -> Unit) {
        database.collection(Constants.COLLECTIONS.USERS).document(user.id)
            .set(user.json)
            .addOnCompleteListener { callback(it.isSuccessful) }
    }

    fun getSavedPosts(callback: (List<String>) -> Unit) {
        val userId = AuthManager.shared.getCurrentUserUid() ?: return callback(emptyList())

        database.collection(Constants.COLLECTIONS.SAVED_POSTS)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val savedPostIds = querySnapshot.documents.mapNotNull { doc ->
                    doc.getString("postId")
                }

                callback(savedPostIds)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun savePost(postId: String, callback: (Boolean, SavedPost?) -> Unit) {
        val userId = AuthManager.shared.getCurrentUserUid() ?: return callback(false, null)

        val savedPostRef = database.collection(Constants.COLLECTIONS.SAVED_POSTS)
            .document("$userId-$postId")

        val savedPost = SavedPost(
            userId = userId,
            postId = postId,
            savedAt = Timestamp.now().toDate().time
        )

        savedPostRef.set(savedPost.json)
            .addOnSuccessListener { callback(true, savedPost) }
            .addOnFailureListener { callback(false, null) }
    }

    fun removeSavedPost(postId: String, callback: (Boolean) -> Unit) {
        val userId = AuthManager.shared.getCurrentUserUid() ?: return callback(false)

        database.collection(Constants.COLLECTIONS.SAVED_POSTS)
            .document("$userId-$postId")
            .delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

}
