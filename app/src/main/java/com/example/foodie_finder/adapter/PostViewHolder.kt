package com.example.foodie_finder.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.PostListRowBinding
import com.squareup.picasso.Picasso
import com.example.foodie_finder.R

class PostViewHolder(private val binding: PostListRowBinding): RecyclerView.ViewHolder(binding.root) {

    private var post: Post? = null

    fun bind(post: Post?){
        binding.userTextView.text = post?.postedBy
        binding.postTitle.text = post?.title
        binding.postContent.text = post?.content
        binding.ratingBar.numStars = post?.rating?.toInt() ?: 0

        post?.imgUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.woman)
                    .into(binding.photoUrlImageView)
            }
        }
    }


}