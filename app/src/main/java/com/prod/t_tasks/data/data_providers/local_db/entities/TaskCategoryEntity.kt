package com.prod.t_tasks.data.data_providers.local_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prod.t_tasks.domain.model.TaskCategory

@Entity(tableName = "task_categories")
data class TaskCategoryEntity(
	@PrimaryKey(autoGenerate = false)
	val id: Int,
	val name: String,
	val lastModify: Long = System.currentTimeMillis()
){
	fun mapToDomain(): TaskCategory = TaskCategory(
		id = id,
		name = name
	)
}

fun TaskCategory.mapToDB(): TaskCategoryEntity = TaskCategoryEntity(id, name)