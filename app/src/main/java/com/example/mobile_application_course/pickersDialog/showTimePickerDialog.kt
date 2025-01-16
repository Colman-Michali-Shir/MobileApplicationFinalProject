package com.example.mobile_application_course.pickersDialog

import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import java.util.Calendar
import java.util.Locale

fun showTimePickerDialog(v: EditText, context: Context) {
    v.let { editText ->
        editText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute1 ->
                    editText.setText(
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            hourOfDay,
                            minute1
                        )
                    )
                },
                hour, minute, true
            )
            timePickerDialog.show()
        }
    }
}