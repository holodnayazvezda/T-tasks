package com.prod.t_tasks.domain.interfaces

import com.prod.t_tasks.domain.model.User

interface UsersRepository {

	suspend fun searchUserByEmail(email: String): Result<List<User>>

}