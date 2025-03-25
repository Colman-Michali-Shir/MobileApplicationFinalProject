package com.example.foodie_finder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.PostListRowBinding
import com.example.foodie_finder.interfaces.OnItemClickListener

class PostsAdapter(
    private var posts: List<Post> = emptyList(),
    private var savedPosts: List<String> = emptyList(),
    private val onSavePost: (String, (Boolean) -> Unit) -> Unit,
    private val onRemoveSavePost: (String, (Boolean) -> Unit) -> Unit,
    private var isSavedPostsMode: Boolean = false

) : RecyclerView.Adapter<PostViewHolder>() {

    var listener: OnItemClickListener? = null
    private var filteredPosts: List<Post> = posts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener, onSavePost, onRemoveSavePost)
    }

    override fun getItemCount(): Int = filteredPosts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = filteredPosts[position]
        holder.bind(post, savedPosts.contains(post.id))
    }

    private fun filterPosts() {
        filteredPosts = if (isSavedPostsMode) {
            posts.filter { savedPosts.contains(it.id) }
        } else {
            posts
        }
    }

    fun updateAllPosts(posts: List<Post>) {
        this.posts = posts
        filterPosts()
    }

    fun updateSavedPosts(savedPosts: List<String>) {
        this.savedPosts = savedPosts
        filterPosts()
    }
}