package com.prod.t_tasks.domain.utils

import com.prod.t_tasks.domain.utils.NetworkError.Connection
import com.prod.t_tasks.domain.utils.NetworkError.InputError
import com.prod.t_tasks.domain.utils.NetworkError.Unexpected
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

/**
 * Network error wrapper class
 */
sealed class NetworkError : Throwable() {

	/**
	 * Network connection error
	 */
	class Connection() : NetworkError()

	/**
	 * Unexpected error for example HTTP code - 500 or exception when mapping data
	 */
	class Unexpected(val error: String) : NetworkError()

	/**
	 * User input error - 400 codes
	 */
	class InputError(val error: String) : NetworkError()
}

fun Throwable.convertToNetworkError() =
	when (this) {
		is NetworkError -> this
		is RedirectResponseException -> Unexpected(error = message)
		is ClientRequestException -> InputError(error = message)
		is ServerResponseException -> Unexpected(error = message)
		else -> Connection()
	}