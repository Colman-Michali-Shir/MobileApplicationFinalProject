package com.example.foodie_finder.adapter.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.model.GooglePlaces.Restaurant
import com.example.foodie_finder.databinding.SearchRowBinding

class SearchAdapter(
    private var restaurants: List<Restaurant> = emptyList(),
) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            SearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int = restaurants.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    fun updateSearchRestaurants(restaurants: List<Restaurant>) {
        this.restaurants = restaurants
    }
}