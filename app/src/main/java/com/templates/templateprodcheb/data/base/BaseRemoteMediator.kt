package com.templates.templateprodcheb.data.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction

@OptIn(ExperimentalPagingApi::class)
class BaseRemoteMediator<DBEntity : BaseEntity>(
	private val db: RoomDatabase,
	private val dao: BasePaginationDAO<DBEntity>,
	private val makeRequest: suspend (page: Long, pageCount: Int) -> Result<List<DBEntity>>
) : RemoteMediator<Int, DBEntity>() {

	override suspend fun load(
		loadType: LoadType,
		state: PagingState<Int, DBEntity>
	): MediatorResult {
		return try {
			val loadKey = when (loadType) {
				LoadType.REFRESH -> 1
				LoadType.PREPEND -> return MediatorResult.Success(
					endOfPaginationReached = true
				)

				LoadType.APPEND -> {
					val lastItem = state.lastItemOrNull()
					if (lastItem == null) {
						1
					} else {
						(lastItem.id.toLong() / state.config.pageSize) + 1
					}
				}
			}

			val result = makeRequest(
				loadKey,
				state.config.pageSize
			)

			if (result.isSuccess) {
				val data = result.getOrNull()!!
				db.withTransaction {
					if (loadType == LoadType.REFRESH) {
						dao.clearAll()
					}
					val beerEntities = data
					dao.upsertAll(beerEntities)
				}
				MediatorResult.Success(
					endOfPaginationReached = data.size < state.config.pageSize
				)
			} else {
				MediatorResult.Error(result.exceptionOrNull()!!)
			}
		} catch (e: Exception) {
			MediatorResult.Error(e)
		}
	}
}