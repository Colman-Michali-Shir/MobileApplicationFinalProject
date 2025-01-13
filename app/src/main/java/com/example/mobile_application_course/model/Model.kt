package com.example.colman24classandroid.model

class Model private constructor() {

    val students: MutableList<Student> = ArrayList()

    companion object {
        val shared = Model()
    }

    init {
        for (i in 0..20) {
            val student = Student(
                name = "Name $i",
                id = "Student id: $i",
                avatarUrl = "",
                phone = "052",
                address = "Hadera",
                isChecked = false
            )

            students.add(student)
        }
    }
}