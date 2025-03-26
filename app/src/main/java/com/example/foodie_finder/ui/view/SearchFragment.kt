package com.example.foodie_finder.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.adapter.SearchAdapter
import com.example.foodie_finder.databinding.FragmentSearchBinding
import com.example.foodie_finder.ui.viewModel.SearchViewModel

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private var adapter: SearchAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding?.listRecyclerView?.setHasFixedSize(true)
        binding?.listRecyclerView?.layoutManager = LinearLayoutManager(context)

        adapter = SearchAdapter(emptyList())

        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            Log.d("TAG", "Observed restaurants: $restaurants") // Debug log

            adapter?.updateSearchRestaurants(restaurants.results)
            adapter?.notifyDataSetChanged()
        }

        binding?.listRecyclerView?.adapter = adapter


        binding?.searchEditText?.setOnTouchListener { v, event ->
            val drawableStart = binding?.searchEditText?.compoundDrawablesRelative?.get(0)
            val drawableEnd = binding?.searchEditText?.compoundDrawablesRelative?.get(2)
            Log.d("SHIR", "1111111111111111")


            // Handle touch event on ACTION_UP
            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the touch is within the bounds of the left drawable (search icon)
                drawableStart?.let {
                    if (event.x <= it.bounds.right) {
                        // Handle left drawable click (search icon)
                        Toast.makeText(requireContext(), "Search icon clicked", Toast.LENGTH_SHORT)
                            .show()

                        // Trigger search action based on the input text
                        viewModel.fetchMovies(binding?.searchEditText?.text.toString())

                        // Register the click for accessibility purposes
                        v.performClick()
                        return@setOnTouchListener true
                    }
                }

                // Check if the touch is within the bounds of the right drawable (close icon)
                drawableEnd?.let {
                    val drawableEndX = v.width - it.bounds.left
                    if (event.x >= drawableEndX) {
                        // Handle right drawable click (close icon)
                        binding?.searchEditText?.setText("")  // Clear the search text

                        // Register the click for accessibility purposes
                        v.performClick()
                        return@setOnTouchListener true
                    }
                }
            }
            false // Let other touch events pass
        }


//        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(
//                charSequence: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//                // You can use this if you need to perform an action before the text is changed.
//            }
//
//            override fun onTextChanged(
//                charSequence: CharSequence?,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                val searchQuery = charSequence.toString()
//                viewModel.fetchMovies()
//            }
//
//            override fun afterTextChanged(editable: Editable?) {
//                // You can use this if you need to perform an action after the text is changed.
//            }
//        })

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
