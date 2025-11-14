package com.prod.t_tasks.presentation.main_activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.prod.t_tasks.data.dto.nav_destinations.AppDestination
import com.prod.t_tasks.domain.usecase.auth.CheckSessionUseCase
import com.prod.t_tasks.presentation.navigation.TTasksApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
	private val checkSessionUseCase: CheckSessionUseCase by inject()
	private val sessionDestinationState = MutableStateFlow<AppDestination?>(null)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		lifecycleScope.launch {
			val isAuthorized = try {
				checkSessionUseCase()
			} catch (e: Exception) {
				false
			}
			sessionDestinationState.value =
				if (isAuthorized) AppDestination.Dashboard else AppDestination.Login
		}
		setContent {
			val sessionDestination by sessionDestinationState.collectAsState()
			TTasksApp(sessionDestination = sessionDestination)
			LaunchedEffect(Unit) {
				requestPermissions(
					arrayOf(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY), 123
				)
				FirebaseMessaging.getInstance().token
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							val token = task.result
							Log.d("TOKEN", token)
						}
					}

				checkAndRequestNotificationPermission()
			}
		}
	}

	private fun checkAndRequestNotificationPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			when {
				ContextCompat.checkSelfPermission(
					this,
					android.Manifest.permission.POST_NOTIFICATIONS
				) == PackageManager.PERMISSION_GRANTED -> {
					// Разрешение уже есть, получаем токен
					getFCMToken()
				}

				else -> {
					// Запрашиваем разрешение
					requestPermissions(
						arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
						123
					)
				}
			}
		} else {
			// Для версий ниже Android 13 разрешение не требуется
			getFCMToken()
		}
	}

	private fun getFCMToken() {
		FirebaseMessaging.getInstance().token
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					val token = task.result
					Log.d("TOKEN", token)
				} else {
					Log.e("TOKEN", "Failed to get token", task.exception)
				}
			}
	}

	// Обработка результата запроса разрешений
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String>,
		grantResults: IntArray,
		deviceId: Int
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == 123) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				getFCMToken()
			}
		}
	}
}
