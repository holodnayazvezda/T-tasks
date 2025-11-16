package com.prod.t_tasks.domain.usecase.auth

import com.prod.t_tasks.data.dto.auth.UpdateUserRequest
import com.prod.t_tasks.data.repositories.AuthRepository
import com.prod.t_tasks.domain.model.UpdateUserData
import com.prod.t_tasks.domain.model.User
import org.koin.core.annotation.Single

@Single
class UpdateUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(data: UpdateUserData): Result<User> {
        val request = UpdateUserRequest(
            firstName = data.firstName,
            secondName = data.secondName
        )
        val updatedUser = authRepository.updateProfile(request)
        return updatedUser
    }
}