package com.prod.t_tasks.presentation.navigation

import android.content.Context
import androidx.appcompat.R
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.presentation.screens.DashboardScreen
import com.prod.t_tasks.presentation.screens.createTrip.CreateTripScreen
import com.prod.t_tasks.presentation.screens.login.LoginScreen
import com.prod.t_tasks.presentation.screens.main.MainScreen
import com.prod.t_tasks.presentation.screens.profile.ProfileScreen
import com.prod.t_tasks.presentation.screens.register.RegisterScreen
import com.prod.t_tasks.presentation.screens.register.RegisterViewModel
import com.prod.t_tasks.presentation.screens.tripList.TripListScreen
import com.prod.t_tasks.presentation.utils.ErrorCallbacks
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TTasksNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	sessionDestination: AppDestination? = null,
	context: Context,
	snackbarHostState: SnackbarHostState
) {
	val startDestination = sessionDestination?.route ?: AppDestination.Login.route
	val registerViewModel: RegisterViewModel = koinViewModel()

	ErrorCollectorScope(context, navController, object : ErrorCallbacks {
		override fun processConnectionError(networkError: NetworkError.Connection) {

		}

		override fun processUnexpectedError(networkError: NetworkError.Unexpected) {

		}

	}) {
		NavHost(
			navController = navController,
			startDestination = startDestination,
			modifier = modifier
		) {
			composable(AppDestination.Login.route) {
				LoginScreen(
					snackbarHostState = snackbarHostState,
					onRegisterClick = {
						navController.navigate(AppDestination.Register.route)
					},
					onSuccess = {
						navController.navigate(AppDestination.Main.route) {
							popUpTo(AppDestination.Login.route) { inclusive = true }
						}
					}
				)
			}

			composable(AppDestination.Register.route) {
				RegisterScreen(
					snackbarHostState = snackbarHostState,
					onLoginClick = {
						navController.popBackStack()
					},
					onSuccess = {
						navController.navigate(AppDestination.Main.route) {
							popUpTo(AppDestination.Register.route) { inclusive = true }
						}
					},
					viewModel = registerViewModel
				)
			}

			composable(AppDestination.Main.route) {
				MainScreen()
			}

			composable(AppDestination.TripList.route) {
				TripListScreen(navigateToCreateTrip = {
					navController.navigate(AppDestination.CreateTrip.route)
				})
			}

			composable(AppDestination.Profile.route) {
				ProfileScreen()
			}

			composable(AppDestination.Dashboard.route) {
				DashboardScreen()
			}

			composable(AppDestination.CreateTrip.route) {
				CreateTripScreen()
			}
		}

	}
}
