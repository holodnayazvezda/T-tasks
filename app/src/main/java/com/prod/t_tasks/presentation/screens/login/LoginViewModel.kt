package com.prod.t_tasks.presentation.screens.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.model.AuthPayload
import com.prod.t_tasks.domain.model.LoginData
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.usecase.auth.LoginUserUseCase
import com.prod.t_tasks.domain.usecase.auth.ValidateAuthFieldsUseCase
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class LoginFormState(
	val email: String = "",
	val password: String = "",
	val errors: Map<AuthField, String> = emptyMap()
)

@KoinViewModel
class LoginViewModel(
	private val loginUserUseCase: LoginUserUseCase,
	private val validateAuthFieldsUseCase: ValidateAuthFieldsUseCase
) : BaseViewModel() {

	private val _formState = MutableStateFlow(LoginFormState())
	val formState: StateFlow<LoginFormState> = _formState

	private val _authState = MutableUIStateFlow<AuthPayload>()
	val authState: StateFlow<UIState<AuthPayload>> = _authState

	fun onEmailChange(value: String) {
		_formState.update { it.copy(email = value, errors = it.errors - AuthField.Email) }
	}

	fun onPasswordChange(value: String) {
		_formState.update { it.copy(password = value, errors = it.errors - AuthField.Password) }
	}

	fun submit() {
		viewModelScope.launch {
			val validation = validateAuthFieldsUseCase.validateLogin(
				email = _formState.value.email,
				password = _formState.value.password
			)
			if (!validation.isValid) {
				_formState.update { it.copy(errors = validation.errors) }
				return@launch
			}

            _authState.emit(UIState.Loading())

			val result = loginUserUseCase(
				LoginData(
					email = _formState.value.email,
					password = _formState.value.password
				)
			)
			result.collectRequest(_authState)
		}
	}
}
