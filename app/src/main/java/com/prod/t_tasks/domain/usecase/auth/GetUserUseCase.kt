package com.prod.t_tasks.domain.usecase.auth

import com.prod.t_tasks.data.repositories.AuthRepository
import com.prod.t_tasks.domain.model.User
import org.koin.core.annotation.Single

@Single
class GetUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        authRepository.fetchProfile()
        return authRepository.getUser()
    }
}