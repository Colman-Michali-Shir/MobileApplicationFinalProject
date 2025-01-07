package com.example.colman24classandroid.model

import java.util.UUID


data class Student(
    val name: String,
    val id: String, // Generate a unique ID by default
    val phone: String?,
    val address: String?,
    val avatarUrl: String?,
    var isChecked: Boolean = false
)


