package com.prod.t_tasks.data.dto.users

import com.prod.t_tasks.domain.model.User
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
	val id: Long,
	val first_name: String,
	val second_name: String,
	val email: String,
	val avatar_file_key: String
) {
	fun mapToUser(): User = User(
		id = id,
		firstName = first_name,
		secondName = second_name,
		email = email,
		avatarFileKey = avatar_file_key
	)
}