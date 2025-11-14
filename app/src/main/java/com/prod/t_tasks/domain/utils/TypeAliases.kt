package com.prod.t_tasks.domain.utils

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * Simple wrapper for convenience of network requests in repositories
 *
 * @see Flow
 * @see Result
 * @see NetworkError
 */
internal typealias RemoteWrapper<T> = Flow<Result<T>>

/**
 * Simple wrapper for convenience of network paging requests in repositories
 *
 * @see Flow
 * @see PagingData
 */
internal typealias RemotePagingWrapper<T> = Flow<PagingData<T>>