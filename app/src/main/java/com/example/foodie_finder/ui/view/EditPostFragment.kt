package com.example.foodie_finder.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.foodie_finder.R
import com.example.foodie_finder.data.local.Student
import com.example.foodie_finder.data.model.Model
import com.example.foodie_finder.databinding.FragmentEditPostBinding

class EditPostFragment : Fragment() {
    private var student: Student? = null
    private var id: String = ""

    private var binding: FragmentEditPostBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = arguments?.let {
            EditPostFragmentArgs.fromBundle(it).id
        } ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)

//        setUp()
        binding?.cancelButton?.setOnClickListener(::onCancelClicked)
//        binding?.saveButton?.setOnClickListener(::onSaveClicked)
        binding?.deleteButton?.setOnClickListener(::onDeleteClicked)

        return binding?.root
    }

//    private fun setUp() {
//        Model.shared.getStudentById(id) {
//            student = it
//            student?.let { student ->
//                binding?.studentInputForm?.apply {
//                    nameEditText.setText(student.name)
//                    idEditText.setText(student.id)
//                    phoneEditText.setText(student.phone ?: "")
//                    addressEditText.setText(student.address ?: "")
//                    checkBox.isChecked = student.isChecked
//
//                    birthDateEditText.setText(
//                        student.birthDate?.let(DateTimeUtils::formatDate) ?: ""
//                    )
//                    birthTimeEditText.setText(
//                        student.birthTime?.let(DateTimeUtils::formatTime) ?: ""
//                    )
//
//                    showDatePickerDialog(birthDateEditText, context)
//                    showTimePickerDialog(birthTimeEditText, context)
//                }
//            }
//
//        }
//    }
//
//    private fun onSaveClicked(view: View) {
//        val updatedName = binding?.studentInputForm?.nameEditText?.text.toString()
//        val updatedId = binding?.studentInputForm?.idEditText?.text.toString()
//        val updatedPhone = binding?.studentInputForm?.phoneEditText?.text.toString()
//        val updatedAddress = binding?.studentInputForm?.addressEditText?.text.toString()
//        val updatedIsChecked = binding?.studentInputForm?.checkBox?.isChecked ?: false
//        val updatedBirthDate = binding?.studentInputForm?.birthDateEditText?.text.toString()
//        val updatedBirthTime = binding?.studentInputForm?.birthTimeEditText?.text.toString()
//
//        student?.apply {
//            // I don't want update id
////            id = updatedId
//            name = updatedName
//            phone = updatedPhone
//            address = updatedAddress
//            isChecked = updatedIsChecked
//            birthDate = updatedBirthDate.takeIf { it.isNotBlank() }
//                ?.let { DateTimeUtils.parseDate(it) }
//            birthTime = updatedBirthTime.takeIf { it.isNotBlank() }?.let {
//                DateTimeUtils.parseTime(it)?.let { birthTime -> Time(birthTime.time) }
//            }
//        }
//
//
//        student?.let {
//            Model.shared.updateStudent(it) {
//                Navigation.findNavController(view).popBackStack()
//                context?.let { it ->
//                    showSuccessOperationDialog(it, "edit")
//                }
//            }
//        }
//
//    }

    private fun onCancelClicked(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    private fun onDeleteClicked(view: View) {
        student?.let {
            Model.shared.deleteStudent(it) {
                Navigation.findNavController(view).popBackStack(
                    R.id.homeFragment,
                    false
                )
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}