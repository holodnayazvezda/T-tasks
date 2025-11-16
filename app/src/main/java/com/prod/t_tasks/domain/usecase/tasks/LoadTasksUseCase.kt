package com.prod.t_tasks.domain.usecase.tasks

import androidx.paging.PagingData
import com.prod.t_tasks.domain.interfaces.TasksRepository
import com.prod.t_tasks.domain.model.Task
import com.prod.t_tasks.domain.model.TaskFilters
import com.prod.t_tasks.domain.utils.RemotePagingWrapper
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class LoadTasksUseCase(
	private val tasksRepository: TasksRepository
) {
	operator fun invoke(filters: TaskFilters): RemotePagingWrapper<Task> {
		return tasksRepository.loadTasksByFilters(filters.copy(dateEnd = filters.dateEnd + 86400000))
	}
}
