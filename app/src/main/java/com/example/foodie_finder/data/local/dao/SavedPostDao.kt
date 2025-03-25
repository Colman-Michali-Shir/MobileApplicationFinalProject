package com.example.foodie_finder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie_finder.data.local.SavedPost

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePost(savedPost: SavedPost)

    @Delete
    fun removeSavedPost(savedPost: SavedPost)

    @Query("SELECT postId FROM saved_posts WHERE userId =:userId")
    fun getSavedPostsByUser(userId: String): LiveData<List<String>>
}