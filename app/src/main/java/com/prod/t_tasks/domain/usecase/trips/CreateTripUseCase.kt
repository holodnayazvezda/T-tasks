package com.prod.t_tasks.domain.usecase.trips

import com.prod.t_tasks.data.dto.trips.CreateTripDTO
import com.prod.t_tasks.domain.interfaces.TripsRepository
import com.prod.t_tasks.domain.model.trip.Trip
import com.prod.t_tasks.domain.model.trip.TripCreate
import com.prod.t_tasks.presentation.utils.timestampToIso
import org.koin.core.annotation.Single

@Single
class CreateTripUseCase(
    private val repository: TripsRepository
) {
    suspend operator fun invoke(tripCreate: TripCreate): Result<Trip> {
        return repository.createTrip(
            CreateTripDTO(
                startDate = timestampToIso(tripCreate.startDate),
                endDate = timestampToIso(tripCreate.endDate),
                destinationType = tripCreate.destinationType,
                city = tripCreate.city,
                country = tripCreate.country,
                description = tripCreate.description,
                purpose = tripCreate.purpose,
                transport = tripCreate.transport,
                withKids = tripCreate.withKids,
                withPets = tripCreate.withPets,
                kidsAges = tripCreate.kidsAges,
                membersIds = tripCreate.membersIds,
                autoGenerateTasks = tripCreate.autoGenerateTasks
            )
        )
    }
}