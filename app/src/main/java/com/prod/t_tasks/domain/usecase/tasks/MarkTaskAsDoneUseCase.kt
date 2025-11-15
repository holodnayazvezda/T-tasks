package com.prod.t_tasks.domain.usecase.tasks

import org.koin.core.annotation.Single

@Single
class MarkTaskAsDoneUseCase {
	suspend operator fun invoke(): Result<String> {
		return Result.success("Success")
	}
}
