package com.example.foodie_finder.data.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.data.model.GooglePlaces.GooglePlacesClient
import com.example.foodie_finder.data.model.GooglePlaces.GooglePlacesResponse
import java.util.concurrent.Executors

class RestaurantModel private constructor() {
    private var executor = Executors.newSingleThreadExecutor()
    val restaurants: MutableLiveData<GooglePlacesResponse> = MutableLiveData()

    companion object {
        val shared = RestaurantModel()
    }

    fun getPopularMovies(query: String) {
        executor.execute {
            try {
                val request =
                    GooglePlacesClient.placesApiClient.searchRestaurant(query = query)

                val response = request.execute()

                if (response.isSuccessful) {
                    val restaurants = response.body()
                    this.restaurants.postValue(restaurants)
                    Log.e(
                        "TAG",
                        "Fetched restaurants!.. with total number of restaurants ${restaurants?.results?.size ?: 0}"
                    )
                } else {
                    Log.e("TAG", "Failed to fetch restaurants!")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Failed to fetch restaurants! with exception ${e}")
            }
        }
    }
}