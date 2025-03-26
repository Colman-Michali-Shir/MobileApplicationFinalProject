package com.example.foodie_finder.data.model.GooglePlaces

import com.example.foodie_finder.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class GooglePlacesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url()

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(
                "key",
                BuildConfig.GOOGLE_API_KEY
            )
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}