package com.example.mobile_application_course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_application_course.interfaces.OnItemClickListener
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.adapter.StudentsRecyclerAdapter
import com.example.mobile_application_course.databinding.FragmentStudentsListBinding

class StudentsListFragment : Fragment() {
    private var students: List<Student>? = null
    private var adapter: StudentsRecyclerAdapter? = null
    private var binding: FragmentStudentsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentsListBinding.inflate(inflater, container, false)

        binding?.recyclerView?.setHasFixedSize(true)
        adapter = StudentsRecyclerAdapter(students)
        val layoutManger = LinearLayoutManager(context)
        binding?.recyclerView?.layoutManager = layoutManger


        adapter?.listener = object : OnItemClickListener {
            override fun onItemClick(id: String) {
                val action =
                    StudentsListFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(
                        id
                    )
                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
            }
        }

        binding?.recyclerView?.adapter = adapter

        return binding?.root
    }

    private fun getAllStudents() {

        binding?.progressBar?.visibility = View.VISIBLE

        Model.shared.getAllStudents {
            this.students = it
            adapter?.set(it)
            adapter?.notifyDataSetChanged()

            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        getAllStudents()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}