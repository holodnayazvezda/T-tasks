package com.prod.t_tasks.presentation.screens.register

import TTTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.presentation.components.BigButton
import com.prod.t_tasks.presentation.components.TTPasswordField
import com.prod.t_tasks.presentation.utils.UIState
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: RegisterViewModel
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val coroutineScope = rememberCoroutineScope()
    val formState by viewModel.formState.collectAsState()
    val registerState by viewModel.registerState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorText = if (registerState is UIState.Error) {
        when (val error = (registerState as UIState.Error).error) {
            is NetworkError.Connection -> "Нет подключения к сети"
            is NetworkError.InputError -> error.error
            is NetworkError.Unexpected -> error.error
        }
    } else {
        ""
    }
    LaunchedEffect(registerState) {
        if (registerState is UIState.Success) {
            onSuccess()
        }
    }

    coroutineScope.launch {
        if (errorText.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = "Ошибка: $errorText",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .imePadding()
            .systemBarsPadding(),
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
        }
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(R.drawable.lottie),
                contentDescription = null,
                modifier = Modifier.width(130.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Давайте\nзнакомиться!",
                        style = typography.titleLarge,
                        fontSize = 31.sp
                    )
                    Image(
                        painter = painterResource(R.drawable.app_logo_dark_font),
                        contentDescription = null,
                        modifier = Modifier.size(140.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.height(20.dp))
                TTTextField(
                    value = formState.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    label = "Ваше имя",
                    error = formState.errors[AuthField.FirstName],
                )
                Spacer(Modifier.height(12.dp))
                TTTextField(
                    value = formState.secondName,
                    onValueChange = viewModel::onSecondNameChange,
                    label = "Ваша фамилия",
                    error = formState.errors[AuthField.SecondName]
                )
                Spacer(Modifier.height(12.dp))
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
                    error = formState.errors[AuthField.Password]
                )
                Spacer(Modifier.height(12.dp))
                TTPasswordField(
                    value = formState.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Повторите пароль",
                    error = formState.errors[AuthField.ConfirmPassword],
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onDone = viewModel::submit
                )
                Spacer(modifier = Modifier.height(20.dp))
                BigButton(
                    onClick = viewModel::submit,
                    modifier = Modifier.fillMaxWidth(),
                    buttonText = "Зарегистрироваться",
                    isLoading = registerState is UIState.Loading
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextButton(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Уже есть аккаунт?",
                        style = typography.labelMedium,
                        color = colorScheme.onBackground,
                        fontSize = 24.sp
                    )
                }
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}
