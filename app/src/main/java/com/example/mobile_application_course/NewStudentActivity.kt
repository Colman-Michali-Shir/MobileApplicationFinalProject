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
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.pickersDialog.showDatePickerDialog
import com.example.mobile_application_course.pickersDialog.showTimePickerDialog
import com.example.mobile_application_course.utils.DateTimeUtils
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Locale


class NewStudentActivity : AppCompatActivity() {
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var nameEditText: EditText? = null
    private var idEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var checkBox: CheckBox? = null
    private var birthDateEditText: EditText? = null
    private var birthTimeEditText: EditText? = null


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
    }

    private fun setUp() {
        saveButton = findViewById(R.id.add_student_activity_save_button)
        cancelButton = findViewById(R.id.add_student_activity_cancel_button)
        nameEditText = findViewById(R.id.student_name_edit_text)
        idEditText = findViewById(R.id.student_id_edit_text)
        phoneEditText = findViewById(R.id.student_phone_edit_text)
        addressEditText = findViewById(R.id.student_address_edit_text)
        checkBox = findViewById(R.id.student_check_box)
        birthDateEditText = findViewById(R.id.student_birth_date_edit_text)
        birthTimeEditText = findViewById(R.id.student_birth_time_edit_text)


        cancelButton?.setOnClickListener {
            finish()
        }

        saveButton?.setOnClickListener {
            val newStudent = Student(
                name = nameEditText?.text.toString(),
                id = idEditText?.text.toString(),
                phone = phoneEditText?.text.toString(),
                address = addressEditText?.text.toString(),
                avatarUrl = null,
                isChecked = checkBox?.isChecked ?: false,
                birthDate = birthDateEditText?.text.toString().takeIf { it.isNotBlank() }
                    ?.let { DateTimeUtils.parseDate(it) },
                birthTime = birthTimeEditText?.text.toString().takeIf { it.isNotBlank() }?.let {
                    DateTimeUtils.parseTime(it)?.let { birthTime -> Time(birthTime.time) }
                }
            )

            Model.shared.addStudent(newStudent)

            val resultIntent = Intent()
            resultIntent.putExtra("action", "add")
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }

        birthDateEditText?.let { showDatePickerDialog(it, this) }
        birthTimeEditText?.let { showTimePickerDialog(it, this) }
    }
}