package com.prod.t_tasks.data.data_providers.api.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

internal fun String.parseToTimestamp(): Long? {
	val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
	dateFormatter.timeZone = TimeZone.getTimeZone("UTC")

	return dateFormatter.parse(this)?.time
}