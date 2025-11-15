package com.prod.t_tasks.presentation.commonDataModels

data class UITripModel(
	val id: Long,
	val placeName: String,
	val dateRangeString: String,
	val countOfPeoples: Int,
	val readyPercentage: Int?
)
