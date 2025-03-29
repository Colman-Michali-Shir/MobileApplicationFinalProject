package com.example.foodie_finder.data.entities

import androidx.room.Entity
import com.example.foodie_finder.utils.extensions.toFirebaseTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity(tableName = "saved_posts", primaryKeys = ["userId", "postId"])
data class SavedPost(
    val userId: String,
    val postId: String,
    val lastUpdateTime: Long? = null,
    val savedAt: Long
) {
    companion object {
        private const val USER_ID = "userId"
        private const val POST_ID = "postId"
        const val LAST_UPDATE_TIME = "lastUpdateTime"
        private const val SAVED_AT = "savedAt"

        fun fromJSON(json: Map<String, Any>): SavedPost {
            val userId = json[USER_ID] as? String ?: ""
            val postId = json[POST_ID] as? String ?: ""
            val lastUpdateTime = json[Post.LAST_UPDATE_TIME] as? Timestamp
            val lastUpdatedLongTimestamp = lastUpdateTime?.toDate()?.time
            val savedAt = json[SAVED_AT] as? Timestamp
            val savedAtLongTimestamp =
                savedAt?.toDate()?.time ?: Timestamp.now().toDate().time


            return SavedPost(
                userId = userId,
                postId = postId,
                lastUpdateTime = lastUpdatedLongTimestamp,
                savedAt = savedAtLongTimestamp
            )
        }
    }

    val json: Map<String, Any?>
        get() = hashMapOf(
            USER_ID to userId,
            POST_ID to postId,
            LAST_UPDATE_TIME to FieldValue.serverTimestamp(),
            SAVED_AT to savedAt.toFirebaseTimestamp,
        )
}