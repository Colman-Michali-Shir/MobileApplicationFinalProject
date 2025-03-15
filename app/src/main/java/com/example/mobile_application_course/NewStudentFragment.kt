package com.example.mobile_application_course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.mobile_application_course.databinding.FragmentNewStudentBinding
import com.example.mobile_application_course.dialogs.alert.showSuccessOperationDialog
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.dialogs.pickers.showDatePickerDialog
import com.example.mobile_application_course.dialogs.pickers.showTimePickerDialog
import com.example.mobile_application_course.utils.DateTimeUtils
import java.sql.Time

class NewStudentFragment : Fragment() {
    private var binding: FragmentNewStudentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewStudentBinding.inflate(inflater, container, false)


        binding?.cancelButton?.setOnClickListener(::onCancelClicked)
        binding?.saveButton?.setOnClickListener(::onSaveClicked)
        binding?.studentInputForm?.birthDateEditText?.let { showDatePickerDialog(it, context) }
        binding?.studentInputForm?.birthTimeEditText?.let { showTimePickerDialog(it, context) }

        return binding?.root
    }

    private fun onSaveClicked(view: View) {
        val newStudent = Student(
            name = binding?.studentInputForm?.nameEditText?.text.toString(),
            id = binding?.studentInputForm?.idEditText?.text.toString(),
            phone = binding?.studentInputForm?.phoneEditText?.text.toString(),
            address = binding?.studentInputForm?.addressEditText?.text.toString(),
            avatarUrl = null,
            isChecked = binding?.studentInputForm?.checkBox?.isChecked ?: false,
            birthDate = binding?.studentInputForm?.birthDateEditText?.text.toString()
                .takeIf { it.isNotBlank() }
                ?.let { DateTimeUtils.parseDate(it) },
            birthTime = binding?.studentInputForm?.birthTimeEditText?.text.toString()
                .takeIf { it.isNotBlank() }?.let {
                    DateTimeUtils.parseTime(it)?.let { birthTime -> Time(birthTime.time) }
                }
        )
        binding?.progressBar?.visibility = View.VISIBLE

        Model.shared.addStudent(newStudent) {
            binding?.progressBar?.visibility = View.GONE
            Navigation.findNavController(view).popBackStack()
            context?.let {
                showSuccessOperationDialog(it, "add")
            }
        }
    }

    private fun onCancelClicked(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}