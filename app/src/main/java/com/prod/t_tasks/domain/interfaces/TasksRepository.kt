package com.prod.t_tasks.domain.interfaces

import com.prod.t_tasks.domain.model.TaskCategory
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

	fun loadTopics(): Flow<Result<List<TaskCategory>>>
}