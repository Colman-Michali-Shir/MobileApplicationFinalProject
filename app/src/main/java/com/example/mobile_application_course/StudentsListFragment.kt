package com.example.mobile_application_course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_course.interfaces.OnItemClickListener
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.adapter.StudentsRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StudentsListFragment : Fragment() {

    private var students: MutableList<Student>? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: StudentsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_students_list, container, false)

        students = Model.shared.students

        recyclerView = view.findViewById(R.id.students_list_fragment_recycler_view)

        recyclerView?.setHasFixedSize(true)

        val layoutManger = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManger

        adapter = StudentsRecyclerAdapter(students)

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("TAG", "On click Activity listener on position $position")
//                val intent = Intent(recyclerView?.context, StudentDetailsActivity::class.java)
//                intent.putExtra("studentPosition", position)
//                resultLauncher.launch(intent)


            }
        }

        recyclerView?.adapter = adapter

        val b: FloatingActionButton = view.findViewById(R.id.student_list_new_student)
        val action = StudentsListFragmentDirections.actionGlobalNewStudentFragment()
        b.setOnClickListener(Navigation.createNavigateOnClickListener(action))
//        view.findViewById<FloatingActionButton>(R.id.student_list_new_student).setOnClickListener {
//
//            Log.d("TAG", "FloatingActionButton")
//
//
//            Log.d("TAG", "action $action")
//
//            Navigation.createNavigateOnClickListener(action)
//        }

        return view
    }

}