package com.prod.t_tasks.data.dto.trips

import com.prod.t_tasks.domain.model.trip.DestinationType
import com.prod.t_tasks.domain.model.trip.TravelType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTripDTO(
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("destination_type")
    val destinationType: String? = null,
    val city: String,
    val country: String? = null,
    val description: String? = null,
    val purpose: String,
    val transport: String,
    @SerialName("with_kids")
    val withKids: Boolean? = null,
    @SerialName("with_pets")
    val withPets: Boolean? = null,
    @SerialName("kids_ages")
    val kidsAges: List<Int>? = null,
    @SerialName("members_ids")
    val membersIds: List<Long>,
    @SerialName("auto_generate_tasks")
    val autoGenerateTasks: Boolean? = null
)