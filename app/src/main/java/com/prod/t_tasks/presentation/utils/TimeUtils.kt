package com.prod.t_tasks.presentation.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun daysUntilTimestampZoned(targetTimestamp: Long, zoneId: ZoneId = ZoneId.systemDefault()): Int {
	val now = Instant.now().atZone(zoneId)
	val targetTime = Instant.ofEpochMilli(targetTimestamp).atZone(zoneId)

	return ChronoUnit.DAYS.between(now, targetTime).toInt()
}

fun getStartOfDayTimestamp(date: Date): Long {
	val localDate = date.toInstant()
		.atZone(ZoneId.systemDefault())
		.toLocalDate()
	return localDate.atStartOfDay(ZoneId.systemDefault())
		.toInstant()
		.toEpochMilli()
}

fun getStartOfTodayTimestamp(): Long {
	val today = LocalDate.now()
	return today.atStartOfDay(ZoneId.systemDefault())
		.toInstant()
		.toEpochMilli()
}

fun timestampToDate(timestamp: Long, timeZone: TimeZone = TimeZone.getDefault()): String {
	val date = Date(timestamp)
	val formatter = SimpleDateFormat("dd.MM", Locale.getDefault())
	formatter.timeZone = timeZone
	return formatter.format(date)
}

fun timestampToDateWithYear(timestamp: Long, timeZone: TimeZone = TimeZone.getDefault()): String {
	val date = Date(timestamp)
	val formatter = SimpleDateFormat("dd.MM.YYYY", Locale.getDefault())
	formatter.timeZone = timeZone
	return formatter.format(date)
}

fun convertGMTToSystemTimezone(gmtTimestamp: Long): Long {
	return getStartOfDayTimestamp(Date(gmtTimestamp))
}