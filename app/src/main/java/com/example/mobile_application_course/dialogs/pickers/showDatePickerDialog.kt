package com.example.mobile_application_course.dialogs.pickers

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import com.example.mobile_application_course.utils.DateTimeUtils
import java.util.Calendar

fun showDatePickerDialog(v: EditText, context: Context?) {
    v.let { editTextDate ->
        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            context?.let { context ->
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(selectedYear, selectedMonth, selectedDay)
                        editTextDate.setText(DateTimeUtils.formatDate(selectedDate.time))
                    }, year, month, day
                )


                datePickerDialog.show()
            }
        }
    }
}