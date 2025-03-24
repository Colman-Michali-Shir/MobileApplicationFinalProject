package com.example.foodie_finder.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.R
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.PostListRowBinding
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.squareup.picasso.Picasso

class PostViewHolder(
    private val binding: PostListRowBinding,
    listener: OnItemClickListener?,
    private val onSavePost: (String) -> Unit,
    private val onRemoveSavePost: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    private var post: Post? = null

    init {
        itemView.setOnClickListener {
            listener?.onItemClick(post)
        }
    }

    fun bind(post: Post?) {
        this.post = post

        binding.username.text = post?.username
        binding.postTitle.text = post?.title
        binding.postContent.text = post?.content
        binding.ratingBar.rating = post?.rating?.toFloat() ?: (0).toFloat()

        post?.userProfileImg?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.woman)
                    .into(binding.userProfilePicture)
            }
        }

        post?.imgUrl?.let {
            if (it.isNotBlank()) {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.woman)
                    .into(binding.photoUrlImageView)
            }


            binding.saveButton.setOnClickListener {
                val isSaved = binding.saveButton.tag as? Boolean ?: false

                if (isSaved) {
                    binding.saveButton.setImageResource(R.drawable.bookmark)
                    onRemoveSavePost(post.id)
                } else {
                    binding.saveButton.setImageResource(R.drawable.baseline_bookmark_24)
                    onSavePost(post.id)
                }

                binding.saveButton.tag = !isSaved  // Toggle the state
            }
        }

    }
}