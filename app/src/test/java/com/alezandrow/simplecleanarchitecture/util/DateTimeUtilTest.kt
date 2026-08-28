package com.alezandrow.simplecleanarchitecture.util

import com.alezandrow.simplecleanarchitecture.common.GreetingType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateTimeUtilTest {

    @Test
    fun `convertToLocalTime should return correct time`() {

        val hour = 14
        val minute = 30

        val result = convertToLocalTime(hour, minute)

        assertEquals(LocalTime.of(14, 30), result)
    }

    @Test
    fun `convertToLocalDateTime should combine date and time`() {
        val date = LocalDate.of(2026, 8, 28)
        val time = LocalTime.of(14, 30)

        val result = convertToLocalDateTime(date, time)

        assertEquals(
            LocalDateTime.of(2026, 8, 28, 14, 30),
            result
        )
    }

    @Test
    fun `Instant toDbMillis should return epoch millis`() {
        val instant = Instant.parse("2026-08-28T06:30:00Z")

        val result = instant.toDbMillis()

        assertEquals(instant.toEpochMilli(), result)
    }

    @Test
    fun `Long toLocalDate should convert millis using UTC`() {

        val millis = Instant.parse("2026-08-28T06:30:00Z").toEpochMilli()

        val result = millis.toLocalDate()

        assertEquals(
            LocalDate.of(2026, 8, 28),
            result
        )
    }

    @Test
    fun `LocalDateTime toInstantWithTimeZone should use system timezone`() {

        val dateTime = LocalDateTime.of(
            2026,
            8,
            28,
            14,
            30
        )

        val result = dateTime.toInstantWithSystemZone()

        val expected = dateTime.atZone(ZoneId.systemDefault()).toInstant()

        assertEquals(expected, result)

    }

    @Test
    fun `Long toLocalTimeAtSystemZone should return local time`() {

        val millis = Instant.parse("2026-08-28T06:30:00Z").toEpochMilli()

        val result = millis.toLocalTimeAtSystemZone()

        val expected = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalTime()

        assertEquals(expected, result)
    }

    @Test
    fun `toFormattedDate should format millis correctly`() {

        val millis = Instant.parse("2026-08-28T00:00:00Z").toEpochMilli()

        val result = millis.toFormattedDate(ZoneId.of("Asia/Makassar"))

        val expected = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.of("Asia/Makassar"))
            .toLocalDate()
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMM yyyy",
                    Locale.ENGLISH
                )
            )

        assertEquals(expected, result)
    }

    @Test
    fun `toFormattedDate should return set due date when millis is null`() {
        val millis: Long? = null

        val result = millis.toFormattedDate()

        assertEquals("Set due date", result)
    }

    @Test
    fun `combineDateAndTime should combine date and time correctly`() {

        val dateMillis = Instant
            .parse("2026-08-28T00:00:00Z")
            .toEpochMilli()

        val hour = 14
        val minute = 30

        val result = combineDateAndTime(
            dateMillis = dateMillis,
            hour = hour,
            minute = minute
        )

        val expected = LocalDate
            .of(2026, 8, 28)
            .atTime(14, 30)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        assertEquals(expected, result)
    }

    @Test
    fun `toDatePickerMillis should return start of local date in UTC`() {

        val millis = Instant.parse(
            "2026-08-28T06:30:00Z"
        ).toEpochMilli()

        val result = millis.toDatePickerMillis()

        val localDate = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val expected = localDate
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        assertEquals(expected, result)
    }

    @Test
    fun `getGreeting at 11 59 should return MORNING`() {
        assertEquals(
            GreetingType.MORNING,
            getGreeting(LocalTime.of(11, 59))
        )
    }

    @Test
    fun `getGreeting at 12 00 should return AFTERNOON`() {
        assertEquals(
            GreetingType.AFTERNOON,
            getGreeting(LocalTime.of(12, 0))
        )
    }

    @Test
    fun `getGreeting at 16 59 should return AFTERNOON`() {
        assertEquals(
            GreetingType.AFTERNOON,
            getGreeting(LocalTime.of(16, 59))
        )
    }

    @Test
    fun `getGreeting at 17 00 should return EVENING`() {
        assertEquals(
            GreetingType.EVENING,
            getGreeting(LocalTime.of(17, 0))
        )
    }

    @Test
    fun `getGreeting at 21 59 should return EVENING`() {
        assertEquals(
            GreetingType.EVENING,
            getGreeting(LocalTime.of(21, 59))
        )
    }

    @Test
    fun `getGreeting at 22 00 should return NIGHT`() {
        assertEquals(
            GreetingType.NIGHT,
            getGreeting(LocalTime.of(22, 0))
        )
    }

    @Test
    fun `toFormattedDateTime should return empty string when millis is null`() {
        val millis: Long? = null

        val result = millis.toFormattedDateTime()

        assertEquals("", result)
    }

    @Test
    fun `toFormattedDateTime should format millis correctly`() {
        val millis = Instant
            .parse("2026-08-28T06:30:00Z")
            .toEpochMilli()

        val result = millis.toFormattedDateTime()

        val expected = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .format(
                DateTimeFormatter.ofPattern(
                    "dd MMM yyyy HH:mm",
                    Locale.getDefault()
                )
            )

        assertEquals(expected, result)
    }

    @Test
    fun `getTodayMillis should return start of given date in UTC`() {
        val date = LocalDate.of(2026, 8, 28)

        val result = getTodayMillis(date)

        val expected = date
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

        assertEquals(expected, result)
    }

    @Test
    fun `getGreeting at midnight should return MORNING`() {
        assertEquals(
            GreetingType.MORNING,
            getGreeting(LocalTime.MIDNIGHT)
        )
    }

    @Test
    fun `getGreeting at 23 59 should return NIGHT`() {
        assertEquals(
            GreetingType.NIGHT,
            getGreeting(LocalTime.of(23, 59))
        )
    }

}