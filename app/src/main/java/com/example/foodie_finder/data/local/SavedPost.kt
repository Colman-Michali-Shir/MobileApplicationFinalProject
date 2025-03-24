package com.example.foodie_finder.data.local

import androidx.room.Entity

@Entity(tableName = "saved_posts", primaryKeys = ["userId", "postId"])
data class SavedPost(
    val userId: String,
    val postId: String,
) {
    companion object {
        private const val USER_ID = "userId"
        private const val POST_ID = "postId"

        fun fromJSON(json: Map<String, Any>): SavedPost {
            val userId = json[USER_ID] as? String ?: ""
            val postId = json[POST_ID] as? String ?: ""


            return SavedPost(
                userId = userId,
                postId = postId
            )
        }
    }

    val json: Map<String, Any?>
        get() = hashMapOf(
            USER_ID to userId,
            POST_ID to postId
        )
}