package com.prod.t_tasks.data.data_providers.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prod.t_tasks.data.data_providers.local_db.dao.TaskCategoriesDao
import com.prod.t_tasks.data.data_providers.local_db.dao.TasksDao
import com.prod.t_tasks.data.data_providers.local_db.dao.TripsDao
import com.prod.t_tasks.data.data_providers.local_db.dao.UserDao
import com.prod.t_tasks.data.data_providers.local_db.entities.TaskCategoryEntity
import com.prod.t_tasks.data.data_providers.local_db.entities.TaskEntity
import com.prod.t_tasks.data.data_providers.local_db.entities.TripEntity
import com.prod.t_tasks.data.data_providers.local_db.entities.UserEntity

@Database(
	entities = [UserEntity::class, TaskCategoryEntity::class, TripEntity::class, TaskEntity::class],
	version = 4,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao

	abstract fun taskCategoriesDao(): TaskCategoriesDao

	abstract fun tripsDao(): TripsDao
	abstract fun tasksDao(): TasksDao
}
