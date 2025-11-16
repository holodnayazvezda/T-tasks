package com.prod.t_tasks.data.dto.trips

import com.prod.t_tasks.data.dto.tasks.TaskDTO
import com.prod.t_tasks.domain.model.trip.Trip
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TripDTO(
	val id: Long,
	val start_date: String,
	val end_date: String,
	val owner_id: Long,
	val country: String?,
	val city: String?,
	val description: String?,
	val purpose: String,
	val tasks: List<TaskDTO>,
	val members: List<TripMemberDTO>
) {
	fun mapToDomain(currentUserId: Long): Trip = Trip(
		id = id,
		isMy = owner_id == currentUserId,
		startDate = isoToTimestamp(start_date),
		endDate = isoToTimestamp(end_date),
		city = city,
		country = country,
		description = description,
		purpose = purpose,
		membersCount = members.size,
		members = members.map { it.mapToDomain() },
		tasks = tasks.map { it.mapToDomain(id) }
	)
}

fun isoToTimestamp(isoString: String): Long {
	return Instant.parse(isoString).toEpochMilli()
}