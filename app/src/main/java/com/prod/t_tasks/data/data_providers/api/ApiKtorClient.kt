package com.prod.t_tasks.data.data_providers.api

import android.util.Log
import com.prod.t_tasks.data.data_providers.localInfo.AuthorizationDataStore
import com.prod.t_tasks.common.Constants
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.koin.core.annotation.Single
import java.util.Base64


// Configuration Ktor client for request to API
@Single
class ApiKtorClient(authorizationDataStore: AuthorizationDataStore) {

	val client = HttpClient(OkHttp) {
		install(Logging) {
			logger = Logger.ANDROID
			level = LogLevel.ALL
		}
		install(HttpRequestRetry) {
			retryOnServerErrors(maxRetries = 3)
			exponentialDelay()
		}
		install(ContentNegotiation) {
			json(Json {
				prettyPrint = true
				isLenient = true
				ignoreUnknownKeys = true
			})
		}
		defaultRequest {
			url(Constants.BASE_API_URL)
		}
		install(Auth) {
			bearer {
				sendWithoutRequest { request ->
					val segments = request.url.pathSegments

					val endpointsWithoutAuth = listOf(
						"register",
						"login",
						"refresh"
					)

					endpointsWithoutAuth.any { segments.contains(it) }.not()
				}
				loadTokens {
					return@loadTokens authorizationDataStore.tokenPair.first()
						.toBearerTokens()
				}
				refreshTokens {
					val tokens = authorizationDataStore.tokenPair.first()
					val exp = tokens.first.getJwtExpiration() ?: 0L
					val currTime = System.currentTimeMillis() / 1000
					Log.d(
						"KtorClient",
						"Current JWT exp: $exp; Current time: $currTime"
					)
					if (exp < (currTime + 60)) {
						Log.d(
							"KtorClient",
							"Request new token pair"
						)
						// Requesting new token pair by refresh token
						TODO("make request")
//						val response = client.post("/auth/refresh") {
//							contentType(ContentType.Application.Json)
//							setBody(
//								RefreshTokenModel(
//									authorizationDataStore.tokenPair.first().second
//								)
//							)
//						}
//
//						if (response.status.isSuccess().not()) {
//							authorizationDataStore.saveTokenPair(
//								accessToken = "",
//								refreshToken = ""
//							)
//							return@refreshTokens null
//						} else {
//							return@refreshTokens response.body<TokensDTO>()
//								.mapToBearerTokens().also {
//									authorizationDataStore.saveTokenPair(
//										accessToken = it.accessToken,
//										refreshToken = it.refreshToken ?: ""
//									)
//								}
//						}
					} else {
						tokens.toBearerTokens()
					}
				}
			}
		}
	}

	fun Pair<String, String>.toBearerTokens() = BearerTokens(this.first, this.second)

	private fun String.getJwtExpiration(): Long? {
		return try {
			val parts = this.split(".")
			if (parts.size != 3) throw IllegalArgumentException("Invalid JWT token format")

			val payload = parts[1]

			// Add padding if necessary and decode Base64Url
			val paddedPayload = payload.padEnd(payload.length + (4 - payload.length % 4) % 4, '=')
			val decodedBytes = Base64.getUrlDecoder().decode(paddedPayload)
			val decodedString = String(decodedBytes, Charsets.UTF_8)

			// Parse JSON and extract exp claim
			val json = JSONObject(decodedString)
			json.getLong("exp")
		} catch (e: Exception) {
			// Handle parsing errors, invalid format, or missing exp field
			null
		}
	}
}
