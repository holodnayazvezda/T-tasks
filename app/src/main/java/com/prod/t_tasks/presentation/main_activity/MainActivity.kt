package com.prod.t_tasks.presentation.main_activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.navigation.TTasksApp

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		val splashScreen = installSplashScreen()

		splashScreen.setKeepOnScreenCondition { true }
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			TTasksApp()
			LaunchedEffect(Unit) {
				requestPermissions(
					arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 123
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
