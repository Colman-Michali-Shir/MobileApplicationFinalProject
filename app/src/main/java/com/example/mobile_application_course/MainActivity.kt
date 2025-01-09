package com.example.mobile_application_course

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private var students: MutableList<Student>? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        students = Model.shared.students

        recyclerView = findViewById(R.id.students_list_recycler_view)

        recyclerView?.setHasFixedSize(true)

        val layoutManger = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManger

        val adapter = StudentsRecyclerAdapter(students)

        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(student: Student?) {
                Log.d("TAG", "On student clicked name: ${student?.name}")
            }
        }

        recyclerView?.adapter = adapter


        findViewById<FloatingActionButton>(R.id.student_list_new_student).setOnClickListener {
            val intent = Intent(this, NewStudentActivity::class.java)
            resultLauncher.launch(intent)
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val newStudent = data?.getBooleanExtra("newStudent", false)

                    newStudent?.let {
                        // If newStudent is true, notify the adapter that a new item has been added
                        if (newStudent) {
                            recyclerView?.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

    }

    class StudentViewHolder(itemView: View, listener: OnItemClickListener?) :
        RecyclerView.ViewHolder(itemView) {
        private var nameTextView: TextView? = null
        private var idTextView: TextView? = null
        private var checkBox: CheckBox? = null
        private var student: Student? = null

        init {
            nameTextView = itemView.findViewById(R.id.student_row_name_text_view)
            idTextView = itemView.findViewById(R.id.student_row_id_text_view)
            checkBox = itemView.findViewById(R.id.student_row_check_box)

            checkBox?.apply {
                setOnClickListener { view ->
                    (tag as? Int)?.let {
                        student?.isChecked = (view as? CheckBox)?.isChecked ?: false
                    }
                }
            }

            itemView.setOnClickListener {
                listener?.onItemClick(student)
            }
        }

        fun bind(student: Student?, position: Int) {
            this.student = student
            nameTextView?.text = student?.name
            idTextView?.text = student?.id

            checkBox?.apply {
                isChecked = student?.isChecked ?: false
                tag = position
            }
        }
    }

    class StudentsRecyclerAdapter(private val students: List<Student>?) :
        RecyclerView.Adapter<StudentViewHolder>() {

        var listener: OnItemClickListener? = null


        override fun getItemCount(): Int = students?.size ?: 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            val view =
                inflater.inflate(
                    R.layout.student_list_row,
                    parent,
                    false
                )


            //this function create a new view, like before
            return StudentViewHolder(view, listener)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            // insert the data of every row
            holder.bind(students?.get(position), position)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d("checkkkk", "hiiiiiiiiii")
//
//        if (resultCode == Activity.RESULT_OK) {
//            // Get the new student from the Intent (make sure it implements Parcelable)
//            val newStudent = data?.getBooleanExtra("newStudent", false)
//
//            Log.d("check", newStudent.toString())
//
//            newStudent?.let {
//                // Add the new student to the Model
//
//
//                // Notify the adapter that a new item has been added
//                if (newStudent) {
//                    recyclerView?.adapter?.notifyDataSetChanged()
//                }
//            }
//        }
//    }
}