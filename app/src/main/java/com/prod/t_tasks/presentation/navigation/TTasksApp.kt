package com.prod.t_tasks.presentation.navigation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.prod.t_tasks.R
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.presentation.components.TBottomNavigation
import com.prod.t_tasks.presentation.theme.TemplateProdChebTheme

@Composable
fun TTasksApp(
	appState: TTasksAppState = rememberTTasksAppState(),
	context: Context,
	sessionDestination: AppDestination? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

	TemplateProdChebTheme {
		val snackbarHostState = remember { SnackbarHostState() }

		val bottomBarState = remember { mutableStateOf<Int?>(null) }

		when (appState.navController.currentBackStackEntryAsState().value?.destination?.route) {
			AppDestination.Login.route -> {
				bottomBarState.value = null
			}

			AppDestination.Register.route -> {
				bottomBarState.value = null
			}

			AppDestination.Dashboard.route -> {
				bottomBarState.value = null
			}

			AppDestination.TripList.route -> {
				bottomBarState.value = 0
			}

			AppDestination.Main.route -> {
				bottomBarState.value = 1
			}

			AppDestination.Profile.route -> {
				bottomBarState.value = 2
			}
		}
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			snackbarHost = {
				SnackbarHost(
					hostState = snackbarHostState,
					snackbar = { data ->
						Snackbar(
							snackbarData = data,
							containerColor = MaterialTheme.colorScheme.errorContainer,
							contentColor = MaterialTheme.colorScheme.onErrorContainer,
							shape = MaterialTheme.shapes.medium
						)
					}
				)
			},
			bottomBar = {
				bottomBarState.value?.let { bbState ->
					TBottomNavigation(
						modifier = Modifier
							.background(MaterialTheme.colorScheme.surfaceContainer)
							.windowInsetsPadding(WindowInsets.navigationBars),
						selectedPage = bbState
					) { newPage ->
						when (newPage) {
							0 -> {
								appState.navController.navigate(AppDestination.TripList.route)

							}

							1 -> {
								appState.navController.navigate(AppDestination.Main.route)
							}

							2 -> {
								appState.navController.navigate(AppDestination.Profile.route)
							}
						}
					}
				}
			},
            floatingActionButton = {
                when (appState.navController.currentBackStackEntryAsState().value?.destination?.route) {
                    AppDestination.Main.route -> {
                        ExtendedFloatingActionButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            containerColor = colorScheme.tertiaryContainer,
                            contentColor = colorScheme.onTertiaryContainer
                        ) {
                            Row {
                                Text(
                                    text = "Добавить задачу",
                                    style = typography.titleMedium,
                                    fontSize = 16.sp
                                )
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    painter = painterResource(R.drawable.add_square_outline),
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center
		) { padding ->
			TTasksNavHost(
				navController = appState.navController,
				modifier = Modifier.padding(padding),
				sessionDestination = sessionDestination,
				snackbarHostState = snackbarHostState,
				context = context
			)
		}
	}
}
