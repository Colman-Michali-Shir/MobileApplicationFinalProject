package com.example.foodie_finder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie_finder.data.local.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPost(post: Post)

    @Query("SELECT * FROM posts ORDER BY creationTime DESC")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    fun getPostById(postId: String): Post?

    @Query("SELECT * FROM posts WHERE postedBy = :userId ORDER BY creationTime DESC, lastUpdateTime DESC")
    fun getPostsByUser(userId: String): LiveData<List<Post>>

    @Query("DELETE FROM posts WHERE id = :postId")
    fun deletePost(postId: String)
}