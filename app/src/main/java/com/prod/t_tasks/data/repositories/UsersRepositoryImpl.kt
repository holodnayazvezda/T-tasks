package com.prod.t_tasks.data.repositories

import android.util.Log
import com.prod.t_tasks.data.base.BaseRepository
import com.prod.t_tasks.data.data_providers.api.ApiKtorClient
import com.prod.t_tasks.data.dto.users.UserDto
import com.prod.t_tasks.domain.interfaces.UsersRepository
import com.prod.t_tasks.domain.model.User
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.path
import org.koin.core.annotation.Single

@Single
class UsersRepositoryImpl(
	private val apiKtorClient: ApiKtorClient
) : UsersRepository, BaseRepository() {

	override val defaultKtorClient = apiKtorClient.client

	override suspend fun searchUserByEmail(email: String): Result<List<User>> {
		return networkRequest<List<UserDto>> {
			method = HttpMethod.Get
			url {
				path("api", "v1", "users")
				parameters.append("email_search_string", email)
			}
			contentType(ContentType.Application.Json)
		}.also {
			Log.d("UsersRepositoryImpl", it.toString())
		}.map { response ->
			response.map { it.mapToUser() }
		}
	}
}