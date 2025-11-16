package com.prod.t_tasks.domain.interfaces

import com.prod.t_tasks.data.dto.trips.CreateTripDTO
import com.prod.t_tasks.domain.model.trip.Trip
import kotlinx.coroutines.flow.Flow

interface TripsRepository {
	fun getAllTrips(): Flow<Result<List<Trip>>>
    suspend fun createTrip(
        createTripDTO: CreateTripDTO
    ): Result<Trip>
}