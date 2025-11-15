package com.prod.t_tasks.data.dto.nav_destinations

import kotlinx.serialization.Serializable

/**
 * Centralized list of application destinations.
 *
 * Keeping the routes in one place helps to avoid
 * string duplication and makes refactoring safer.
 */
sealed class AppDestination(val route: String) {
	data object Login : AppDestination("app/login")
	data object Register : AppDestination("app/register")
	data object Dashboard : AppDestination("app/dashboard")

	data object Main : AppDestination("app/main")

	data object TripList: AppDestination("app/trip/list")

	data object Profile: AppDestination("app/profile")

	data object CreateTrip: AppDestination("app/trip/create")
}
