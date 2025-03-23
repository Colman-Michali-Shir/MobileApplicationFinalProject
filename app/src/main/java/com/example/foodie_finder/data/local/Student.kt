package com.example.foodie_finder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity
data class Student(
    var name: String,
    @PrimaryKey var id: String,
    var phone: String?,
    var address: String?,
    var avatarUrl: String?,
    var isChecked: Boolean = false,
    var birthDate: Date?,
    var birthTime: Time?
) {
    companion object {
        private const val ID_KEY = "id"
        private const val NAME_KEY = "name"
        private const val PHONE_KEY = "phone"
        private const val ADDRESS_KEY = "address"
        private const val BIRTH_DATE_KEY = "birthDate"
        private const val BIRTH_TIME_KEY = "birthTime"
        private const val AVATAR_URL_KEY = "avatarUrl"
        private const val IS_CHECKED_KEY = "isChecked"

        fun fromJSON(json: Map<String, Any>): Student {
            return Student(
                name = json[NAME_KEY] as? String ?: "",
                id = json[ID_KEY] as? String ?: throw IllegalArgumentException("ID is required"),
                phone = json[PHONE_KEY] as? String,
                address = json[ADDRESS_KEY] as? String,
                avatarUrl = json[AVATAR_URL_KEY] as? String,
                isChecked = json[IS_CHECKED_KEY] as? Boolean ?: false,
                birthDate = (json[BIRTH_DATE_KEY] as? Long)?.let { Date(it) },
                birthTime = (json[BIRTH_TIME_KEY] as? String)?.let { timeString ->
                    runCatching { Time.valueOf(timeString) }.getOrNull()
                }
            )
        }
    }

    val json: Map<String, Any?>
        get() {
            return hashMapOf(
                NAME_KEY to name,
                ID_KEY to id,
                PHONE_KEY to phone,
                ADDRESS_KEY to address,
                AVATAR_URL_KEY to avatarUrl,
                IS_CHECKED_KEY to isChecked,
                BIRTH_DATE_KEY to birthDate?.time,
                BIRTH_TIME_KEY to birthTime
            )
        }
}


