package com.example.foodie_finder.data.model

import androidx.lifecycle.MutableLiveData
import com.example.foodie_finder.data.model.PostModel.LoadingState
import com.example.foodie_finder.data.networking.GooglePlacesClient
import com.example.foodie_finder.data.networking.Restaurant
import java.util.concurrent.Executors

class RestaurantModel private constructor() {
    private var executor = Executors.newSingleThreadExecutor()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData<LoadingState>()

    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()

    companion object {
        val shared = RestaurantModel()
    }

    fun clearRestaurants() {
        restaurants.value = emptyList()
    }

    fun getRestaurants(query: String, callback: (Boolean) -> Unit) {
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
                callback(response.isSuccessful)
            } catch (e: Exception) {
                this.restaurants.postValue(emptyList())
            } finally {
                loadingState.postValue(LoadingState.LOADED)
            }
        }
    }
}