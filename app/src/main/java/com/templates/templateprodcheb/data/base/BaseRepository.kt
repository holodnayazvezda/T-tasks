package com.templates.templateprodcheb.data.base

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.RoomDatabase
import com.templates.templateprodcheb.domain.utils.RemoteWrapper
import com.templates.templateprodcheb.domain.utils.convertToNetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

	/**
	 * WARNING! Documentation doesn't contain information about caching, but it will be added soon, sorry, I'm lazy :))
	 *
	 * Creates a data flow that attempts to load data from a local database first,
	 * while simultaneously fetching fresh data from the network.
	 *
	 * This function is designed to provide a responsive user experience by showing cached data quickly,
	 * and then updating it with the latest data from the network.
	 *
	 * It handles various scenarios:
	 * - **Successful DB load & successful network request:** Emits DB data, then network data (after saving to DB).
	 * - **Failed DB load & successful network request:** Attempts to restart DB collection after saving network data. If restart fails, it emits the saved network data.
	 * - **Successful DB load & failed network request:** Emits DB data, then emits the network error.
	 * - **Failed DB load & failed network request:** Emits DB load error, then emits the network error.
	 * - **Errors during DB saving or mapping:** Emits relevant errors.
	 *
	 * Emitted data is mapped to `DomainDTO`. Network errors are converted using `convertToNetworkError`.
	 *
	 * @param T The type of the domain data object.
	 * @param DBEntity The type of the database entity, which must be mappable to `DomainDTO`.
	 * @param dbDataFlow A lambda function that returns a `Flow` of `DBEntity` from the database.
	 *                   This flow will be collected to emit cached data.
	 * @param makeRequest A suspend lambda function that performs the network request and returns a `Result`
	 *                    containing a `DBMappableDTO<DBEntity>`. It's recommended to use the `networkRequest`
	 *                    helper function for this.
	 * @param saveToDB A suspend lambda function that takes a `DBEntity` (typically from the network response)
	 *                 and saves it to the database.
	 *
	 * @return A `RemoteWrapper<DomainDTO>` (which is a `Flow<Result<DomainDTO>>`) that emits `Result` objects
	 *         representing either successfully loaded/mapped data or errors encountered during the process.
	 */
	internal inline fun <reified T> progressiveCachedRequest(
		crossinline dbDataFlow: () -> Flow<T>,
		crossinline makeRequest: suspend () -> Result<T>,
		crossinline saveToDB: suspend (T) -> Unit
	): RemoteWrapper<T> =
		flow {
			suspend fun performLocalDbCollection(
				attemptDescription: String,
				onFailureCallback: suspend (exception: Exception) -> Unit
			) {
				try {
					dbDataFlow.invoke()
						.map { dbEntity -> Result.success(dbEntity) }
						.distinctUntilChanged()
						.collect { resultFromDb ->
							this@flow.emit(resultFromDb)
						}
				} catch (e: CancellationException) {
					throw e
				} catch (e: Exception) {
					Log.e(TAG, "$attemptDescription DB data flow collection error.", e)
					onFailureCallback(e)
					this@flow.emit(Result.failure(e.convertToNetworkError()))
				}
			}

			var initialDbCollectionFailed = false

			coroutineScope {
				launch {
					performLocalDbCollection(
						attemptDescription = "Initial",
						onFailureCallback = { initialDbCollectionFailed = true }
					)
				}

				val networkResult = makeRequest.invoke()

				networkResult.onSuccess { networkDataWrapper ->
					try {
						val dbEntityToSave = networkDataWrapper
						saveToDB(dbEntityToSave)

						if (initialDbCollectionFailed) {
							Log.d(
								TAG,
								"Initial DB collection failed. Attempting to restart DB collection after saving network data."
							)
							launch {
								performLocalDbCollection(
									attemptDescription = "Restarted",
									onFailureCallback = { exception ->
										Log.e(
											TAG,
											"Restarted DB collection also failed. Manually emitting saved network data.",
											exception
										)
										try {
											val domainDtoFromResult = dbEntityToSave
											this@flow.emit(Result.success(domainDtoFromResult))
										} catch (mapError: Exception) {
											Log.e(
												TAG,
												"Error mapping saved network data for fallback emit.",
												mapError
											)
										}
									}
								)
							}
						}
					} catch (e: CancellationException) {
						throw e
					} catch (e: Exception) {
						Log.e(TAG, "Error saving network data to DB or during post-save logic", e)
						this@flow.emit(Result.failure(e.convertToNetworkError()))
					}
				}.onFailure { error ->
					Log.w(TAG, "Network request failed: ${error.message}", error)
					this@flow.emit(Result.failure(error))
				}
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
