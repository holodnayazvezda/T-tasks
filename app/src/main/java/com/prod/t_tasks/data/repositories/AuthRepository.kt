package com.prod.t_tasks.data.repositories

import androidx.room.withTransaction
import com.prod.t_tasks.data.base.BaseRepository
import com.prod.t_tasks.data.data_providers.api.ApiKtorClient
import com.prod.t_tasks.data.data_providers.localInfo.AuthorizationDataStore
import com.prod.t_tasks.data.data_providers.local_db.AppDatabase
import com.prod.t_tasks.data.data_providers.local_db.entities.UserEntity
import com.prod.t_tasks.data.dto.auth.TokenResponse
import com.prod.t_tasks.data.dto.auth.UserCreateRequest
import com.prod.t_tasks.data.dto.auth.UserLoginRequest
import com.prod.t_tasks.data.dto.auth.UserRegisterResponse
import com.prod.t_tasks.data.dto.auth.UserResponse
import com.prod.t_tasks.domain.model.AuthPayload
import com.prod.t_tasks.domain.model.AuthTokens
import com.prod.t_tasks.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.path
import io.ktor.client.request.setBody
import io.ktor.http.contentType

@Single
class AuthRepository(
	private val apiKtorClient: ApiKtorClient,
	private val authorizationDataStore: AuthorizationDataStore,
	private val appDatabase: AppDatabase
) : BaseRepository() {

	private val userDao = appDatabase.userDao()

	override val defaultKtorClient = apiKtorClient.client
	override val db = appDatabase

	fun observeUser(): Flow<User?> = userDao.observeUser().map { entity ->
		entity?.toDomain()
	}

	suspend fun register(request: UserCreateRequest): Result<AuthPayload> {
		return networkRequest<UserRegisterResponse> {
			method = HttpMethod.Post
			url {
				path("api", "v1", "users", "auth", "register")
			}
			contentType(ContentType.Application.Json)
			setBody(request)
		}.onSuccess { response ->
			saveTokens(response.tokens)
			storeUser(response.user)
		}.map { response ->
			AuthPayload(
				user = response.user.toDomain(),
				tokens = response.tokens.toDomain()
			)
		}
	}

	suspend fun login(request: UserLoginRequest): Result<AuthPayload> {
		return networkRequest<TokenResponse> {
			method = HttpMethod.Post
			url {
				path("api", "v1", "users", "auth", "login")
			}
			contentType(ContentType.Application.Json)
			setBody(request)
		}.onSuccess { tokens ->
			saveTokens(tokens)
		}.flatMap { tokens ->
			fetchProfile().map { user ->
				AuthPayload(
					user = user,
					tokens = tokens.toDomain()
				)
			}
		}
	}

	suspend fun fetchProfile(): Result<User> {
		return networkRequest<UserResponse> {
			method = HttpMethod.Get
			url {
				path("api", "v1", "users", "profile")
			}
		}.onSuccess { response ->
			storeUser(response)
		}.map { response -> response.toDomain() }
	}

	private suspend fun storeUser(user: UserResponse) {
		appDatabase.withTransaction {
			userDao.clear()
			userDao.upsert(
				UserEntity(
					id = user.id,
					firstName = user.firstName,
					secondName = user.secondName,
					email = user.email,
					avatarFileKey = user.avatarFileKey
				)
			)
		}
	}

	private suspend fun saveTokens(tokens: TokenResponse) {
		authorizationDataStore.saveTokenPair(
			accessToken = tokens.accessToken,
			refreshToken = tokens.refreshToken
		)
	}

	private fun UserEntity.toDomain() = User(
		id = id,
		firstName = firstName,
		secondName = secondName,
		email = email,
		avatarFileKey = avatarFileKey
	)

	private fun UserResponse.toDomain() = User(
		id = id,
		firstName = firstName,
		secondName = secondName,
		email = email,
		avatarFileKey = avatarFileKey
	)

	private fun TokenResponse.toDomain() = AuthTokens(
		accessToken = accessToken,
		refreshToken = refreshToken
	)

	private inline fun <T, R> Result<T>.flatMap(transform: (value: T) -> Result<R>): Result<R> =
		fold(
			onSuccess = { transform(it) },
			onFailure = { Result.failure(it) }
		)
}
