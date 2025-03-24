package com.example.foodie_finder.data.local

import android.content.Context
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodie_finder.base.MyApplication
import com.example.foodie_finder.utils.extensions.toFirebaseTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity(tableName = "posts",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["postedBy"])])
data class Post(
    @PrimaryKey var id: String,
    val postedBy: String,
    val title: String,
    val content: String,
    val rating: Int,
    val imgUrl: String? = "",
    val lastUpdateTime: Long? = null,
    val creationTime: Long
){
    companion object {

        private const val LOCAL_LAST_UPDATED = "localStudentLastUpdated"

        var lastUpdated: Long
            get() = MyApplication.Globals.context?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong(LOCAL_LAST_UPDATED, 0) ?: 0

            set(value) {
                MyApplication.Globals.context
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.apply {
                        edit().putLong(LOCAL_LAST_UPDATED, value).apply()
                    }
            }

        private const val ID_KEY = "id"
        private const val USER_ID = "postedBy"
        private const val TITLE = "title"
        private const val CONTENT = "content"
        private const val RATING = "rating"
        private const val IMAGE_URL = "imgUrl"
        private const val LAST_UPDATE_TIME = "lastUpdateTime"
        private const val CREATION_TIME = "creationTime"

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY] as? String ?: ""
            val postedBy = json[USER_ID] as? String ?: ""
            val title = json[TITLE] as? String ?: ""
            val content = json[CONTENT] as? String ?: ""
            val rating = json[RATING] as? Int ?: 0
            val imgUrl = json[IMAGE_URL] as? String ?: ""
            val lastUpdateTime = json[LAST_UPDATE_TIME] as? Timestamp
            val creationTime = json[CREATION_TIME] as? Timestamp
            val lastUpdatedLongTimestamp = lastUpdateTime?.toDate()?.time
            val creationTimeLongTimestamp = creationTime?.toDate()?.time ?: Timestamp.now().toDate().time

            return Post(
                id = id,
                postedBy =postedBy,
                title = title,
                content = content,
                rating = rating,
                imgUrl = imgUrl,
                lastUpdateTime = lastUpdatedLongTimestamp,
                creationTime = creationTimeLongTimestamp
            )
        }
    }

    val json: Map<String, Any?>
        get() = hashMapOf(
            ID_KEY to id,
            USER_ID to postedBy,
            TITLE to title,
            CONTENT to content,
            RATING to rating,
            IMAGE_URL to imgUrl,
            LAST_UPDATE_TIME to FieldValue.serverTimestamp(),
            CREATION_TIME to creationTime.toFirebaseTimestamp
        )
}