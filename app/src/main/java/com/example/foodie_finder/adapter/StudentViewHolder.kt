package com.example.foodie_finder.adapter

import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.R
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.databinding.PostListRowBinding
import com.example.foodie_finder.data.local.Student
import com.squareup.picasso.Picasso

class StudentViewHolder(
    private val binding: PostListRowBinding, listener: OnItemClickListener?
) :
    RecyclerView.ViewHolder(binding.root) {

    private var student: Student? = null

    init {
        binding.studentRowCheckBox.apply {
            setOnClickListener { view ->
                (tag as? Int)?.let {
                    student?.isChecked = (view as? CheckBox)?.isChecked ?: false
                }
            }
        }

        itemView.setOnClickListener {
            student?.id?.let { listener?.onItemClick(it) }
        }
    }

    fun bind(student: Student?, position: Int) {
        this.student = student
        binding.studentRowNameTextView.text = student?.name
        binding.studentRowIdTextView.text = student?.id

        binding.studentRowCheckBox.apply {
            isChecked = student?.isChecked ?: false
            tag = position
        }

        student?.avatarUrl?.let { avatarUrl ->
            val url = avatarUrl.ifBlank { return }
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.woman)
                .into(binding.studentRowImageView)
        }
    }
}
