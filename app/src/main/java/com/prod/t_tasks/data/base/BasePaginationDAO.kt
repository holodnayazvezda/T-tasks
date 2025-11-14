package com.prod.t_tasks.data.base

import androidx.paging.PagingSource

interface BasePaginationDAO<T : Any> {
	suspend fun clearAll()
	suspend fun upsertAll(data: List<T>)

	fun getPaginatedData(): PagingSource<Int, T>
}