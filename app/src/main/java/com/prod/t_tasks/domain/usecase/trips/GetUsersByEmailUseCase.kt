package com.prod.t_tasks.domain.usecase.trips

import com.prod.t_tasks.domain.interfaces.UsersRepository
import com.prod.t_tasks.domain.model.User
import org.koin.core.annotation.Single

@Single
class GetUsersByEmailUseCase(private val usersRepository: UsersRepository) {
	suspend operator fun invoke(email: String): Result<List<User>> {
		return usersRepository.searchUserByEmail(email = email)
	}
}
