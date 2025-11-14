package com.prod.t_tasks.data.data_providers.local_db

import android.content.Context
import androidx.room.Room
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseProvider {

	@Single
	fun provideDatabase(context: Context): AppDatabase =
		Room.databaseBuilder(
			context,
			AppDatabase::class.java,
			"t_tasks.db"
		).fallbackToDestructiveMigration()
			.build()
}
