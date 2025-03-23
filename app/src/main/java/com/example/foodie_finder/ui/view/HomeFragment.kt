package com.example.foodie_finder.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.data.model.Model
import com.example.foodie_finder.data.local.Student
import com.example.foodie_finder.adapter.UsersRecyclerAdapter
import com.example.foodie_finder.databinding.FragmentPostsListBinding
import com.example.foodie_finder.ui.viewModel.PostsListViewModel

class HomeFragment : Fragment() {
    private var students: List<Student>? = null
    private var adapter: UsersRecyclerAdapter? = null
    private var binding: FragmentPostsListBinding? = null

    private val viewModel: PostsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsListBinding.inflate(inflater, container, false)

        binding?.recyclerView?.setHasFixedSize(true)
        val layoutManger = LinearLayoutManager(context)
        binding?.recyclerView?.layoutManager = layoutManger

        adapter = UsersRecyclerAdapter(viewModel.posts.value)

        adapter?.listener = object : OnItemClickListener {
            override fun onItemClick(id: String) {
                val action =
                    HomeFragmentDirections.actionStudentsListFragmentToStudentDetailsFragment(
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

    override fun onResume() {
        super.onResume()
        getAllStudents()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}