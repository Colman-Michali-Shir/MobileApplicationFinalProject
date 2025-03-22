package com.example.foodie_finder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_finder.interfaces.OnItemClickListener
import com.example.foodie_finder.databinding.StudentListRowBinding
import com.example.foodie_finder.model.Student

class StudentsRecyclerAdapter(private var students: List<Student>?) :
    RecyclerView.Adapter<StudentViewHolder>() {

    var listener: OnItemClickListener? = null

    fun set(students: List<Student>?) {
        this.students = students
    }

    override fun getItemCount(): Int = students?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = StudentListRowBinding.inflate(inflater, parent, false)
        
        return StudentViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students?.get(position), position)
    }
}
