package com.prod.t_tasks.presentation.utils

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

fun daysUntilTimestampZoned(targetTimestamp: Long, zoneId: ZoneId = ZoneId.systemDefault()): Int {
	val now = Instant.now().atZone(zoneId)
	val targetTime = Instant.ofEpochMilli(targetTimestamp).atZone(zoneId)

	return ChronoUnit.DAYS.between(now, targetTime).toInt()
}