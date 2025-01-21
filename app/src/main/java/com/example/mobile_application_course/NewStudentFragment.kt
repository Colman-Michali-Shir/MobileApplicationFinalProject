package com.example.mobile_application_course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.navigation.Navigation
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.pickersDialog.showDatePickerDialog
import com.example.mobile_application_course.pickersDialog.showTimePickerDialog
import com.example.mobile_application_course.utils.DateTimeUtils
import java.sql.Time

class NewStudentFragment : Fragment() {

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
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_student, container, false)

        setUp(view)
        cancelButton?.setOnClickListener(::onCancelClicked)
        saveButton?.setOnClickListener(::onSaveClicked)

        return view
    }

    private fun setUp(view: View) {
        saveButton = view.findViewById(R.id.add_student_fragment_save_button)
        cancelButton = view.findViewById(R.id.add_student_fragment_cancel_button)
        nameEditText = view.findViewById(R.id.student_name_edit_text)
        idEditText = view.findViewById(R.id.student_id_edit_text)
        phoneEditText = view.findViewById(R.id.student_phone_edit_text)
        addressEditText = view.findViewById(R.id.student_address_edit_text)
        checkBox = view.findViewById(R.id.student_check_box)
        birthDateEditText = view.findViewById(R.id.student_birth_date_edit_text)
        birthTimeEditText = view.findViewById(R.id.student_birth_time_edit_text)

        birthDateEditText?.let { showDatePickerDialog(it, context) }
        birthTimeEditText?.let { showTimePickerDialog(it, context) }
    }

    private fun onSaveClicked(view: View) {
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

        Navigation.findNavController(view).popBackStack()
    }

    private fun onCancelClicked(view: View) {
        Navigation.findNavController(view).popBackStack()
    }
}