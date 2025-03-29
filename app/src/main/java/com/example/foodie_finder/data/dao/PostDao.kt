package com.example.foodie_finder.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie_finder.data.entities.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPost(post: Post)

    @Query("SELECT * FROM posts ORDER BY creationTime DESC")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE postedBy = :userId ORDER BY creationTime DESC, lastUpdateTime DESC")
    fun getPostsByUser(userId: String): LiveData<List<Post>>

    @Query("DELETE FROM posts WHERE id = :postId")
    fun deletePost(postId: String)
}