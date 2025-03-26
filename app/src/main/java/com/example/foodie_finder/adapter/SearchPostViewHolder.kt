package com.example.foodie_finder.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.data.local.Post
import com.example.foodie_finder.databinding.SearchPostBinding
import com.example.foodie_finder.interfaces.OnItemClickListener

class SearchPostViewHolder(
    private val binding: SearchPostBinding,
    listener: OnItemClickListener?,
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


        binding.itemTitle.text = post?.title
        binding.itemContent.text = post?.content

    }
}