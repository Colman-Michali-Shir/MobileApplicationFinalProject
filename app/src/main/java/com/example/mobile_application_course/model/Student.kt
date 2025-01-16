package com.example.mobile_application_course.model

data class Student(
    var name: String,
    var id: String,
    var phone: String?,
    var address: String?,
    var avatarUrl: String?,
    var isChecked: Boolean = false
)


