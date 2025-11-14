package com.prod.t_tasks.data.data_providers.local_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prod.t_tasks.data.data_providers.local_db.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

	@Query("SELECT * FROM users LIMIT 1")
	fun observeUser(): Flow<UserEntity?>

	@Query("SELECT * FROM users LIMIT 1")
	suspend fun getUser(): UserEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(user: UserEntity)

	@Query("DELETE FROM users")
	suspend fun clear()
}
