package com.example.foodie_finder.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.model.GooglePlaces.Restaurant
import com.example.foodie_finder.data.model.RestaurantModel

class SearchViewModel : ViewModel() {
    val restaurants: LiveData<List<Restaurant>> = RestaurantModel.shared.restaurants

    fun clearRestaurants() {
        RestaurantModel.shared.clearRestaurants()
    }

    fun fetchMovies(query: String) {
        RestaurantModel.shared.getPopularMovies(query)
    }
}
