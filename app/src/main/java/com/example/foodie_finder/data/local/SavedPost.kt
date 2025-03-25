package com.example.foodie_finder.data.local

import androidx.room.Entity
import com.google.firebase.Timestamp

@Entity(tableName = "saved_posts", primaryKeys = ["userId", "postId"])
data class SavedPost(
    val userId: String,
    val postId: String,
    val savedAt: Long
) {
    companion object {
        private const val USER_ID = "userId"
        private const val POST_ID = "postId"
        private const val SAVED_AT = "savedAt"

        fun fromJSON(json: Map<String, Any>): SavedPost {
            val userId = json[USER_ID] as? String ?: ""
            val postId = json[POST_ID] as? String ?: ""
            val savedAt = json[SAVED_AT] as? Timestamp
            val savedAtLongTimestamp =
                savedAt?.toDate()?.time ?: Timestamp.now().toDate().time


            return SavedPost(
                userId = userId,
                postId = postId,
                savedAt = savedAtLongTimestamp
            )
        }
    }

    val json: Map<String, Any?>
        get() = hashMapOf(
            USER_ID to userId,
            POST_ID to postId,
            SAVED_AT to savedAt
        )
}