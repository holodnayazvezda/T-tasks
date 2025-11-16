package com.prod.t_tasks.domain.usecase.auth

import android.util.Patterns
import org.koin.core.annotation.Single

enum class AuthField {
	FirstName,
	SecondName,
	Email,
	Password,
	ConfirmPassword
}



data class ValidationResult(
	val errors: Map<AuthField, String> = emptyMap()
) {
	val isValid: Boolean
		get() = errors.isEmpty()
}

@Single
class ValidateAuthFieldsUseCase {

    fun validateRegister(
        firstName: String,
        secondName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        val errors = buildMap {
            if (firstName.isBlank()) put(AuthField.FirstName, "Введите имя")
            if (secondName.isBlank()) put(AuthField.SecondName, "Введите фамилию")
            if (confirmPassword.isBlank()) put(AuthField.ConfirmPassword, "Повторите пароль")
            if (!isEmailValid(email)) put(AuthField.Email, "Некорректный email")

            validatePassword(password)?.let { put(AuthField.Password, it) }

            if (password != confirmPassword) {
                put(AuthField.ConfirmPassword, "Пароли не совпадают")
            }
        }
        return ValidationResult(errors)
    }

    fun validatePassword(password: String): String? {
        if (password.length < 8) {
            return "Пароль должен быть не менее 8 символов"
        }
        if (!password.any { it.isUpperCase() }) {
            return "Пароль должен содержать хотя бы одну заглавную букву"
        }
        if (!password.any { it.isDigit() }) {
            return "Пароль должен содержать хотя бы одну цифру"
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            return "Пароль должен содержать хотя бы один специальный символ"
        }
        return null // пароль валидный
    }


    fun validateLogin(
		email: String,
		password: String
	): ValidationResult {
		val errors = buildMap {
			if (!isEmailValid(email)) put(AuthField.Email, "Некорректный email")
			validatePassword(password)?.let { put(AuthField.Password, it) }
		}
		return ValidationResult(errors)
	}

    fun validateProfile(
        firstName: String,
        secondName: String,
    ): ValidationResult {
        val errors = buildMap {
            if (firstName.isBlank()) put(AuthField.FirstName, "Введите имя")
            if (secondName.isBlank()) put(AuthField.SecondName, "Введите фамилию")
        }
        return ValidationResult(errors)
    }

	private fun isEmailValid(email: String): Boolean =
		email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
