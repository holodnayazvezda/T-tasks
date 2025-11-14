package com.prod.t_tasks.domain.model

data class User(
	val id: Long,
	val firstName: String,
	val secondName: String,
	val email: String,
	val avatarFileKey: String?
)

data class AuthTokens(
	val accessToken: String,
	val refreshToken: String
)

data class AuthPayload(
	val user: User,
	val tokens: AuthTokens
)
