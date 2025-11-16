package com.prod.t_tasks.domain.usecase.trips

import com.prod.t_tasks.domain.interfaces.TripsRepository
import com.prod.t_tasks.domain.model.trip.Trip
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class GetAllUserTripsUseCase (private val tripsRepository: TripsRepository) {
	operator fun invoke(): Flow<Result<List<Trip>>> = tripsRepository.getAllTrips()
}
