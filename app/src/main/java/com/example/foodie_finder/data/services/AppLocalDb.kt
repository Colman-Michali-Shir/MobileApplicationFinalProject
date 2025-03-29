package com.example.foodie_finder.data.services

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodie_finder.base.FoodieFinderApplication
import com.example.foodie_finder.data.dao.PostDao
import com.example.foodie_finder.data.dao.SavedPostDao
import com.example.foodie_finder.data.entities.Post
import com.example.foodie_finder.data.entities.SavedPost
import com.example.foodie_finder.data.entities.User
import com.example.foodie_finder.data.model.Converters

@Database(entities = [Post::class, User::class, SavedPost::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun savedPostDao(): SavedPostDao
}

object AppLocalDb {

    val database: AppLocalDbRepository by lazy {
        val context = FoodieFinderApplication.Globals.context
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "dbFileName.db"
        ).fallbackToDestructiveMigration()
            .build()

    }
}