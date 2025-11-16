package com.prod.t_tasks.data.dto.trips

import com.prod.t_tasks.domain.model.trip.TripMember
import kotlinx.serialization.Serializable

@Serializable
data class TripMemberDTO(
	val id: Long,
	val email: String,
	val first_name: String,
	val second_name: String?,
	val avatar_file_key: String?
) {
	fun mapToDomain(): TripMember = TripMember(
		id = id,
		email = email,
		firstName = first_name,
		secondName = second_name,
		avatarFileKey = avatar_file_key
	)
}