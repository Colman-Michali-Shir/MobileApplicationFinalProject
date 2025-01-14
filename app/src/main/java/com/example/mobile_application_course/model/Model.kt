package com.example.mobile_application_course.model

class Model private constructor() {

    val students: MutableList<Student> = ArrayList()

    companion object {
        val shared = Model()
    }

    fun addStudent(student: Student) {
        students.add(student)
    }
}