package com.example.foodie_finder.adapter.Search

import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.model.GooglePlaces.Restaurant
import com.example.foodie_finder.databinding.SearchRowBinding

class SearchViewHolder(
    private val binding: SearchRowBinding,
) :
    RecyclerView.ViewHolder(binding.root) {

    private var restaurant: Restaurant? = null

    fun bind(restaurant: Restaurant?) {
        this.restaurant = restaurant

        binding.nameTextView.text = restaurant?.name
        binding.addressTextView.text = restaurant?.formattedAddress
        binding.ratingBar.rating = restaurant?.rating?.toFloat() ?: 0f

    }
}