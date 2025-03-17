package com.example.foodie_finder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.foodie_finder.databinding.FragmentNewStudentBinding
import com.example.foodie_finder.dialogs.alert.showSuccessOperationDialog
import com.example.foodie_finder.model.Model
import com.example.foodie_finder.model.Student
import com.example.foodie_finder.dialogs.pickers.showDatePickerDialog
import com.example.foodie_finder.dialogs.pickers.showTimePickerDialog
import com.example.foodie_finder.utils.DateTimeUtils
import com.google.firebase.firestore.FirebaseFirestore
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
            id = FirebaseFirestore.getInstance().collection("students").document().id,
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