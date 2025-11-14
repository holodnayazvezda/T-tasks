package com.prod.t_tasks.domain.usecase.auth

import com.prod.t_tasks.data.dto.auth.UserLoginRequest
import com.prod.t_tasks.data.repositories.AuthRepository
import com.prod.t_tasks.domain.model.AuthPayload
import com.prod.t_tasks.domain.model.LoginData
import org.koin.core.annotation.Single

@Single
class LoginUserUseCase(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(data: LoginData): Result<AuthPayload> {
		val request = UserLoginRequest(
			email = data.email,
			password = data.password
		)
		return authRepository.login(request)
	}
}
