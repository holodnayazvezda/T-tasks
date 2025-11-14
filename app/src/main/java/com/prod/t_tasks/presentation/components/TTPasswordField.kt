package com.prod.t_tasks.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TTPasswordField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	modifier: Modifier = Modifier,
	error: String? = null
) {
	var isVisible by remember { mutableStateOf(false) }

	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		label = { Text(label) },
		modifier = modifier,
		isError = error != null,
		supportingText = {
			if (error != null) {
				Text(text = error)
			}
		},
		keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
		visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
		singleLine = true,
		trailingIcon = {
			val icon = if (isVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
			IconButton(onClick = { isVisible = !isVisible }) {
				Icon(imageVector = icon, contentDescription = null)
			}
		}
	)
}
