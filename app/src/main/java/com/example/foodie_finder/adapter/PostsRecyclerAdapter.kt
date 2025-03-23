package com.example.foodie_finder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.databinding.PostListRowBinding
import com.example.foodie_finder.data.local.Student

class PostsRecyclerAdapter(private var students: List<Student>?) :
    RecyclerView.Adapter<StudentViewHolder>() {

    var listener: OnItemClickListener? = null

    fun set(students: List<Student>?) {
        this.students = students
    }

    override fun getItemCount(): Int = students?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = PostListRowBinding.inflate(inflater, parent, false)
        
        return StudentViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students?.get(position), position)
    }
}
