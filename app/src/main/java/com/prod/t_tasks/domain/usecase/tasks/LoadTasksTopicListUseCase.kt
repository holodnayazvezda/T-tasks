package com.prod.t_tasks.domain.usecase.tasks

import com.prod.t_tasks.domain.interfaces.TasksRepository
import com.prod.t_tasks.domain.model.TaskCategory
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LoadTasksTopicListUseCase(private val tasksRepository: TasksRepository) {
	operator fun invoke(): Flow<Result<List<TaskCategory>>> {
		return tasksRepository.loadTopics()
	}
}
