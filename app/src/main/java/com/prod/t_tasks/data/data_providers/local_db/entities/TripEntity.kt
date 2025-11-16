package com.prod.t_tasks.data.data_providers.local_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prod.t_tasks.domain.model.trip.Trip

@Entity(tableName = "trips")
data class TripEntity(
	@PrimaryKey(autoGenerate = false)
	val id: Long,
	val isMy: Boolean,
	val country: String?,
	val city: String?,
	val purpose: String?,
	val description: String?,
	val startDate: Long,
	val endDate: Long,
	val membersCount: Int,
	val updatedAt: Long = System.currentTimeMillis()
) {
	fun mapToDomain(): Trip = Trip(
		id = id,
		isMy = isMy,
		country = country,
		city = city,
		purpose = purpose,
		description = description,
		startDate = startDate,
		endDate = endDate,
		membersCount = membersCount,
		members = emptyList(),
		tasks = emptyList()
	)

}

fun Trip.mapToDB(): TripEntity = TripEntity(
	id = id,
	isMy = isMy,
	country = country,
	city = city,
	purpose = purpose,
	description = description,
	startDate = startDate,
	endDate = endDate,
	membersCount = membersCount
)
