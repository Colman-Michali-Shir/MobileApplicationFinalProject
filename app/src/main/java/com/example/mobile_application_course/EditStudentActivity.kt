package com.example.mobile_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student

class EditStudentActivity : AppCompatActivity() {
    private var students: MutableList<Student>? = null
    private var student: Student? = null
    private var studentId: String? = null

    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var deleteButton: Button? = null
    private var nameEditText: EditText? = null
    private var idEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var checkBox: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("TAG", "EditStudentActivity")
        setUp()

        cancelButton?.setOnClickListener {
            finish()
        }

        saveButton?.setOnClickListener {
            val updatedName = nameEditText?.text.toString()
            val updatedId = idEditText?.text.toString()
            val updatedPhone = phoneEditText?.text.toString()
            val updatedAddress = addressEditText?.text.toString()
            val updatedIsChecked = checkBox?.isChecked ?: false

            student?.apply {
                id = updatedId
                name = updatedName
                phone = updatedPhone
                address = updatedAddress
                isChecked = updatedIsChecked
            }

            val resultIntent = Intent()
            resultIntent.putExtra("editStudentId", updatedId)
            resultIntent.putExtra("action", "edit")
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        deleteButton?.setOnClickListener {
            val position = students?.indexOfFirst { it.id == studentId }
            students?.removeIf { it.id == studentId }

            val resultIntent = Intent()
            resultIntent.putExtra("deletedStudentPosition", position)
            resultIntent.putExtra("action", "delete")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun setUp() {
        students = Model.shared.students

        saveButton = findViewById(R.id.edit_student_activity_save_button)
        cancelButton = findViewById(R.id.edit_student_activity_cancel_button)
        deleteButton = findViewById(R.id.edit_student_activity_delete_button)
        nameEditText = findViewById(R.id.edit_student_activity_name_edit_text)
        idEditText = findViewById(R.id.edit_student_activity_id_edit_text)
        phoneEditText = findViewById(R.id.edit_student_activity_phone_edit_text)
        addressEditText = findViewById(R.id.edit_student_activity_address_edit_text)
        checkBox = findViewById(R.id.edit_student_activity_check_box)

        studentId = intent.getStringExtra("student_id")
        student = Model.shared.students.find { it.id == studentId }

        nameEditText?.setText(student?.name)
        idEditText?.setText(student?.id)
        phoneEditText?.setText(student?.phone)
        addressEditText?.setText(student?.address)
        checkBox?.isChecked = student?.isChecked ?: false
    }
}