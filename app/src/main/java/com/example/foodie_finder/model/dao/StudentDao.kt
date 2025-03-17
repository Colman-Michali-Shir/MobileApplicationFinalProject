package com.example.foodie_finder.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodie_finder.model.Student

@Dao
interface StudentDao {

    @Query("SELECT * FROM Student")
    fun getAllStudents(): List<Student>

    @Query("SELECT * FROM Student WHERE id =:id")
    fun getStudentById(id: String): Student

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStudent(vararg students: Student)

    @Update
    fun updateStudent(student: Student)

    @Delete
    fun delete(student: Student)
}