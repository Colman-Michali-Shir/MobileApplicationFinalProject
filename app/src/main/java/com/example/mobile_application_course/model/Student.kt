package com.example.mobile_application_course.model

import java.sql.Time
import java.util.Date

data class Student(
    var name: String,
    var id: String,
    var phone: String?,
    var address: String?,
    var avatarUrl: String?,
    var isChecked: Boolean = false,
    var birthDate: Date?,
    var birthTime: Time?
)


