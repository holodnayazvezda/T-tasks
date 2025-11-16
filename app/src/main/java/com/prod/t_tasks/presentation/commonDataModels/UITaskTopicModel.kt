package com.prod.t_tasks.presentation.commonDataModels

import com.prod.t_tasks.domain.model.TaskCategory

data class UITaskTopicModel(
	val id: Int, val name: String
)

fun TaskCategory.mapToUI(): UITaskTopicModel = UITaskTopicModel(id, name)