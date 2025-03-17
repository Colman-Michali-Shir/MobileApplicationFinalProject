package com.example.foodie_finder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.model.Model
import com.example.foodie_finder.model.Student
import com.example.foodie_finder.adapter.StudentsRecyclerAdapter
import com.example.foodie_finder.databinding.FragmentStudentsListBinding

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