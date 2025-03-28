package com.example.foodie_finder.data.local

import com.example.foodie_finder.data.local.Post.Companion.CONTENT
import com.example.foodie_finder.data.local.Post.Companion.CREATION_TIME
import com.example.foodie_finder.data.local.Post.Companion.ID_KEY
import com.example.foodie_finder.data.local.Post.Companion.IMAGE_URL
import com.example.foodie_finder.data.local.Post.Companion.LAST_UPDATE_TIME
import com.example.foodie_finder.data.local.Post.Companion.RATING
import com.example.foodie_finder.data.local.Post.Companion.TITLE
import com.example.foodie_finder.utils.extensions.toFirebaseTimestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue

data class FirebasePost(
    var id: String,
    val postedBy: DocumentReference,
    val title: String,
    val content: String,
    val rating: Int,
    val imgUrl: String? = "",
    val lastUpdateTime: Long? = null,
    val creationTime: Long
) {
    companion object {

        const val POSTED_BY = "postedBy"

    }

    val json: Map<String, Any?>
        get() = hashMapOf(
            ID_KEY to id,
            POSTED_BY to postedBy,
            TITLE to title,
            CONTENT to content,
            RATING to rating,
            IMAGE_URL to imgUrl,
            LAST_UPDATE_TIME to FieldValue.serverTimestamp(),
            CREATION_TIME to creationTime.toFirebaseTimestamp
        )
}