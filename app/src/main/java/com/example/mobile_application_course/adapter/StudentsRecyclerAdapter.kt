package com.example.mobile_application_course.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_course.interfaces.OnItemClickListener
import com.example.mobile_application_course.R
import com.example.mobile_application_course.model.Student

class StudentsRecyclerAdapter(private var students: List<Student>?) :
    RecyclerView.Adapter<StudentViewHolder>() {

    var listener: OnItemClickListener? = null

    fun set(students: List<Student>?) {
        this.students = students
    }

    override fun getItemCount(): Int = students?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view =
            inflater.inflate(
                R.layout.student_list_row,
                parent,
                false
            )

        return StudentViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students?.get(position), position)
    }
}
