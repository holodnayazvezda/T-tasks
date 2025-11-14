package com.prod.t_tasks.data.dto.nav_destinations

/**
 * Centralized list of application destinations.
 *
 * Keeping the routes in one place helps to avoid
 * string duplication and makes refactoring safer.
 */
sealed class AppDestination(val route: String) {
	data object Splash : AppDestination("app/splash")
	data object Login : AppDestination("app/login")
	data object Register : AppDestination("app/register")
	data object Dashboard : AppDestination("app/dashboard")
}
