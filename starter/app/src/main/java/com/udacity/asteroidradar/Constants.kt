package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = "DEMO_KEY"
}

fun TODAY(): String {
    val today = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(today)
}

fun SEVEN_DAYS_FROM_NOW(): String {
    val calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    val newDate = calendar.time
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(newDate)
}