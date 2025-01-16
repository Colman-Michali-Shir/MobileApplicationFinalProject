package com.example.mobile_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.navigation.Navigation
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student

class StudentDetailsFragment : Fragment() {

    private var student: Student? = null
    private var currentPosition: Int = 0

    private var nameTextView: TextView? = null
    private var idTextView: TextView? = null
    private var phoneTextView: TextView? = null
    private var addressTextView: TextView? = null
    private var checkBox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentPosition = arguments?.let {
            StudentDetailsFragmentArgs.fromBundle(it).position
        } ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_details, container, false)

        setUp(view)

        return view
    }

    private fun setUp(view: View) {

        nameTextView = view.findViewById(R.id.student_details_fragment_name_text_view)
        idTextView = view.findViewById(R.id.student_details_fragment_id_text_view)
        phoneTextView = view.findViewById(R.id.student_details_fragment_phone_text_view)
        addressTextView = view.findViewById(R.id.student_details_fragment_address_text_view)
        checkBox = view.findViewById(R.id.student_details_fragment_check_box)


        student = Model.shared.getStudentInPosition(currentPosition)

        student.let {
            nameTextView?.text = it?.name
            idTextView?.text = it?.id
            phoneTextView?.text = it?.phone
            addressTextView?.text = it?.address
            checkBox?.isChecked = it?.isChecked ?: false
        }

//
        view.findViewById<Button>(R.id.student_details_fragment_edit_button).setOnClickListener {
//            val intent = Intent(this, EditStudentActivity::class.java)
//            intent.putExtra("studentPosition", currentPosition)
//            resultLauncher.launch(intent)
            val action =
                StudentDetailsFragmentDirections.actionStudentDetailsFragmentToEditStudentFragment(
                    currentPosition
                )
            Navigation.findNavController(view).navigate(action)
        }
    }

//    private fun setUpResultLauncher() {
//        resultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    val action = result.data?.getStringExtra("action")
//                    when (action) {
//                        "edit" -> {
//                            val student = Model.shared.getStudentInPosition(currentPosition)
//                            student.let {
//                                idTextView?.text = it.id
//                                nameTextView?.text = it.name
//                                phoneTextView?.text = it.phone
//                                addressTextView?.text = it.address
//                                checkBox?.isChecked = it.isChecked
//                            }
//
//                            val resultIntent = Intent()
//                            resultIntent.putExtra("editStudentPosition", currentPosition)
//                            resultIntent.putExtra("action", "edit")
//                            setResult(RESULT_OK, resultIntent)
//                        }
//
//                        "delete" -> {
//                            val resultIntent = Intent()
//                            resultIntent.putExtra(
//                                "deletedStudentPosition",
//                                currentPosition
//                            )
//                            resultIntent.putExtra("action", "delete")
//                            setResult(RESULT_OK, resultIntent)
//                            finish()
//                        }
//                    }
//                }
//            }
//    }


}