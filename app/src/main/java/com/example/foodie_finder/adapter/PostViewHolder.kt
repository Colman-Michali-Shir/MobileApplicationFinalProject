package com.example.foodie_finder.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.R
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.PostRowBinding
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.squareup.picasso.Picasso

class PostViewHolder(
    private val binding: PostRowBinding,
    private val listener: OnItemClickListener?,
    private val onSavePost: (String, (Boolean) -> Unit) -> Unit,
    private val onRemoveSavePost: (String, (Boolean) -> Unit) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    private var post: Post? = null

    fun bind(post: Post?, isSavedByUser: Boolean, currentUserId: String?) {
        this.post = post

        binding.username.text = post?.username
        binding.postTitle.text = post?.title
        binding.postContent.text = post?.content
        binding.ratingBar.rating = post?.rating?.toFloat() ?: (0).toFloat()

        binding.saveButton.setImageResource(
            if (isSavedByUser) R.drawable.baseline_bookmark_24 else R.drawable.bookmark
        )
        binding.saveButton.tag = isSavedByUser == true

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
        }

        if (post?.postedBy == currentUserId) {
            binding.editButton.visibility = View.VISIBLE
            binding.editButton.setOnClickListener {
                listener?.onEditPost(post)
            }
        } else {
            binding.editButton.visibility = View.GONE
        }

        binding.saveButton.setOnClickListener {
            val isSaved = binding.saveButton.tag as? Boolean ?: false
            post?.let { post ->
                if (isSaved) {
                    binding.saveButton.setImageResource(R.drawable.bookmark)
                    onRemoveSavePost(post.id) { success ->
                        if (!success) {
                            Toast.makeText(
                                itemView.context,
                                "Failed to save post ${post.title}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    binding.saveButton.setImageResource(R.drawable.baseline_bookmark_24)
                    onSavePost(post.id) { success ->
                        if (!success) {
                            Toast.makeText(
                                itemView.context,
                                "Failed to remove post ${post.title}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            binding.saveButton.tag = !isSaved  // Toggle the state
        }
    }


}