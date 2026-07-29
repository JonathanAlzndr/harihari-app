package com.alezandrow.simplecleanarchitecture.presentation.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.toKotlinInstant

fun Long?.toFormattedDate(): String {
    return this?.let {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(this))
    } ?: "Set due date"
}

fun Long?.toFormattedDateTime(): String {
    if (this == null) {
        return ""
    }

    val formatter = SimpleDateFormat(
        "dd MMM yyyy HH:mm",
        Locale.getDefault()
    )

    return formatter.format(Date(this))
}

fun combineDateAndTime(dateMillis: Long, hour: Int, minute: Int): Long {
    return Calendar.getInstance().apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun getTodayMillis(): Long {
    return LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toKotlinInstant().toEpochMilliseconds()
}