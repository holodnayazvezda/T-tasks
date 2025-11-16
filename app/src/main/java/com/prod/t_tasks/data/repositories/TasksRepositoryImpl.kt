package com.prod.t_tasks.data.repositories

import com.prod.t_tasks.data.base.BaseRepository
import com.prod.t_tasks.data.data_providers.api.ApiKtorClient
import com.prod.t_tasks.data.data_providers.local_db.AppDatabase
import com.prod.t_tasks.data.data_providers.local_db.entities.mapToDB
import com.prod.t_tasks.data.dto.tasks.TaskCategoryDTO
import com.prod.t_tasks.domain.interfaces.TasksRepository
import com.prod.t_tasks.domain.model.TaskCategory
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
class TasksRepositoryImpl(
	apiKtorClient: ApiKtorClient,
	appDatabase: AppDatabase
) : TasksRepository, BaseRepository() {

	override val defaultKtorClient = apiKtorClient.client
	override val db = appDatabase

	private val tasksCategoriesDao = appDatabase.taskCategoriesDao()

	private fun getTaskCategoriesDataFlow(): Flow<List<TaskCategory>> {
		return tasksCategoriesDao.getAll()
			.map { listOfEntities -> listOfEntities.map { it.mapToDomain() } }
	}

	private suspend fun saveTaskCategoriesToDb(cartPositions: List<TaskCategory>) {
		tasksCategoriesDao.upsert(cartPositions.map { it.mapToDB() })
	}

	private suspend fun getTopicsNetwork(): Result<List<TaskCategory>> {
		return networkRequest<List<TaskCategoryDTO>> {
			method = HttpMethod.Get
			url {
				path("api", "v1", "tasks", "categories")
			}
		}.map { it.map { it.mapToDomain() } }
	}

	override fun loadTopics(): Flow<Result<List<TaskCategory>>> =
		merge(
			getTaskCategoriesDataFlow().map { Result.success(it) },
			flow<Result<List<TaskCategory>>> {

				val networkResult = getTopicsNetwork()

				networkResult.onSuccess {
					saveTaskCategoriesToDb(it)
				}.onFailure {
					emit(Result.failure(it.convertToNetworkError()))
				}
			}.catch { e ->
				emit(Result.failure(e.convertToNetworkError()))
			}
		)
}