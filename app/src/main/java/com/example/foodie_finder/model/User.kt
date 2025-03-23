package com.example.foodie_finder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var id: String,
    var email: String,
    var firstName: String?,
    var lastName: String?,
    var avatarUrl: String?,

    ) {

    companion object {
        private const val ID_KEY = "id"
        private const val EMAIL_KEY = "email"
        private const val FIRST_NAME_KEY = "firstName"
        private const val LAST_NAME_KEY = "lastName"
        private const val AVATAR_URL_KEY = "avatarUrl"

        fun fromJSON(json: Map<String, Any>): User {
            return User(
                id = json[ID_KEY] as? String
                    ?: throw IllegalArgumentException("ID is required"),
                email = json[EMAIL_KEY] as? String
                    ?: throw IllegalArgumentException("Email is required"),
                firstName = json[FIRST_NAME_KEY] as? String,
                lastName = json[LAST_NAME_KEY] as? String,
                avatarUrl = json[AVATAR_URL_KEY] as? String
            )
        }
    }

    val json: Map<String, Any?>
        get() {
            return hashMapOf(
                EMAIL_KEY to email,
                ID_KEY to id,
                FIRST_NAME_KEY to firstName,
                LAST_NAME_KEY to lastName,
                AVATAR_URL_KEY to avatarUrl
            )
        }
}


