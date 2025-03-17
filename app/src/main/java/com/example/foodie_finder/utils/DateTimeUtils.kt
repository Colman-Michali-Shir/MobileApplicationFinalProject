package com.example.foodie_finder.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date.time)
    }

    fun formatTime(time: Date): String {
        return timeFormat.format(time.time)
    }

    fun parseDate(date: String): Date? {
        return dateFormat.parse(date)

    }

    fun parseTime(time: String): Date? {
        return timeFormat.parse(time)
    }
}