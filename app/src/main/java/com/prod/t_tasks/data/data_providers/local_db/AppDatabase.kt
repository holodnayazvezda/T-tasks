package com.prod.t_tasks.data.data_providers.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prod.t_tasks.data.data_providers.local_db.dao.UserDao
import com.prod.t_tasks.data.data_providers.local_db.entities.UserEntity

@Database(
	entities = [UserEntity::class],
	version = 1,
	exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
	abstract fun userDao(): UserDao
}
