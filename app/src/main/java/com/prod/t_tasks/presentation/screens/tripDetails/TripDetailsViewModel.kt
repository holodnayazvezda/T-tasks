package com.prod.t_tasks.presentation.screens.tripDetails

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.usecase.trips.GetUsersByEmailUseCase
import com.prod.t_tasks.presentation.commonDataModels.UIUserBaseInfoModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.collections.plus

data class UITrip(
	val id: Long,
	val country: String,
	val city: String,
	val dateRangeString: String,
	val purpose: String,
	val description: String,
	val members: List<Pair<Long, String>>
)

@KoinViewModel
class TripDetailsViewModel(
	private val getUsersByEmailUseCase: GetUsersByEmailUseCase,
//	private val getTripInfoUseCase: GetTripInfoUseCase
): BaseViewModel() {

	val tripInfo = MutableUIStateFlow<UITrip>()

	fun addMember(info: UIUserBaseInfoModel) {
		viewModelScope.launch {
			searchEmail.emit("")
		}
	}

	private val searchEmail = MutableStateFlow("")

	val currentEmailValue = searchEmail.asStateFlow()

	val usersLoadingState = mutableStateOf(false)

	@OptIn(FlowPreview::class)
	val foundUsers: StateFlow<List<UIUserBaseInfoModel>> =
		searchEmail
			.debounce(500L)
			.onEach { usersLoadingState.value = true }
			.map {
				getUsersByEmailUseCase(it).fold(
					onSuccess = {
						it
					},
					onFailure = {
						Log.e("CreateTripViewModel", it.message, it)
						emptyList()
					}
				).map { it.mapToUI() }
			}
			.onEach { usersLoadingState.value = false }
			.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

	fun searchEmailChanged(email: String) {
		viewModelScope.launch {
			searchEmail.emit(email)
		}
	}
}