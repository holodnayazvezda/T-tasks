package com.prod.t_tasks.data.data_providers.local_db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.prod.t_tasks.data.data_providers.local_db.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

	@Query("DELETE FROM tasks WHERE deadline >= :startDate AND deadline <= :endDate " +
			"AND ((not :withoutCommon) OR (not common))" +
			" AND ((not archived) OR :showFinished) AND typeIds LIKE :categoryFilter")
	suspend fun clearByFilters(
		startDate: Long,
		endDate: Long,
		withoutCommon: Boolean,
		showFinished: Boolean,
		categoryFilter: String
	)

	@Query("SELECT * FROM tasks WHERE deadline >= :startDate AND deadline <= :endDate " +
			"AND ((not :withoutCommon) OR (not common))" +
			" AND ((not archived) OR :showFinished) AND typeIds LIKE :categoryFilter")
	fun getByFilters(
		startDate: Long,
		endDate: Long,
		withoutCommon: Boolean,
		showFinished: Boolean,
		categoryFilter: String
	): PagingSource<Int, TaskEntity>


	@Upsert
	suspend fun upsert(
		data: List<TaskEntity>
	)

	@Query("UPDATE tasks SET archived = :state WHERE id = :id")
	suspend fun setTaskArchivedState(id: Long, state: Boolean)

	@Query("UPDATE tasks SET deadline = :deadline WHERE id = :id")
	suspend fun setTaskDeadline(id: Long, deadline: Long)
}