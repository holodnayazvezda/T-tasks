package com.prod.t_tasks.presentation.screens.register

import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.model.AuthPayload
import com.prod.t_tasks.domain.model.RegisterData
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.usecase.auth.RegisterUserUseCase
import com.prod.t_tasks.domain.usecase.auth.ValidateAuthFieldsUseCase
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class RegisterFormState(
	val firstName: String = "",
	val secondName: String = "",
	val email: String = "",
	val password: String = "",
	val confirmPassword: String = "",
	val errors: Map<AuthField, String> = emptyMap()
)

@KoinViewModel
class RegisterViewModel(
	private val registerUserUseCase: RegisterUserUseCase,
	private val validateAuthFieldsUseCase: ValidateAuthFieldsUseCase
) : BaseViewModel() {

	private val _formState = MutableStateFlow(RegisterFormState())
	val formState: StateFlow<RegisterFormState> = _formState

	private val _registerState = MutableUIStateFlow<AuthPayload>()
	val registerState: StateFlow<UIState<AuthPayload>> = _registerState

	fun onFirstNameChange(value: String) {
		_formState.update { it.copy(firstName = value, errors = it.errors - AuthField.FirstName) }
	}

	fun onSecondNameChange(value: String) {
		_formState.update { it.copy(secondName = value, errors = it.errors - AuthField.SecondName) }
	}

	fun onEmailChange(value: String) {
		_formState.update { it.copy(email = value, errors = it.errors - AuthField.Email) }
	}

	fun onPasswordChange(value: String) {
		_formState.update { it.copy(password = value, errors = it.errors - AuthField.Password) }
	}

	fun onConfirmPasswordChange(value: String) {
		_formState.update { it.copy(confirmPassword = value, errors = it.errors - AuthField.ConfirmPassword) }
	}

	fun submit() {
		viewModelScope.launch {
			val validation = validateAuthFieldsUseCase.validateRegister(
				firstName = _formState.value.firstName,
				secondName = _formState.value.secondName,
				email = _formState.value.email,
				password = _formState.value.password,
				confirmPassword = _formState.value.confirmPassword
			)

			if (!validation.isValid) {
				_formState.update { it.copy(errors = validation.errors) }
				return@launch
			}

			val result = registerUserUseCase(
				RegisterData(
					firstName = _formState.value.firstName,
					secondName = _formState.value.secondName,
					email = _formState.value.email,
					password = _formState.value.password
				)
			)
			result.collectRequest(_registerState)
		}
	}
}
