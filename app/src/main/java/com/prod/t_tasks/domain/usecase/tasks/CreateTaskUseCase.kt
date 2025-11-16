package com.prod.t_tasks.domain.usecase.tasks

import com.prod.t_tasks.data.dto.tasks.CreateTaskDTO
import com.prod.t_tasks.domain.interfaces.TasksRepository
import com.prod.t_tasks.domain.model.CreateTask
import com.prod.t_tasks.domain.model.trip.TripsTask
import com.prod.t_tasks.presentation.utils.timestampToIso
import org.koin.core.annotation.Single

@Single
class CreateTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend operator fun invoke(createTask: CreateTask): Result<TripsTask> {
        return tasksRepository.createTask(
            tripId = createTask.tripId,
            createTaskDTO = CreateTaskDTO(
                title = createTask.title,
                description = createTask.description,
                dueTime = timestampToIso(createTask.dueTime),
                status = "pending",
                categoriesIds = createTask.categoriesIds,
                isCommon = createTask.isCommon
            )
        )

    }
}