package com.prod.t_tasks.presentation.screens.profile

import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.model.UpdateUserData
import com.prod.t_tasks.domain.model.User
import com.prod.t_tasks.domain.usecase.auth.AuthField
import com.prod.t_tasks.domain.usecase.auth.GetUserUseCase
import com.prod.t_tasks.domain.usecase.auth.LogOutUseCase
import com.prod.t_tasks.domain.usecase.auth.UpdateUserUseCase
import com.prod.t_tasks.domain.usecase.auth.ValidateAuthFieldsUseCase
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class ProfileState(
    val firstName: String = "",
    val secondName: String = "",
    val errors: Map<AuthField, String> = emptyMap()
)

@KoinViewModel
class ProfileScreenViewModel(
    private val updateUserUsecase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val validateAuthFieldsUseCase: ValidateAuthFieldsUseCase
): BaseViewModel() {
    private var _state = MutableStateFlow(ProfileState())
    val state
        get() = _state
    private val _profileState = MutableUIStateFlow<User>()
    val profileState: StateFlow<UIState<User>>
        get() = _profileState

    fun onFirstNameChange(value: String) {
        _state.update { it.copy(firstName = value, errors = it.errors - AuthField.FirstName) }
    }

    fun onSecondNameChange(value: String) {
        _state.update { it.copy(secondName = value, errors = it.errors - AuthField.SecondName) }
    }

    fun submit(
        clearFocusFromAllOTF: () -> Unit
    ) {
        viewModelScope.launch {
            val validation = validateAuthFieldsUseCase.validateProfile(
                firstName = _state.value.firstName,
                secondName = _state.value.secondName
            )
            if (!validation.isValid) {
                _state.update { it.copy(errors = validation.errors) }
                return@launch
            }

            _profileState.emit(UIState.Loading())
            val result = updateUserUsecase(
                UpdateUserData(
                    firstName = _state.value.firstName,
                    secondName = _state.value.secondName
                )
            )
            result.collectRequest(_profileState)
            clearFocusFromAllOTF()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logOutUseCase()
        }
    }

    init {
        viewModelScope.launch {
            val user = getUserUseCase()
            if (user != null) {
                _state.update {
                    it.copy(
                        firstName = user.firstName,
                        secondName = user.secondName?: ""
                    )
                }
            }
        }
    }
}