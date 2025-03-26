package com.example.foodie_finder.data.model.GooglePlaces

import com.google.gson.annotations.SerializedName

data class GooglePlacesResponse(
    val results: List<Restaurant>
)

data class Restaurant(
    val name: String,
    val rating: Double?,
    @SerializedName("formatted_address")
    val formattedAddress: String,
)
