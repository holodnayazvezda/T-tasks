package com.prod.t_tasks.data.data_providers.local_db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.prod.t_tasks.data.data_providers.local_db.entities.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripsDao {

	@Query("SELECT * FROM trips")
	fun getAll(): Flow<List<TripEntity>>

	@Upsert
	suspend fun upsert(data: List<TripEntity>)

	@Query("DELETE FROM trips WHERE updatedAt < :timeBreak")
	suspend fun clearCache(timeBreak: Long)

	@Query("DELETE FROM TRIPS WHERE id = :id")
	suspend fun deleteTrip(id: Long)
}