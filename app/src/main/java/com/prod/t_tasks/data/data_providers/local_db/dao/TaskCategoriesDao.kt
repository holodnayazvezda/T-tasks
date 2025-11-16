package com.prod.t_tasks.data.data_providers.local_db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.prod.t_tasks.data.data_providers.local_db.entities.TaskCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskCategoriesDao {

	@Query("SELECT * FROM task_categories")
	fun getAll(): Flow<List<TaskCategoryEntity>>

	@Upsert
	suspend fun upsert(data: List<TaskCategoryEntity>)

	@Query("DELETE FROM task_categories WHERE lastModify < :timeBreak")
	suspend fun clearCache(timeBreak: Long)
}