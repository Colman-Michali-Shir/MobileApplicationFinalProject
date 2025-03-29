package com.example.foodie_finder.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.adapter.Search.SearchAdapter
import com.example.foodie_finder.databinding.FragmentSearchBinding

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

        binding?.listRecyclerView?.adapter = adapter

        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            adapter?.updateSearchRestaurants(restaurants)
            adapter?.notifyDataSetChanged()
            binding?.progressBar?.visibility = View.GONE
        }

        binding?.searchButton?.setOnClickListener {
            binding?.progressBar?.visibility = View.VISIBLE

            val searchText = binding?.searchEditText?.text.toString()

            if (searchText.isNotEmpty()) {
                viewModel.fetchRestaurants(searchText) { isSuccessful ->
                    if (!isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to fetch restaurants!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter text to search",
                    Toast.LENGTH_LONG
                ).show()
            }

            binding?.progressBar?.visibility = View.GONE
        }

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        binding?.searchEditText?.text?.clear()
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearRestaurants()
        binding = null
    }
}
