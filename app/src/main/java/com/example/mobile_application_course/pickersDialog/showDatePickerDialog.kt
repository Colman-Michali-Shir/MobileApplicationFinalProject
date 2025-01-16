package com.example.mobile_application_course.pickersDialog

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.util.Calendar

fun showDatePickerDialog(v: EditText, context: Context) {
    v.let { editText ->
        editText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year1, month1, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month1 + 1}/$year1"

                    editText.setText(selectedDate)
                }, year, month, day
            )

            datePickerDialog.show()
        }
    }
}