package com.prod.t_tasks.data.dto.tasks

import kotlinx.serialization.Serializable

@Serializable
data class ChangeStatusDTO(
	val user_status: String
)

@Serializable
data class ChangeDeadlineDTO(
	val due_time: String
)