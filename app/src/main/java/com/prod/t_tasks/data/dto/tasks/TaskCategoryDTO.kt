package com.prod.t_tasks.data.dto.tasks

import com.prod.t_tasks.domain.model.TaskCategory
import kotlinx.serialization.Serializable

@Serializable
data class TaskCategoryDTO(
	val id: Int,
	val name: String
) {
	fun mapToDomain(): TaskCategory = TaskCategory(id, name)
}