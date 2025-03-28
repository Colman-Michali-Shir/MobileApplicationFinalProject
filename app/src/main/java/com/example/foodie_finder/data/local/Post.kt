package com.example.foodie_finder.data.local

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodie_finder.base.MyApplication
import com.example.foodie_finder.utils.extensions.toFirebaseTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey var id: String,
    val postedBy: String,
    var username: String,
    var userProfileImg: String? = "",
    val title: String,
    val content: String,
    val rating: Int,
    val imgUrl: String? = "",
    val lastUpdateTime: Long? = null,
    val creationTime: Long,
) {
    companion object {

        private const val LOCAL_LAST_UPDATED = "localPostLastUpdated"

        var lastUpdated: Long
            get() = MyApplication.Globals.context?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong(LOCAL_LAST_UPDATED, 0) ?: 0
            set(value) {
                MyApplication.Globals.context
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.apply {
                        edit().putLong(LOCAL_LAST_UPDATED, value).apply()
                    }
            }

        const val ID_KEY = "id"
        const val USER_ID = "postedBy"
        const val USERNAME = "username"
        const val USER_PROFILE_PICTURE = "userProfilePicture"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val RATING = "rating"
        const val IMAGE_URL = "imgUrl"
        const val LAST_UPDATE_TIME = "lastUpdateTime"
        const val CREATION_TIME = "creationTime"


        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY] as? String ?: ""
            val postedBy = json[USER_ID] as? String ?: ""
            val username = json[USERNAME] as? String ?: ""
            val userProfilePicture = json[USER_PROFILE_PICTURE] as? String ?: ""
            val title = json[TITLE] as? String ?: ""
            val content = json[CONTENT] as? String ?: ""
            val rating = (json[RATING] as? Number)?.toInt() ?: 0
            val imgUrl = json[IMAGE_URL] as? String ?: ""
            val lastUpdateTime = json[LAST_UPDATE_TIME] as? Timestamp
            val creationTime = json[CREATION_TIME] as? Timestamp
            val lastUpdatedLongTimestamp = lastUpdateTime?.toDate()?.time
            val creationTimeLongTimestamp =
                creationTime?.toDate()?.time ?: Timestamp.now().toDate().time

            return Post(
                id = id,
                postedBy = postedBy,
                username = username,
                userProfileImg = userProfilePicture,
                title = title,
                content = content,
                rating = rating,
                imgUrl = imgUrl,
                lastUpdateTime = lastUpdatedLongTimestamp,
                creationTime = creationTimeLongTimestamp,
            )
        }
    }

    val json: Map<String, Any?>
        get() = hashMapOf(
            ID_KEY to id,
            USER_ID to postedBy,
            USERNAME to username,
            USER_PROFILE_PICTURE to userProfileImg,
            TITLE to title,
            CONTENT to content,
            RATING to rating,
            IMAGE_URL to imgUrl,
            LAST_UPDATE_TIME to FieldValue.serverTimestamp(),
            CREATION_TIME to creationTime.toFirebaseTimestamp,
        )
}