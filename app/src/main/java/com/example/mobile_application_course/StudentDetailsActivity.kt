package com.example.mobile_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.utils.DateTimeUtils

class StudentDetailsActivity : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var student: Student? = null
    private var currentPosition: Int = 0

    private var nameTextView: TextView? = null
    private var idTextView: TextView? = null
    private var phoneTextView: TextView? = null
    private var addressTextView: TextView? = null
    private var birthDateTextView: TextView? = null
    private var birthTimeTextView: TextView? = null
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
        setUpResultLauncher()

        nameTextView = findViewById(R.id.student_details_activity_name_text_view)
        idTextView = findViewById(R.id.student_details_activity_id_text_view)
        phoneTextView = findViewById(R.id.student_details_activity_phone_text_view)
        addressTextView = findViewById(R.id.student_details_activity_address_text_view)
        birthDateTextView = findViewById(R.id.student_details_activity_birth_date_text_view)
        birthTimeTextView = findViewById(R.id.student_details_activity_birth_time_text_view)

        checkBox = findViewById(R.id.student_details_activity_check_box)

        currentPosition = intent.getIntExtra("studentPosition", -1)
        student = Model.shared.getStudentInPosition(currentPosition)

        nameTextView?.text = student?.name
        idTextView?.text = student?.id
        phoneTextView?.text = student?.phone
        addressTextView?.text = student?.address
        checkBox?.isChecked = student?.isChecked ?: false
        birthDateTextView?.text = student?.birthDate?.let {
            DateTimeUtils.formatDate(it)
        }
        birthTimeTextView?.text = student?.birthTime?.let {
            DateTimeUtils.formatTime(it)
        }

        findViewById<Button>(R.id.student_details_activity_edit_button).setOnClickListener {
            val intent = Intent(this, EditStudentActivity::class.java)
            intent.putExtra("studentPosition", currentPosition)
            resultLauncher.launch(intent)
        }
    }

    private fun setUpResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val action = result.data?.getStringExtra("action")
                    when (action) {
                        "edit" -> {
                            val student = Model.shared.getStudentInPosition(currentPosition)
                            student.let {
                                idTextView?.text = it.id
                                nameTextView?.text = it.name
                                phoneTextView?.text = it.phone
                                addressTextView?.text = it.address
                                checkBox?.isChecked = it.isChecked
                                birthDateTextView?.text = it.birthDate?.let { birthDate ->
                                    DateTimeUtils.formatDate(birthDate)
                                }
                                birthTimeTextView?.text = it.birthTime?.let { birthTime ->
                                    DateTimeUtils.formatTime(birthTime)
                                }
                            }

                            val resultIntent = Intent()
                            resultIntent.putExtra("editStudentPosition", currentPosition)
                            resultIntent.putExtra("action", "edit")
                            setResult(RESULT_OK, resultIntent)
                        }

                        "delete" -> {
                            val resultIntent = Intent()
                            resultIntent.putExtra(
                                "deletedStudentPosition",
                                currentPosition
                            )
                            resultIntent.putExtra("action", "delete")
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        }
                    }
                }
            }
    }
}