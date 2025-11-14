package com.prod.t_tasks.data.base

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.RoomDatabase
import com.prod.t_tasks.domain.utils.convertToNetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

abstract class BaseRepository {

	// Caching module ==============================================================================
	private val internalCacheStorage = mutableMapOf<String, CacheEntry<*>>()

	private data class CacheEntry<T>(
		val value: T,
		val expirationTime: Long
	)

	fun <T> putCache(cacheConfiguration: Pair<String, Duration>, value: T) {
		internalCacheStorage[cacheConfiguration.first] = CacheEntry(value, cacheConfiguration.second.inWholeSeconds)
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> getCache(key: String): T? {
		val entry = internalCacheStorage[key] ?: return null
		if (entry.expirationTime < System.currentTimeMillis()) {
			internalCacheStorage.remove(key)
			return null
		}
		return entry.value as T
	}

	// Base data sources ===========================================================================

	protected open val defaultKtorClient: HttpClient? = null
	protected open val db: RoomDatabase? = null

	companion object {
		private const val TAG = "BaseRepository"
	}

	// Internal methods ============================================================================

	private fun assertKtorClientSpecify() {
		if (defaultKtorClient == null) {
			Log.e(TAG, "You must specify ktor client for make network requests")
			throw IllegalStateException("You must specify ktor client for make network requests")
		}
	}

	private fun assertDBSpecify() {
		if (db == null) {
			throw IllegalStateException("You must specify db for use pagination/cashing")
		}
	}

	// And methods for use :) ======================================================================

	/**
	 * Makes a network request using the provided Ktor client and request builder block.
	 *
	 * This function handles the common boilerplate for making a network request,
	 * including error handling and converting exceptions to a domain-specific `NetworkError`.
	 *
	 * @param T The expected successful response type. This type must be deserializable by Ktor.
	 * @param ktorClient The [HttpClient] to use for the request. Defaults to `this.defaultKtorClient`.
	 *                   An [IllegalStateException] will be thrown if no client is provided and `defaultKtorClient` is null.
	 * @param block A lambda function that configures the [HttpRequestBuilder] for the request.
	 * @return A [Result] object containing either the successful response of type [T] or a [NetworkError] if the request fails.
	 * @throws IllegalStateException if `ktorClient` is null and `defaultKtorClient` is also null.
	 */
	internal suspend inline fun <reified T> networkRequest(
		ktorClient: HttpClient? = this.defaultKtorClient,
		cacheConfiguration: Pair<String, Duration>? = null,
		block: HttpRequestBuilder.() -> Unit
	): Result<T> {
		Log.d(TAG, "Network request! Asserting ktor client specify")
		assertKtorClientSpecify()
		Log.d(TAG, "ktor client is specified - continue network request")
		return try {
			Log.d(TAG, "Start request!")
			val response = ktorClient!!.request(block = block)
			Log.d(TAG, "Request was made without exceptions")
			Result.success(
				value = response
			).map {
				it.body<T>()
			}
		} catch (e: Exception) {
			Log.e(TAG, "Exception in request process! $e")
			Result.failure(
				exception = e.convertToNetworkError()
			)
		}.onSuccess {
			Log.v(TAG, "Network request was successful")
			if (cacheConfiguration != null) {
				putCache(cacheConfiguration, it)
			}
		}.onFailure {
			Log.e(TAG, "Network request has error! $it")
		}
	}


	internal suspend inline fun <reified T> internalCachedRequest(
		ktorClient: HttpClient? = this.defaultKtorClient,
		cacheConfiguration: Pair<String, Duration>,
		block: HttpRequestBuilder.() -> Unit
	): Result<T> {
		val cachedResult = getCache<T>(cacheConfiguration.first)

		return if (cachedResult != null) {
			Result.success(cachedResult)
		} else {
			networkRequest<T>(ktorClient, cacheConfiguration, block)
		}
	}

	@OptIn(ExperimentalPagingApi::class)
	protected fun <Value : BaseEntity> paginatedRequest(
		pageSize: Int = 10,
		prefetchDistance: Int = pageSize,
		enablePlaceholders: Boolean = true,
		initialLoadSize: Int = pageSize * 3,
		maxSize: Int = Int.MAX_VALUE,
		jumpThreshold: Int = Int.MIN_VALUE,
		dbDao: BasePaginationDAO<Value>,
		makeRequest: suspend (page: Long, pageCount: Int) -> Result<List<Value>>
	): Flow<PagingData<Value>> {
		assertDBSpecify()

		return Pager(
			config = PagingConfig(
				pageSize,
				prefetchDistance,
				enablePlaceholders,
				initialLoadSize,
				maxSize,
				jumpThreshold
			),
			remoteMediator = BaseRemoteMediator(
				db = db!!,
				dao = dbDao,
				makeRequest = makeRequest
			),
			pagingSourceFactory = {
				dbDao.getPaginatedData()
			}
		).flow
	}
}
