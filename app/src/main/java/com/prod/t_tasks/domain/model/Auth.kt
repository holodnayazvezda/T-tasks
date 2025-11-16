package com.prod.t_tasks.domain.model

data class RegisterData(
	val firstName: String,
	val secondName: String,
	val email: String,
	val password: String,
	val avatarFileKey: String? = null,
	val fsmToken: String? = null
)

data class LoginData(
	val email: String,
	val password: String
)

data class UpdateUserData(
    val firstName: String,
    val secondName: String
)