package com.example.foodie_finder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.PostListRowBinding
import com.example.foodie_finder.interfaces.OnItemClickListener

class PostsAdapter( var posts: List<Post>?): RecyclerView.Adapter<PostViewHolder>() {

    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = posts?.size ?: 0


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
       holder.bind(posts?.get(position))
    }

    fun update(posts: List<Post>) {
        this.posts = posts
    }
}