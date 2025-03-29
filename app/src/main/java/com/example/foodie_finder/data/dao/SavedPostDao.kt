package com.example.foodie_finder.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie_finder.data.entities.SavedPost

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePost(savedPost: SavedPost)

    @Query("DELETE FROM saved_posts WHERE userId = :userId AND postId = :postId")
    fun removeSavedPost(userId: String, postId: String)

    @Query("SELECT postId FROM saved_posts WHERE userId =:userId")
    fun getSavedPostsByUser(userId: String): LiveData<List<String>>
}