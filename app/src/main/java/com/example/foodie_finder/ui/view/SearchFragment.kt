package com.example.foodie_finder.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_finder.adapter.SearchPostsAdapter
import com.example.foodie_finder.databinding.FragmentSearchBinding
import com.example.foodie_finder.ui.viewModel.SearchViewModel

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()
    private var adapter: SearchPostsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding?.listRecyclerView?.setHasFixedSize(true)
        binding?.listRecyclerView?.layoutManager = LinearLayoutManager(context)

        adapter = SearchPostsAdapter(
            viewModel.searchedPosts.value ?: emptyList(),

            )

        viewModel.searchedPosts.observe(viewLifecycleOwner) { posts ->
            Log.d("TAG", "Observed posts: $posts") // Debug log

            adapter?.updateSearchPosts(posts)
            adapter?.notifyDataSetChanged()
        }

        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // You can use this if you need to perform an action before the text is changed.
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val searchQuery = charSequence.toString()
                viewModel.searchInPosts(searchQuery)
            }

            override fun afterTextChanged(editable: Editable?) {
                // You can use this if you need to perform an action after the text is changed.
            }
        })

        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // You can use this if you need to perform an action before the text is changed.
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val searchQuery = charSequence.toString()
                viewModel.searchInPosts(searchQuery)
            }

            override fun afterTextChanged(editable: Editable?) {
                // You can use this if you need to perform an action after the text is changed.
            }
        })

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
