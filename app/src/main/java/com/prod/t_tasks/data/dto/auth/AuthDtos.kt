package com.prod.t_tasks.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorNetworkDTO(
    val detail: String
)

@Serializable
data class UserCreateRequest(
	@SerialName("first_name")
	val firstName: String,
	@SerialName("second_name")
	val secondName: String,
	val email: String,
	@SerialName("avatar_file_key")
	val avatarFileKey: String? = null,
	val password: String,
	@SerialName("fsm_token")
	val fsmToken: String? = null
)

@Serializable
data class UserLoginRequest(
	val email: String,
	val password: String
)

@Serializable
data class RefreshTokenRequest(
	@SerialName("refresh_token")
	val refreshToken: String
)

@Serializable
data class UpdateUserRequest(
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("second_name")
    val secondName: String? = null,
    @SerialName("fsm_token")
    val fsmToken: String? = null,
    @SerialName("avatar_file_key")
    val avatarFileKey: String? = null
)

@Serializable
data class TokenResponse(
	@SerialName("refresh_token")
	val refreshToken: String,
	@SerialName("access_token")
	val accessToken: String
)

@Serializable
data class UserResponse(
	@SerialName("first_name")
	val firstName: String,
	@SerialName("second_name")
	val secondName: String,
	val email: String,
	@SerialName("avatar_file_key")
	val avatarFileKey: String? = null,
	val id: Long
)

@Serializable
data class UserRegisterResponse(
	val user: UserResponse,
	val tokens: TokenResponse
)