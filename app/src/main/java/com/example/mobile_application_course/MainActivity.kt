package com.example.mobile_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_application_course.interfaces.OnItemClickListener
import com.example.mobile_application_course.model.Model
import com.example.mobile_application_course.model.Student
import com.example.mobile_application_course.adapter.StudentsRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private var students: MutableList<Student>? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: StudentsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUp()
    }

    private fun setUp() {
        students = Model.shared.students

        recyclerView = findViewById(R.id.students_list_recycler_view)

        recyclerView?.setHasFixedSize(true)

        val layoutManger = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManger

        adapter = StudentsRecyclerAdapter(students)

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d("TAG", "On click Activity listener on position $position")
                val intent = Intent(recyclerView?.context, StudentDetailsActivity::class.java)
                intent.putExtra("studentPosition", position)
                resultLauncher.launch(intent)
            }
        }

        recyclerView?.adapter = adapter

        findViewById<FloatingActionButton>(R.id.student_list_new_student).setOnClickListener {
            val intent = Intent(this, NewStudentActivity::class.java)
            resultLauncher.launch(intent)
        }

        setUpResultLauncher()
    }

    private fun setUpResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val action = result.data?.getStringExtra("action")
                    when (action) {
                        "add" -> {
                            students?.let {
                                recyclerView?.adapter?.notifyItemInserted(it.size - 1) // Update adapter for addition
                            }
                        }

                        "edit" -> {
                            val position = result.data?.getIntExtra("editStudentPosition", -1)
                            if (position != -1 && position != null) {
                                adapter.notifyItemChanged(position)
                            }
                        }

                        "delete" -> {
                            val position = result.data?.getIntExtra("deletedStudentPosition", -1)
                            if (position != -1 && position != null) {
                                adapter.notifyItemRemoved(position)
                            }
                        }
                    }
                }
            }
    }
}