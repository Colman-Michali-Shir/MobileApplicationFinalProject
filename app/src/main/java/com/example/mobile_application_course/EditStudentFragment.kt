package com.example.mobile_application_course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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


class EditStudentFragment : Fragment() {
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

        position = arguments?.let {
            EditStudentFragmentArgs.fromBundle(it).position
        } ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_edit_student, container, false)

        setUp(view)

        cancelButton?.setOnClickListener(::onCancelClicked)
        saveButton?.setOnClickListener(::onSaveClicked)
        deleteButton?.setOnClickListener(::onDeleteClicked)

        return view
    }

    private fun setUp(view: View) {
        students = Model.shared.students

        saveButton = view.findViewById(R.id.edit_student_fragment_save_button)
        cancelButton = view.findViewById(R.id.edit_student_fragment_cancel_button)
        deleteButton = view.findViewById(R.id.edit_student_fragment_delete_button)
        nameEditText = view.findViewById(R.id.student_name_edit_text)
        idEditText = view.findViewById(R.id.student_id_edit_text)
        phoneEditText = view.findViewById(R.id.student_phone_edit_text)
        addressEditText = view.findViewById(R.id.student_address_edit_text)
        checkBox = view.findViewById(R.id.student_check_box)
        birthDateEditText = view.findViewById(R.id.student_birth_date_edit_text)
        birthTimeEditText = view.findViewById(R.id.student_birth_time_edit_text)

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

        birthDateEditText?.let { showDatePickerDialog(it, context) }
        birthTimeEditText?.let { showTimePickerDialog(it, context) }
    }

    private fun onSaveClicked(view: View) {
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

        Navigation.findNavController(view).popBackStack()
    }

    private fun onCancelClicked(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    private fun onDeleteClicked(view: View) {
        students?.removeAt(position)

        Navigation.findNavController(view).popBackStack(
            R.id.studentsListFragment,
            false
        )
    }

}