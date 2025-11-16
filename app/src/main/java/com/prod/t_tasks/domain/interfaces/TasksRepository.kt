package com.prod.t_tasks.domain.interfaces

import com.prod.t_tasks.data.dto.tasks.CreateTaskDTO
import com.prod.t_tasks.domain.model.Task
import com.prod.t_tasks.domain.model.TaskCategory
import com.prod.t_tasks.domain.model.TaskFilters
import com.prod.t_tasks.domain.utils.RemotePagingWrapper
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

	fun loadTopics(): Flow<Result<List<TaskCategory>>>

	fun loadTasksByFilters(filters: TaskFilters): RemotePagingWrapper<Task>
	suspend fun createTask(
		tripId: Long,
		createTaskDTO: CreateTaskDTO
	): Result<Task>

	suspend fun markTaskAsFinished(
		tripId: Long,
		taskId: Long
	)

	suspend fun markTaskOpen(
		tripId: Long,
		taskId: Long
	)
}