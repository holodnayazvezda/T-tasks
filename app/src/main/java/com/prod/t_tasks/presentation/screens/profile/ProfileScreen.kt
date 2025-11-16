package com.prod.t_tasks.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.presentation.components.standart.BigButton
import com.prod.t_tasks.presentation.components.standart.TTNamedTextField
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.utils.UIState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    navigateToLoginScreen: () -> Unit,
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val focusManager = LocalFocusManager.current
    val state by viewModel.state.collectAsState()
    val profileState by viewModel.profileState.collectAsState()
    val errorText = if (profileState is UIState.Error) {
        when (val error = (profileState as UIState.Error).error) {
            is NetworkError.Connection -> "Нет подключения к сети"
            is NetworkError.InputError -> error.error
            is NetworkError.Unexpected -> error.error
        }
    } else {
        ""
    }
    LaunchedEffect(errorText) {
        if (errorText.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = "Ошибка: $errorText",
                duration = SnackbarDuration.Short
            )
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
            .padding(top = 80.dp)
            .imePadding()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.app_logo_dark_font),
                contentDescription = "App logo"
            )

            Spacer(modifier = Modifier.width(Paddings.medium))
            Text(
                text = "Это вы, и вы\nпрекрасны!",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 31.sp
            )
        }
        Spacer(Modifier.height(30.dp))
        TTNamedTextField(
            name = "Имя",
            value = state.firstName,
            onValueChange = viewModel::onFirstNameChange,
            error = state.errors[AuthField.FirstName],
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )
        Spacer(Modifier.padding(7.dp))
        TTNamedTextField(
            name = "Фамилия",
            value = state.secondName,
            onValueChange = viewModel::onSecondNameChange,
            error = state.errors[AuthField.SecondName],
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )
        Spacer(Modifier.height(30.dp))
        BigButton(
            onClick = {
                viewModel.submit({ focusManager.clearFocus() })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            buttonText = "Сохранить",
            isLoading = profileState is UIState.Loading
        )
        Spacer(Modifier.height(15.dp))
        Button(
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                viewModel.logout()
                navigateToLoginScreen()
            },
            colors = ButtonColors(
                containerColor = colorScheme.errorContainer,
                contentColor = colorScheme.onErrorContainer,
                disabledContainerColor = colorScheme.errorContainer,
                disabledContentColor = colorScheme.onErrorContainer
            )
        ){
            Row {
                Text(
                    text = "Выйти из аккаунта",
                    style = typography.titleMedium,
                    fontSize = 24.sp
                )
                Spacer(Modifier.width(3.dp))
                Icon(
                    painter = painterResource(R.drawable.logout_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}