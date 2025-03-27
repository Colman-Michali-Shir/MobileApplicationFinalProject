package com.example.foodie_finder.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.base.Constants
import com.example.foodie_finder.base.CreatePostCallback
import com.example.foodie_finder.base.DeletePostCallback
import com.example.foodie_finder.base.GetAllPostsCallback
import com.example.foodie_finder.base.UserRefPostCallback
import com.example.foodie_finder.data.local.FirebasePost
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.local.User
import com.example.foodie_finder.data.model.UserModel
import com.example.foodie_finder.utils.extensions.toFirebaseTimestamp
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel private constructor() {

    private val database: FirebaseFirestore by lazy { Firebase.firestore }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _loggedInUser = MutableLiveData<FirebaseUser>()
    val loggedInUser: LiveData<FirebaseUser> get() = _loggedInUser

    init {
        val setting = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }

        auth.addAuthStateListener { auth ->
            _loggedInUser.postValue(auth.currentUser)
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

                    if (userRef != null && UserModel.shared.getConnectedUserRef() == userRef) {
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

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getConnectedUserUid(): String? {
        return auth.currentUser?.uid
    }

    fun getConnectedUserRef(): DocumentReference? {
        val userId = auth.currentUser?.uid ?: return null
        return (database.collection(Constants.COLLECTIONS.USERS).document(userId))
    }

    fun getConnectedUser(callback: (User?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return callback(null)

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

    fun signIn(
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
//                val user = auth.currentUser
                UserModel.shared.loadUser { user ->
                    if (user != null) {
                        callback(true, "Login successful: ${user.email}", null)
                    } else {
                        callback(false, "Failed to load user data", null)
                    }
                }

            }
            .addOnFailureListener {
                handleFirebaseError(callback, it.message)
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        callback: (Boolean, String?, List<String>?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                val user = User(
                    id = userId,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    avatarUrl = null,
                )
                database.collection(Constants.COLLECTIONS.USERS).document(userId)
                    .set(user.json)
                    .addOnSuccessListener {
                        callback(true, "User registered successfully", null)
                    }
                    .addOnFailureListener { e ->
                        callback(false, "Failed to save user data: ${e.message}", null)
                    }
            }
            .addOnFailureListener {
                handleFirebaseError(callback, it.message)
            }
    }


    private fun handleFirebaseError(
        callback: (Boolean, String?, List<String>?) -> Unit,
        errorMessage: String?
    ) {
        when {
            errorMessage?.contains(
                "email address is already in use",
                ignoreCase = true
            ) == true -> {
                callback(false, "Email is already registered", listOf("email"))
            }

            errorMessage?.contains(
                "The email address is badly formatted",
                ignoreCase = true
            ) == true -> {
                callback(false, "Invalid email format", listOf("email"))
            }

            errorMessage?.contains(
                "Password should be at least 6 characters",
                ignoreCase = true
            ) == true -> {
                callback(false, "Password must be at least 6 characters", listOf("password"))
            }

            errorMessage?.contains(
                "The supplied auth credential is incorrect",
                ignoreCase = true
            ) == true -> {
                callback(false, "Email or password is incorrect", listOf("email", "password"))
            }

            else -> {
                callback(false, errorMessage ?: "Unknown error", null)
            }
        }
    }
}
