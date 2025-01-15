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
    private var nameTextView: TextView? = null
    private var idTextView: TextView? = null
    private var phoneTextView: TextView? = null
    private var addressTextView: TextView? = null
    private var checkBox: CheckBox? = null

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

        nameTextView = findViewById(R.id.student_details_activity_name_text_view)
        idTextView = findViewById(R.id.student_details_activity_id_text_view)
        phoneTextView = findViewById(R.id.student_details_activity_phone_text_view)
        addressTextView = findViewById(R.id.student_details_activity_address_text_view)
        checkBox = findViewById(R.id.student_details_activity_check_box)

        nameTextView?.text = intent.getStringExtra("student_name")
        idTextView?.text = intent.getStringExtra("student_id")
        phoneTextView?.text = intent.getStringExtra("student_phone")
        addressTextView?.text = intent.getStringExtra("student_address")
        checkBox?.isChecked = intent.getBooleanExtra("student_isChecked", false)
    }


}