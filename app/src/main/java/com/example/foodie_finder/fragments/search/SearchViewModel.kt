package com.example.foodie_finder.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodie_finder.data.model.RestaurantModel
import com.example.foodie_finder.data.networking.Restaurant

class SearchViewModel : ViewModel() {
    val restaurants: LiveData<List<Restaurant>> = RestaurantModel.shared.restaurants

    fun clearRestaurants() {
        RestaurantModel.shared.clearRestaurants()
    }

    fun fetchRestaurants(query: String, callback: (Boolean) -> Unit) {
        RestaurantModel.shared.getRestaurants(query, callback)
    }
}
