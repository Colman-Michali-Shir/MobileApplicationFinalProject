package com.example.mobile_application_course.model

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
)


