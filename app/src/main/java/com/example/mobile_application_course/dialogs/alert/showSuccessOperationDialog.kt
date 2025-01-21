package com.example.mobile_application_course.dialogs.alert

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun showSuccessOperationDialog(context: Context, operation: String = "save") {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("${operation.replaceFirstChar { it.uppercase() }} Successful")
    builder.setMessage("The $operation operation was completed successfully")
    builder.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
    }

    val dialog = builder.create()
    dialog.show()
}