package com.example.mobile_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class NewStudentActivity : AppCompatActivity() {
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var nameEditText: EditText? = null
    private var idEditText: EditText? = null
    private var checkBox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUp()

        cancelButton?.setOnClickListener {
            finish()
        }

        saveButton?.setOnClickListener {
            val newStudent = Student(
                name = nameEditText?.text.toString(),
                id = idEditText?.text.toString(),
                phone = null,
                address = null,
                avatarUrl = null,
                isChecked = checkBox?.isChecked ?: false
            )
            Model.shared.addStudent(newStudent)

            val resultIntent = Intent()
            resultIntent.putExtra("action", "add")
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }
    }

    private fun setUp() {
        saveButton = findViewById(R.id.add_student_activity_save_button)
        cancelButton = findViewById(R.id.add_student_activity_cancel_button)
        nameEditText = findViewById(R.id.add_student_activity_name_edit_text)
        idEditText = findViewById(R.id.add_student_activity_id_edit_text)
        checkBox = findViewById(R.id.add_student_activity_check_box)

    }
}