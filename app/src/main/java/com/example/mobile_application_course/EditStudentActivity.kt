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
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var deleteButton: Button? = null
    private var nameEditText: EditText? = null
    private var idEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var checkBox: CheckBox? = null

    private var studentName = intent.getStringExtra("student_name")
    private val studentId = intent.getStringExtra("student_id")
    private val studentPhone = intent.getStringExtra("student_phone")
    private val studentAddress = intent.getStringExtra("student_address")
    private val studentIsChecked = intent.getBooleanExtra("student_isChecked", false)

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

//        cancelButton?.setOnClickListener {
//            finish()
//        }
//
//        saveButton?.setOnClickListener {
////            val newStudent = Student(
////                name = nameEditText?.text.toString(),
////                id = idEditText?.text.toString(),
////                phone = phoneEditText?.text.toString(),
////                address = addressEditText?.text.toString(),
////                avatarUrl = null,
////                isChecked = checkBox?.isChecked ?: false
////            )
////            Model.shared.addStudent(newStudent)
////
////            val resultIntent = Intent()
////            resultIntent.putExtra("action", "add")
////            setResult(Activity.RESULT_OK, resultIntent)
//
//            finish()
//        }

//        deleteButton?.setOnClickListener {
//            finish()
//        }
    }

    private fun setUp() {
        saveButton = findViewById(R.id.edit_student_activity_save_button)
        cancelButton = findViewById(R.id.edit_student_activity_cancel_button)
        deleteButton = findViewById(R.id.edit_student_activity_delete_button)
        nameEditText = findViewById(R.id.edit_student_activity_name_edit_text)
        idEditText = findViewById(R.id.edit_student_activity_id_edit_text)
        phoneEditText = findViewById(R.id.edit_student_activity_phone_edit_text)
        addressEditText = findViewById(R.id.edit_student_activity_address_edit_text)
        checkBox = findViewById(R.id.edit_student_activity_check_box)

        nameEditText?.setText(studentName)
        idEditText?.setText(studentId)
        phoneEditText?.setText(studentPhone)
        addressEditText?.setText(studentAddress)
        checkBox?.isChecked = studentIsChecked
    }
}