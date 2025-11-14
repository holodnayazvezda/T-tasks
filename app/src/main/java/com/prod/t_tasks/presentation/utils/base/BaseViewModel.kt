package com.prod.t_tasks.presentation.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.prod.t_tasks.domain.utils.convertToNetworkError
import com.prod.t_tasks.presentation.utils.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Base class for all [ViewModel]s
 */
abstract class BaseViewModel : ViewModel() {

	/**
	 * Creates [MutableStateFlow] with [UIState] and the given initial value [UIState.Idle]
	 */
	@Suppress("FunctionName")
	protected fun <T> MutableUIStateFlow(defaultValue: T? = null) =
		MutableStateFlow<UIState<T>>(defaultValue?.let { UIState.Success(it) } ?: UIState.Idle())

	/**
	 * Reset [MutableUIStateFlow] to [UIState.Idle]
	 */
	protected fun <T> MutableStateFlow<UIState<T>>.reset() {
		value = UIState.Idle()
	}

	/**
	 * Collect network request
	 *
	 * @return [UIState] depending request result
	 */
	protected fun <T> Flow<Result<T>>.collectRequest(
		state: MutableStateFlow<UIState<T>>,
	) {
		viewModelScope.launch {
			state.value = UIState.Loading()
			this@collectRequest.collect {
				state.value = if (it.isSuccess) {
					UIState.Success(it.getOrNull()!!)
				} else {
					UIState.Error(it.exceptionOrNull()!!.convertToNetworkError())
				}
			}
		}
	}

	/**
	 * Collect network request
	 *
	 * @return [UIState] depending request result
	 */
	protected fun <T> Result<T>.collectRequest(
		state: MutableStateFlow<UIState<T>>
	) {
		state.value = UIState.Loading()
		state.value = if (isSuccess) {
			UIState.Success(getOrNull()!!)
		} else {
			UIState.Error(exceptionOrNull()!!.convertToNetworkError())
		}
	}


	/**
	 * Collect paging request
	 */
	protected fun <T : Any, S : Any> Flow<PagingData<T>>.collectPagingRequest(
		mappedData: suspend (T) -> S
	) = map { it.map { data -> mappedData(data) } }.cachedIn(viewModelScope)

}
