package com.prod.t_tasks.data.data_providers.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prod.t_tasks.data.data_providers.local_db.dao.TaskCategoriesDao
import com.prod.t_tasks.data.data_providers.local_db.dao.UserDao
import com.prod.t_tasks.data.data_providers.local_db.entities.TaskCategoryEntity
import com.prod.t_tasks.data.data_providers.local_db.entities.UserEntity

@Database(
	entities = [UserEntity::class, TaskCategoryEntity::class],
	version = 2,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao

	abstract fun taskCategoriesDao(): TaskCategoriesDao
}
