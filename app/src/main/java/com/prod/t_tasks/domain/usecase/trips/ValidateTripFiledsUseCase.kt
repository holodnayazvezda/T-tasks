package com.prod.t_tasks.domain.usecase.trips

import org.koin.core.annotation.Single

enum class CreateTripFields {
    DateStart,
    Country,
    City,
    Description,
    Goal,
    Transport,
    Members
}

data class TripValidationResult(
    val errors: Map<CreateTripFields, String> = emptyMap()
) {
    val isValid: Boolean
        get() = errors.isEmpty()
}


@Single
class ValidateTripFieldsUseCase {

    fun validate(
        dateStart: Long?,
        dateEnd: Long?,
        otherCountry: Boolean,
        country: String?,
        city: String?,
        description: String?,
        goal: String?,
        goalV: String?,
        transport: String?,
        transportV: String?,
        members: List<Pair<Long, String>>,
    ): TripValidationResult {

        val errors = buildMap<CreateTripFields, String> {

            if (dateStart == null)
                put(CreateTripFields.DateStart, "Выберите даты поездки")

            if (otherCountry && country.isNullOrEmpty()) {
                put(CreateTripFields.Country, "Укажите страну")
            }

            if (city.isNullOrEmpty())
                put(CreateTripFields.City, "Укажите город")

            if (description.isNullOrEmpty())
                put(CreateTripFields.Description, "Введите описание")

            val goalFinal = goalV?.takeIf { it.isNotBlank() } ?: goal
            if (goalFinal.isNullOrEmpty())
                put(CreateTripFields.Goal, "Укажите цель поездки")

            val transportFinal = transportV?.takeIf { it.isNotBlank() } ?: transport
            if (transportFinal.isNullOrEmpty())
                put(CreateTripFields.Transport, "Выберите транспорт")

            if (members.isEmpty())
                put(CreateTripFields.Members, "Добавьте хотя бы одного участника")
        }

        return TripValidationResult(errors)
    }
}
