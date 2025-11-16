package com.prod.t_tasks.data.data_providers.local_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prod.t_tasks.data.base.BaseEntity
import com.prod.t_tasks.domain.model.Task
import com.prod.t_tasks.domain.model.trip.TripsTask

@Entity(tableName = "tasks")
data class TaskEntity(
	@PrimaryKey(autoGenerate = false)
	override val id: Long,
	val name: String,
	val whatToDo: String,
	val reason: String,
	val typeIds: String,
	val deadline: Long,
	val archived: Boolean,
	val common: Boolean,
	val tripId: Long
) : BaseEntity {
	fun mapToDomain(): Task = Task(
		id = id,
		name = name,
		typeIds = typeIds.split("|").mapNotNull { it.toIntOrNull() },
		deadline = deadline,
		archived = archived,
		common = common,
		tripId = tripId,
		whatNeedToDo = whatToDo,
		reason = reason
	)
}