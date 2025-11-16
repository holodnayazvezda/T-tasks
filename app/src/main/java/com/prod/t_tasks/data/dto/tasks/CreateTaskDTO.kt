package com.prod.t_tasks.data.dto.tasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskDTO(
    val title: String,
    val description: String,
    @SerialName("due_time")
    val dueTime: String,
    val status: String,
    @SerialName("categories_ids")
    val categoriesIds: List<Int>,
    @SerialName("is_common")
    val isCommon: Boolean
)
