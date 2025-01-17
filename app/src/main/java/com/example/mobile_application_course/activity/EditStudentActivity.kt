package com.example.mobile_application_course.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_application_course.R
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.pickersDialog.showDatePickerDialog
import com.example.mobile_application_course.pickersDialog.showTimePickerDialog
import com.example.mobile_application_course.utils.DateTimeUtils
import java.sql.Time

class EditStudentActivity : AppCompatActivity() {
    private var students: MutableList<Student>? = null
    private var student: Student? = null
    private var position: Int = 0

    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var deleteButton: Button? = null
    private var nameEditText: EditText? = null
    private var idEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var addressEditText: EditText? = null
    private var birthDateEditText: EditText? = null
    private var birthTimeEditText: EditText? = null

    private var checkBox: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_nav_host)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            val updatedBirthDate = birthDateEditText?.text.toString()
            val updatedBirthTime = birthTimeEditText?.text.toString()

            student?.apply {
                id = updatedId
                name = updatedName
                phone = updatedPhone
                address = updatedAddress
                isChecked = updatedIsChecked
                birthDate = updatedBirthDate.takeIf { it.isNotBlank() }
                    ?.let { DateTimeUtils.parseDate(it) }
                birthTime = updatedBirthTime.takeIf { it.isNotBlank() }?.let {
                    DateTimeUtils.parseTime(it)?.let { birthTime -> Time(birthTime.time) }
                }
            }

            val resultIntent = Intent()
            resultIntent.putExtra("action", "edit")
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        deleteButton?.setOnClickListener {
            students?.removeAt(position)

            val resultIntent = Intent()
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
        nameEditText = findViewById(R.id.student_name_edit_text)
        idEditText = findViewById(R.id.student_id_edit_text)
        phoneEditText = findViewById(R.id.student_phone_edit_text)
        addressEditText = findViewById(R.id.student_address_edit_text)
        checkBox = findViewById(R.id.student_check_box)
        birthDateEditText = findViewById(R.id.student_birth_date_edit_text)
        birthTimeEditText = findViewById(R.id.student_birth_time_edit_text)


        position = intent.getIntExtra("studentPosition", -1)
        student = Model.shared.getStudentInPosition(position)

        student?.let {
            nameEditText?.setText(it.name)
            idEditText?.setText(it.id)
            phoneEditText?.setText(it.phone)
            addressEditText?.setText(it.address)
            checkBox?.isChecked = it.isChecked
            birthDateEditText?.setText(it.birthDate?.let { birthDate ->
                DateTimeUtils.formatDate(
                    birthDate
                )
            })
            birthTimeEditText?.setText(it.birthTime?.let { birthTime ->
                DateTimeUtils.formatTime(
                    birthTime
                )
            })
        }


        birthDateEditText?.let { showDatePickerDialog(it, this) }
        birthTimeEditText?.let { showTimePickerDialog(it, this) }
    }
}