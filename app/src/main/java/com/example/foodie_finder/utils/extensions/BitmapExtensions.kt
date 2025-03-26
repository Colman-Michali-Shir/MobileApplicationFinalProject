package com.example.foodie_finder.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toFile(context: Context, name: String?): File {
    val file = File(context.cacheDir, "${name ?: "temp_image_${System.currentTimeMillis()}"}.jpg")
    try {
        FileOutputStream(file).use { stream ->
            compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }
    } catch (e: Exception) {
        Log.e("CloudinaryModel", "Error saving bitmap to file", e)
    }
    return file
}