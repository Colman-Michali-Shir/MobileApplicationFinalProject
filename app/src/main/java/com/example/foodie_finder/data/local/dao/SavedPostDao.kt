package com.example.foodie_finder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.data.local.SavedPost

@Dao
interface SavedPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePost(savedPost: SavedPost)

    @Delete
    fun removeSavedPost(savedPost: SavedPost)

    @Query(
        """
    SELECT posts.* FROM posts 
    INNER JOIN saved_posts ON posts.id = saved_posts.postId 
    WHERE saved_posts.userId = :userId
    ORDER BY posts.creationTime DESC
"""
    )
    fun getSavedPostsByUser(userId: String): LiveData<List<Post>>

    @Query("SELECT EXISTS (SELECT 1 FROM saved_posts WHERE userId = :userId AND postId = :postId)")
    fun isPostSaved(userId: String, postId: String): LiveData<Boolean>
}