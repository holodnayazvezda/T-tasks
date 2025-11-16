package com.prod.t_tasks.presentation.commonDataModels

import com.prod.t_tasks.domain.model.trip.Trip
import com.prod.t_tasks.presentation.utils.timestampToDateWithYear

data class UITripModel(
	val id: Long,
	val placeName: String,
	val dateRangeString: String,
	val countOfPeoples: Int,
	val readyPercentage: Int?
)

fun Trip.mapToUI(): UITripModel = UITripModel(
	id = id,
	placeName = "${country?.let { if (it.isNotBlank()) "$it - " else "" }}${city?:""}",
	dateRangeString = "${timestampToDateWithYear(this.startDate)}-${timestampToDateWithYear(this.endDate)}",
	countOfPeoples = membersCount,
	readyPercentage = null
)
