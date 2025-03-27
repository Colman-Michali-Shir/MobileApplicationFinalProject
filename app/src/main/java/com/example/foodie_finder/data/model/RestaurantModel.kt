package com.example.foodie_finder.data.model

import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.data.model.GooglePlaces.GooglePlacesClient
import com.example.foodie_finder.data.model.GooglePlaces.Restaurant
import com.example.foodie_finder.data.model.PostModel.LoadingState
import java.util.concurrent.Executors

class RestaurantModel private constructor() {
    private var executor = Executors.newSingleThreadExecutor()
    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    companion object {
        val shared = RestaurantModel()
    }

    fun getPopularMovies(query: String) {
        loadingState.postValue(LoadingState.LOADING)

        executor.execute {
            try {
                val request =
                    GooglePlacesClient.placesApiClient.searchRestaurant(query = query)

                val response = request.execute()

                if (response.isSuccessful) {
                    val restaurants = response.body()
                    this.restaurants.postValue(restaurants?.results)
                } else {
                    this.restaurants.postValue(emptyList())
                }
            } catch (e: Exception) {
                this.restaurants.postValue(emptyList())
            } finally {
                loadingState.postValue(LoadingState.LOADED)
            }
        }
    }
}