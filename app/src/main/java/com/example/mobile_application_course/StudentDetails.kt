package com.example.mobile_application_course

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StudentDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUp()
    }

    private fun setUp() {
        val studentName = intent.getStringExtra("student_name")
        val studentId = intent.getStringExtra("student_id")
        val studentPhone = intent.getStringExtra("student_phone")
        val studentAddress = intent.getStringExtra("student_address")
        val studentIsChecked = intent.getBooleanExtra("student_isChecked", false)


        val studentNameTextView: TextView =
            findViewById(R.id.student_details_activity_name_text_view)
        val studentIdTextView: TextView = findViewById(R.id.student_details_activity_id_text_view)
        val studentPhoneTextView: TextView =
            findViewById(R.id.student_details_activity_phone_text_view)
        val studentAddressTextView: TextView =
            findViewById(R.id.student_details_activity_address_text_view)
        val studentIsCheckedCheckBox: CheckBox =
            findViewById(R.id.student_details_activity_check_box)

        studentNameTextView.text = studentName
        studentIdTextView.text = studentId
        studentPhoneTextView.text = studentPhone
        studentAddressTextView.text = studentAddress
        studentIsCheckedCheckBox.isChecked = studentIsChecked
    }


}