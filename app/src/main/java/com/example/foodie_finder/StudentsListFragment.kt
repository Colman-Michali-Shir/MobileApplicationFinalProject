package com.example.foodie_finder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.adapter.StudentsRecyclerAdapter
import com.example.foodie_finder.databinding.FragmentStudentsListBinding
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.model.Model
import com.example.foodie_finder.model.Student

class StudentsListFragment : Fragment() {
    private var students: List<Student>? = null
    private var adapter: StudentsRecyclerAdapter? = null
    private var binding: FragmentStudentsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentsListBinding.inflate(inflater, container, false)
//        private var viewModel: StudentViewModel? = null
//
//        override fun onAttach(context: Context) {
//            super.onAttach(context)
//            viewModel = ViewModelProvider(this)[StudentsListViewModel::class.java]
//        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.newStudentFragment -> {
                val action = StudentsListFragmentDirections
                    .actionStudentsListFragmentToNewStudentFragment()
                findNavController().navigate(action)
                true
            }

            R.id.logout -> {
                logoutUser()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        findNavController().navigate(R.id.action_studentsListFragment_to_loginFragment)
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