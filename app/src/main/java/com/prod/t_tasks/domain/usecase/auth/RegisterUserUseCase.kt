package com.prod.t_tasks.domain.usecase.auth

import com.prod.t_tasks.data.dto.auth.UserCreateRequest
import com.prod.t_tasks.data.repositories.AuthRepository
import com.prod.t_tasks.domain.model.AuthPayload
import com.prod.t_tasks.domain.model.RegisterData
import org.koin.core.annotation.Single

@Single
class RegisterUserUseCase(
	private val authRepository: AuthRepository
) {
	suspend operator fun invoke(data: RegisterData): Result<AuthPayload> {
		val request = UserCreateRequest(
			firstName = data.firstName,
			secondName = data.secondName,
			email = data.email,
			password = data.password,
			avatarFileKey = data.avatarFileKey,
			fsmToken = data.fsmToken
		)
		return authRepository.register(request)
	}
}
