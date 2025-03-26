package com.example.foodie_finder.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.SearchPostBinding
import com.example.foodie_finder.interfaces.OnItemClickListener

class SearchPostsAdapter(
    private var posts: List<Post> = emptyList(),
) : RecyclerView.Adapter<SearchPostViewHolder>() {

    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPostViewHolder {
        val binding = SearchPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchPostViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: SearchPostViewHolder, position: Int) {
        val post = posts[position]
        Log.d("SHIR", "11111111 $post")

        holder.bind(post)
    }

    fun updateSearchPosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }
}