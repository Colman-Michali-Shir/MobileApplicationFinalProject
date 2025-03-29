package com.example.foodie_finder.data.networking

import com.example.foodie_finder.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GooglePlacesClient {

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(GooglePlacesInterceptor())
            .build()
    }

    val placesApiClient: GooglePlacesAPI by lazy {
        val retrofitClient = Retrofit.Builder()
            .baseUrl(BuildConfig.GOOGLE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofitClient.create(GooglePlacesAPI::class.java)
    }
}