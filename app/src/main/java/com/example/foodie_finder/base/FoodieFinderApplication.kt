package com.example.foodie_finder.base

import android.app.Application
import android.content.Context

class FoodieFinderApplication : Application() {

    object Globals {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Globals.context = applicationContext
    }
}
