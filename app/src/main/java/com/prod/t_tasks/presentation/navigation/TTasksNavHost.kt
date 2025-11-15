package com.prod.t_tasks.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.presentation.screens.login.LoginScreen
import com.prod.t_tasks.presentation.screens.register.RegisterScreen
import com.prod.t_tasks.presentation.screens.DashboardScreen
import com.prod.t_tasks.presentation.screens.register.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TTasksNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	sessionDestination: AppDestination? = null
) {
    val startDestination = sessionDestination?.route ?: AppDestination.Login.route
    val registerViewModel: RegisterViewModel = koinViewModel()

	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier
	) {
		composable(AppDestination.Login.route) {
			LoginScreen(
				onRegisterClick = {
					navController.navigate(AppDestination.Register.route)
				},
				onSuccess = {
					navController.navigate(AppDestination.Dashboard.route) {
						popUpTo(AppDestination.Login.route) { inclusive = true }
					}
				}
			)
		}
		composable(AppDestination.Register.route) {
			RegisterScreen(
				onLoginClick = {
					navController.popBackStack()
				},
				onSuccess = {
					navController.navigate(AppDestination.Dashboard.route) {
						popUpTo(AppDestination.Register.route) { inclusive = true }
					}
				},
                viewModel = registerViewModel
			)
		}
		composable(AppDestination.Dashboard.route) {
			DashboardScreen()
		}
	}
}
