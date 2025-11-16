package com.prod.t_tasks.domain.usecase.tasks

import com.prod.t_tasks.domain.interfaces.TasksRepository
import org.koin.core.annotation.Single

@Single
class SetFinishedStateToTaskUseCase (private val tasksRepository: TasksRepository) {
	suspend operator fun invoke(tripId: Long, taskId: Long, finishedState: Boolean){
		if (finishedState){
			tasksRepository.markTaskAsFinished(tripId = tripId, taskId = taskId)
		} else{
			tasksRepository.markTaskOpen(tripId = tripId, taskId = taskId)
		}
	}
}
