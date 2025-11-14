package com.startup.suprisa.presentation.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
	val stream = ByteArrayOutputStream() // Create a ByteArrayOutputStream
	compress(
		Bitmap.CompressFormat.JPEG,
		100,
		stream
	) // Compress Bitmap to PNG with 100% quality
	return stream.toByteArray() // Convert stream to byte array
}