package com.alezandrow.simplecleanarchitecture.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long?.toFormattedDate(): String {
    return this?.let {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    } ?: "Set due date"
}

fun Long?.toFormattedDateTime(): String {
    if (this == null) return ""

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.getDefault())
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(formatter)
}

fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}

fun convertToLocalTime(hour: Int, minute: Int): LocalTime {
    return LocalTime.of(hour, minute)
}

fun convertToLocalDateTime(localDate: LocalDate, localTime: LocalTime): LocalDateTime {
    return LocalDateTime.of(localDate, localTime)
}

fun LocalDateTime.toInstantWithSystemZone(): Instant {
    return this.atZone(ZoneId.systemDefault()).toInstant()
}

fun Instant.toDbMillis(): Long =
    this.toEpochMilli()

fun combineDateAndTime(dateMillis: Long, hour: Int, minute: Int): Long {
    val localDate = dateMillis.toLocalDate()
    val localTime = convertToLocalTime(hour, minute)
    val localDateTime = convertToLocalDateTime(localDate, localTime)
    return localDateTime.toInstantWithSystemZone().toDbMillis()
}

fun Long.toLocalTimeAtSystemZone(): LocalTime {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
}

fun getTodayMillis(): Long {
    return LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

fun Long.toDatePickerMillis(): Long {
    val localDate = Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}
