package com.prod.t_tasks.domain.usecase.tasks

import androidx.paging.PagingData
import com.prod.t_tasks.domain.model.Task
import com.prod.t_tasks.domain.model.TaskFilters
import com.prod.t_tasks.domain.utils.RemotePagingWrapper
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class LoadTasksUseCase() {
	suspend operator fun invoke(filters: TaskFilters): RemotePagingWrapper<Task> {
		return flow {
			emit(
				PagingData.from(
					listOf(
						Task(
							0,
							"Task1",
							0,
							1763326800,
							archived = false,
							common = false
						),
						Task(
							1,
							"Task2",
							0,
							1763326800,
							archived = true,
							common = false
						),
						Task(
							2,
							"Task3",
							0,
							1763326800,
							archived = false,
							common = true
						),
						Task(
							3,
							"Task4",
							0,
							1763326800,
							archived = false,
							common = false
						),
                        Task(
                            0,
                            "Task1",
                            0,
                            1763326800,
                            archived = false,
                            common = false
                        ),
                        Task(
                            1,
                            "Task2",
                            0,
                            1763326800,
                            archived = true,
                            common = false
                        ),
                        Task(
                            2,
                            "Task3",
                            0,
                            1763326800,
                            archived = false,
                            common = true
                        ),
                        Task(
                            3,
                            "Task4",
                            0,
                            1763326800,
                            archived = false,
                            common = false
                        )
					)
				)
			)
		}
	}
}
