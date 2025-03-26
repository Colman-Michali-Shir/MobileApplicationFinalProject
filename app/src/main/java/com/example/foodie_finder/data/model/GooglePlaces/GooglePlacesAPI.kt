package com.example.foodie_finder.data.model.GooglePlaces

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesAPI {

    @GET("maps/api/place/textsearch/json")
    fun searchRestaurant(
        @Query("query") query: String,   // Restaurant name
        @Query("type") type: String = "restaurant",
        @Query("language") language: String = "en",
    ): Call<GooglePlacesResponse>
}