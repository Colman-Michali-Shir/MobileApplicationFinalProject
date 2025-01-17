package com.example.mobile_application_course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var adapter: StudentsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

                val action =
                    StudentsListFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(
                        position
                    )
                Navigation.findNavController(view).navigate(action)
            }
        }

        recyclerView?.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.student_list_new_student).setOnClickListener {
            val action = StudentsListFragmentDirections.actionGlobalNewStudentFragment()
            Navigation.findNavController(view).navigate(action)
        }

        return view
    }
}