package com.prod.t_tasks.domain.usecase.auth

import com.prod.t_tasks.data.repositories.AuthRepository
import org.koin.core.annotation.Single

@Single
class LogOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.deleteStoredUser()
    }
}