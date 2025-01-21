package com.example.mobile_application_course.dialogs.alert

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showSaveSuccessDialog(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Save Successful")
    builder.setMessage("The save operation was completed successfully")
    builder.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
    }

    val dialog = builder.create()
    dialog.show()
}