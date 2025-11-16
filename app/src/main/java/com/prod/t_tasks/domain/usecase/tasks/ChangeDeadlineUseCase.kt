package com.prod.t_tasks.domain.usecase.tasks

import com.prod.t_tasks.domain.interfaces.TasksRepository
import org.koin.core.annotation.Single


@Single
class ChangeDeadlineUseCase(
	private val tasksRepository: TasksRepository
) {
	suspend operator fun invoke(tripId: Long, taskId: Long, deadline: Long) {
		tasksRepository.patchTaskDeadline(tripId, taskId, deadline)
	}
}
