package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.model.GooglePlaces.GooglePlacesResponse
import com.example.foodie_finder.data.model.RestaurantModel

class SearchViewModel : ViewModel() {
    val restaurants: LiveData<GooglePlacesResponse> = RestaurantModel.shared.restaurants

    fun fetchMovies(query: String) {
        RestaurantModel.shared.getPopularMovies(query)
    }
}
