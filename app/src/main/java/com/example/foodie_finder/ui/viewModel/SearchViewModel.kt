package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.model.PostModel

class SearchViewModel : ViewModel() {
    // Use MutableLiveData to allow updates to the list
    private val _searchedPosts = MutableLiveData<List<Post>>()
    val searchedPosts: LiveData<List<Post>> get() = _searchedPosts

    // Assuming PostModel.shared.allPosts is a LiveData<List<Post>>
    private var allPosts: LiveData<List<Post>> = PostModel.shared.allPosts

    fun searchInPosts(search: String) {
        // Filter the posts based on the search string
        val filteredPosts = allPosts.value?.filter {
            it.title.contains(search, ignoreCase = true) ||
                    it.content.contains(search, ignoreCase = true)
        } ?: emptyList()

        // Update the MutableLiveData with the filtered list
        _searchedPosts.value = filteredPosts
    }
}
