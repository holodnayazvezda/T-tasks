package com.prod.t_tasks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.prod.t_tasks.presentation.main_activity.MainActivity

class FirebaseMessagingService : FirebaseMessagingService() {

	override fun onCreate() {
		super.onCreate()
		createNotificationChannel()
	}

	override fun onMessageReceived(message: RemoteMessage) {

		val title = message.data["title"]
		val text = message.data["body"]
		Log.e(
			"fcm",
			"title=$title" +
					"\ntext=$text"
		)

		title?.let { it1 -> text?.let { it2 -> sendNotification(it1, it2) } }
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		Log.e("fcm", "token=$token")
	}

	private fun createNotificationChannel() {
		val channel = NotificationChannel(
			"default_channel_id",
			"MainAppNotifications",
			NotificationManager.IMPORTANCE_HIGH
		).apply {
			description = "Channel for main motifications"
			enableLights(true)
			lightColor = Color.Red.toArgb()
			enableVibration(true)
			vibrationPattern = longArrayOf(0, 500, 200, 500)
			lockscreenVisibility = Notification.VISIBILITY_PUBLIC
		}

		val notificationManager =
			getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
	}

	private fun sendNotification(title: String?, messageBody: String?) {
		val intent = Intent(this, MainActivity::class.java).apply {
			addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		}

		val pendingIntent = PendingIntent.getActivity(
			this, 0, intent,
			PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
		)

		val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
			.setSmallIcon(android.R.drawable.ic_dialog_info) // замените на свою иконку
			.setContentTitle(title ?: "Уведомление")
			.setContentText(messageBody)
			.setAutoCancel(true)
			.setContentIntent(pendingIntent)

		val notificationManager =
			getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.notify(0, notificationBuilder.build())
	}
}