package com.prod.t_tasks.presentation.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.prod.t_tasks.domain.utils.NetworkError
import com.prod.t_tasks.domain.utils.convertToNetworkError
import com.prod.t_tasks.presentation.utils.ui.placeholders.ErrorPlaceholder
import com.prod.t_tasks.presentation.utils.ui.placeholders.LoadingPlaceholder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

sealed class UIState<T> {
	class Idle<T> : UIState<T>()
	class Loading<T> : UIState<T>()
	class Error<T>(val error: NetworkError) : UIState<T>()
	class Success<T>(val data: T) : UIState<T>()

	fun <S> map(mapper: (T) -> S): UIState<S> {
		return when (this) {
			is Idle -> Idle()
			is Loading -> Loading()
			is Error -> Error(this.error)
			is Success -> Success(mapper(this.data))
		}
	}

	fun getOrNull(): T? = if (this is Success) {
		data
	} else {
		null
	}

	val isSuccess: Boolean
		get() = this is Success
}

interface ErrorCallbacks {
	fun processConnectionError(networkError: NetworkError.Connection)
	fun processUnexpectedError(networkError: NetworkError.Unexpected)
}

open class ErrorCollectorScope(
	private val context: Context,
	val navController: NavController,
	private val errorCallbacks: ErrorCallbacks
) {
	companion object {
		private const val TAG = "ErrorCollectorScope"
	}

	@Composable
	fun <T> Flow<UIState<T>>.collectAsStateWithCallbacks(
		onInputError: ((NetworkError.InputError) -> Unit) = {
			Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
				.show()
		},
		onLoading: (() -> Unit) = {},
		onSuccess: (T) -> Unit = {}
	): State<UIState<T>> = this.onEach {
		when (it) {
			is UIState.Loading -> {
				onLoading()
			}

			is UIState.Error -> {
				Log.e(TAG, "collected error ${it.error}")
				when (it.error) {
					is NetworkError.Connection -> errorCallbacks.processConnectionError(it.error)
					is NetworkError.Unexpected -> errorCallbacks.processUnexpectedError(it.error)
					is NetworkError.InputError -> onInputError.invoke(it.error)
				}
			}

			is UIState.Success -> {
				onSuccess.invoke(it.data)
			}

			else -> {}
		}
	}.collectAsState(UIState.Idle())


	@Composable
	fun <T> Flow<UIState<T>>.collectAsValueStateWithCallbacks(
		onInputError: ((NetworkError.InputError) -> Unit) = {
			Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
				.show()
		},
		onLoading: (() -> Unit) = {},
		onSuccess: (T) -> Unit = {}
	): State<T?> = this.map {
		when (it) {
			is UIState.Loading -> {
				onLoading()
				null
			}

			is UIState.Error -> {
				Log.e(TAG, "collected error ${it.error}")
				when (it.error) {
					is NetworkError.Connection -> errorCallbacks.processConnectionError(it.error)
					is NetworkError.Unexpected -> errorCallbacks.processUnexpectedError(it.error)
					is NetworkError.InputError -> onInputError.invoke(it.error)
				}
				null
			}

			is UIState.Success -> {
				onSuccess.invoke(it.data)
				it.data
			}

			else -> {
				null
			}
		}
	}.collectAsState(null)

	@Composable
	fun <T> Flow<UIState<T>>.FoldUIStateWithGlobalCallbacks(
		modifier: Modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp),
		onIdle: @Composable () -> Unit = {},
		onError: @Composable (NetworkError) -> Unit = { ErrorPlaceholder(modifier = modifier) { navController?.popBackStack() } },
		onLoading: @Composable () -> Unit = { LoadingPlaceholder(modifier = modifier) },
		onSuccess: @Composable (T) -> Unit
	) {
		val state = this.onEach {
			if (it is UIState.Error) {
				Log.e(TAG, "collected error ${it.error}")
				when (it.error) {
					is NetworkError.Connection -> errorCallbacks.processConnectionError(it.error)
					is NetworkError.Unexpected -> errorCallbacks.processUnexpectedError(it.error)
					else -> {}
				}
			}
		}.collectAsState(initial = UIState.Idle()).value

		when (state) {
			is UIState.Idle -> {
				onIdle()
			}

			is UIState.Error -> {
				onError(state.error.convertToNetworkError())
			}

			is UIState.Loading -> {
				onLoading()
			}

			is UIState.Success -> {
				onSuccess(state.data)
			}
		}
	}

	@Composable
	fun <T> UIState<T>.FoldUIStateWithGlobalCallbacks(
		modifier: Modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp),
		onIdle: @Composable () -> Unit = {},
		onError: @Composable (NetworkError) -> Unit = { ErrorPlaceholder(modifier = modifier) { navController?.popBackStack() } },
		onLoading: @Composable () -> Unit = { LoadingPlaceholder(modifier = modifier) },
		onSuccess: @Composable (T) -> Unit
	) {
		if (this is UIState.Error) {
			Log.e(TAG, "collected error ${this.error}")
			when (error) {
				is NetworkError.Connection -> errorCallbacks.processConnectionError(error)
				is NetworkError.Unexpected -> errorCallbacks.processUnexpectedError(error)
				else -> {}
			}
		}

		when (this) {
			is UIState.Idle -> {
				onIdle()
			}

			is UIState.Error -> {
				onError(error.convertToNetworkError())
			}

			is UIState.Loading -> {
				onLoading()
			}

			is UIState.Success -> {
				onSuccess.invoke(data)
			}
		}
	}
}

@Composable
fun ErrorCollectorScope(
	context: Context,
	navController: NavController? = null,
	errorCallbacks: ErrorCallbacks,
	content: @Composable ErrorCollectorScope.() -> Unit
) {
	ErrorCollectorScope(context, navController?: rememberNavController(), errorCallbacks).content()
}