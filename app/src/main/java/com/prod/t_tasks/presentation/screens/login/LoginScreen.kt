package com.prod.t_tasks.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.presentation.components.TTPasswordField
import com.prod.t_tasks.presentation.components.TTTextField
import com.prod.t_tasks.presentation.utils.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
	val formState by viewModel.formState.collectAsState()
	val authState by viewModel.authState.collectAsState()

	LaunchedEffect(authState) {
		if (authState is UIState.Success) {
			onSuccess()
		}
	}

	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = "Вход",
			style = MaterialTheme.typography.headlineMedium
		)
		Spacer(modifier = Modifier.height(24.dp))

		TTTextField(
			value = formState.email,
			onValueChange = viewModel::onEmailChange,
			label = "Email",
			modifier = Modifier.fillMaxWidth(),
			error = formState.errors[AuthField.Email],
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
		)
		Spacer(modifier = Modifier.height(12.dp))
		TTPasswordField(
			value = formState.password,
			onValueChange = viewModel::onPasswordChange,
			label = "Пароль",
			error = formState.errors[AuthField.Password]
		)
		Spacer(modifier = Modifier.height(24.dp))
		Button(
			onClick = viewModel::submit,
			modifier = Modifier.fillMaxWidth(),
			enabled = authState !is UIState.Loading
		) {
			Text(text = "Войти")
		}

		if (authState is UIState.Loading) {
			Spacer(modifier = Modifier.height(16.dp))
			CircularProgressIndicator()
		}

		if (authState is UIState.Error) {
			Spacer(modifier = Modifier.height(8.dp))
			val errorText = when (val error = (authState as UIState.Error).error) {
				is NetworkError.Connection -> "Нет подключения к сети"
				is NetworkError.InputError -> error.error
				is NetworkError.Unexpected -> error.error
			}
			Text(
				text = "Ошибка: $errorText",
				color = MaterialTheme.colorScheme.error,
				textAlign = TextAlign.Center
			)
		}

		Spacer(modifier = Modifier.height(16.dp))
		TextButton(
			onClick = onRegisterClick,
			modifier = Modifier.fillMaxWidth()
		) {
			Text(text = "Зарегистрироваться")
		}
	}
}
