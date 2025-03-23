package com.example.foodie_finder.utils.dialogs.pickers

import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import com.example.foodie_finder.utils.DateTimeUtils
import java.util.Calendar

fun showTimePickerDialog(v: EditText, context: Context?) {
    v.let { editTextTime ->
        editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            context?.let { context ->
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        val selectedTime = Calendar.getInstance()
                        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                        selectedTime.set(Calendar.MINUTE, selectedMinute)
                        editTextTime.setText(DateTimeUtils.formatTime(selectedTime.time))
                    },
                    hour,
                    minute,
                    true // is24HourView
                )
                timePickerDialog.show()
            }
        }
    }
}