package com.prod.t_tasks.domain.model

data class Task(
	val id: Long,
	val name: String,
	val typeId: Int,
	val deadline: Long,
	val archived: Boolean,
	val common: Boolean
)

data class TaskCategory(
	val id: Int,
	val name: String
)

data class TaskFilters(
	val dateStart: Long,
	val dateEnd: Long,
	val topicIds: List<Int>,
	val onlySelf: Boolean
)