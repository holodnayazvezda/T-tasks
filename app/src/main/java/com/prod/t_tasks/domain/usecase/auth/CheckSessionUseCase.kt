package com.prod.t_tasks.domain.usecase.auth

import com.prod.t_tasks.data.data_providers.localInfo.AuthorizationDataStore
import com.prod.t_tasks.data.repositories.AuthRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class CheckSessionUseCase(
	private val authRepository: AuthRepository,
	private val refreshTokensUseCase: RefreshTokensUseCase,
	private val authorizationDataStore: AuthorizationDataStore
) {
	suspend operator fun invoke(): Boolean {
		val tokens = authorizationDataStore.tokenPair.first()
		if (tokens.first.isBlank() || tokens.second.isBlank()) {
			return false
		}

		val profileResult = authRepository.fetchProfile()
		if (profileResult.isSuccess) {
			return true
		}

		val refreshResult = refreshTokensUseCase()
		if (refreshResult.isFailure) {
			authorizationDataStore.clearTokenPair()
			return false
		}

		val profileAfterRefresh = authRepository.fetchProfile()
		if (profileAfterRefresh.isSuccess) {
			return true
		}

		authorizationDataStore.clearTokenPair()
		return false
	}
}
