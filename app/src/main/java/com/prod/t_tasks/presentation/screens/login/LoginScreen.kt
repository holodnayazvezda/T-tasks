package com.prod.t_tasks.presentation.screens.login

import TTTextField
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.presentation.components.standart.BigButton
import com.prod.t_tasks.presentation.components.standart.TTPasswordField
import com.prod.t_tasks.presentation.utils.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onRegisterClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

	val formState by viewModel.formState.collectAsState()
	val authState by viewModel.authState.collectAsState()
    val errorText = if (authState is UIState.Error) {
        when (val error = (authState as UIState.Error).error) {
            is NetworkError.Connection -> "Нет подключения к сети"
            is NetworkError.InputError -> error.error
            is NetworkError.Unexpected -> error.error
        }
    } else {
        ""
    }
	LaunchedEffect(authState) {
		if (authState is UIState.Success) {
			onSuccess()
		}
	}

    LaunchedEffect(errorText) {
        if (errorText.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = "Ошибка: $errorText",
                duration = SnackbarDuration.Short
            )
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding(),
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = painterResource(R.drawable.lottie),
            contentDescription = null,
            modifier = Modifier.width(130.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo_dark_font),
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Вход",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            TTTextField(
                value = formState.email,
                onValueChange = viewModel::onEmailChange,
                label = "Ваш email",
                error = formState.errors[AuthField.Email]
            )
            Spacer(Modifier.height(12.dp))
            TTPasswordField(
                value = formState.password,
                onValueChange = viewModel::onPasswordChange,
                label = "Пароль",
                error = formState.errors[AuthField.Password],
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                onDone = viewModel::submit
            )
            Spacer(modifier = Modifier.height(40.dp))
            BigButton(
                onClick = viewModel::submit,
                modifier = Modifier.fillMaxWidth(),
                buttonText = "Войти",
                isLoading = authState is UIState.Loading
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Зарегистрироваться",
                    style = typography.labelMedium,
                    color = colorScheme.onBackground,
                    fontSize = 24.sp
                )
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}
