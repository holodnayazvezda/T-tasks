package com.prod.t_tasks.presentation.components.standart

import TTTextField
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TTNamedTextField(
    name: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    onDone: (() -> Unit)? = null
) {
    Column {
        Text(
            text = name,
            style = typography.labelLarge,
            fontSize = 14.sp,
            color = Color.White
        )
        Spacer(Modifier.height(5.dp))
        TTTextField(
            value = value,
            onValueChange = onValueChange,
            error = error,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            onDone = onDone
        )
    }
}