package com.prod.t_tasks.data.repositories

import com.prod.t_tasks.data.base.BaseRepository
import com.prod.t_tasks.data.data_providers.api.ApiKtorClient
import com.prod.t_tasks.data.data_providers.local_db.AppDatabase
import com.prod.t_tasks.data.data_providers.local_db.entities.mapToDB
import com.prod.t_tasks.data.dto.trips.TripDTO
import com.prod.t_tasks.domain.interfaces.TripsRepository
import com.prod.t_tasks.domain.model.trip.Trip
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.domain.utils.convertToNetworkError
import io.ktor.http.HttpMethod
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import org.koin.core.annotation.Single

@Single
class TripsRepositoryImpl(
	apiKtorClient: ApiKtorClient,
	appDatabase: AppDatabase
) : TripsRepository, BaseRepository() {

	override val defaultKtorClient = apiKtorClient.client
	override val db = appDatabase

	private val userDao = appDatabase.userDao()


	private val tripsDao = db.tripsDao()

	private fun getTripsDataFlow(): Flow<List<Trip>> {
		return tripsDao.getAll()
			.map { listOfEntities -> listOfEntities.map { it.mapToDomain() } }
	}

	private suspend fun saveTripsToDb(trips: List<Trip>) {
		tripsDao.upsert(trips.map { it.mapToDB() })
	}

	private suspend fun clearCache() {
//		tripsDao.clearCache(System.currentTimeMillis() - 43200000)
	}

	private suspend fun getTripsNetwork(): Result<List<Trip>> {
		val savedUser = userDao.getUser()
		return savedUser?.id?.let { userId ->
			networkRequest<List<TripDTO>> {
				method = HttpMethod.Get
				url {
					path("api", "v1", "trips")
				}
			}.map { it.map { it.mapToDomain(userId) } }
		} ?: Result.failure(NetworkError.Unexpected("Пользователь не найден"))
	}

	override fun getAllTrips(): Flow<Result<List<Trip>>> =
		merge(
			getTripsDataFlow().map { Result.success(it) },
			flow<Result<List<Trip>>> {

				val networkResult = getTripsNetwork()

				networkResult.onSuccess {
					saveTripsToDb(it).also {
						clearCache()
					}
				}.onFailure {
					emit(Result.failure(it.convertToNetworkError()))
				}
			}.catch { e ->
				emit(Result.failure(e.convertToNetworkError()))
			}
		)
}