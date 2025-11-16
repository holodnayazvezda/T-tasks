package com.prod.t_tasks.domain.usecase.tasks

import org.koin.core.annotation.Single

enum class CreateTaskFields {
    Trip,
    Name,
    WhatNeedToDo,
    Reason,
    Deadline,
    Categories
}

data class ValidationResult(
    val errors: Map<CreateTaskFields, String> = emptyMap()
) {
    val isValid: Boolean
        get() = errors.isEmpty()
}


@Single
class ValidateTaskFieldsUseCase {
    fun validate(
        tripId: Long?,
        taskName: String = "",
        whatNeedToDo: String = "",
        reason: String = "",
        deadline: Long? = null,
        selectedCategories: List<Int>
    ): ValidationResult {
        val errors = buildMap {
            if (tripId == null) put(CreateTaskFields.Trip, "Выберите поездку")
            if (taskName.isEmpty()) put(CreateTaskFields.Name, "Введите имя")
            if (whatNeedToDo.isEmpty()) put(CreateTaskFields.WhatNeedToDo, "Введите описание задачи")
            if (reason.isEmpty()) put(CreateTaskFields.Reason, "Введите причину")
            if (deadline == null) put(CreateTaskFields.Deadline, "Выберите дату дедлайна")
            if (selectedCategories.isEmpty()) put(CreateTaskFields.Categories, "Укажите хотя бы одну категорию")
        }
        return ValidationResult(errors)
    }
}