package com.example.foodie_finder.adapter

import android.util.Log
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.PostListRowBinding
import com.squareup.picasso.Picasso
import com.example.foodie_finder.R
import com.example.foodie_finder.interfaces.OnItemClickListener

class PostViewHolder(private val binding: PostListRowBinding,  listener: OnItemClickListener?): RecyclerView.ViewHolder(binding.root) {

    private var post: Post? = null

    init {

        itemView.setOnClickListener {
            Log.d("TAG", "On click listener on $post")
            listener?.onItemClick(post)
        }
    }

    fun bind(post: Post?){
        this.post = post

        binding.userTextView.text = post?.postedBy
        binding.postTitle.text = post?.title
        binding.postContent.text = post?.content
        binding.ratingBar.rating = post?.rating?.toFloat() ?: (0).toFloat()

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