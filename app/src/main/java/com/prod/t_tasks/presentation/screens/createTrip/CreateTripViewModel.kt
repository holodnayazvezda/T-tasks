package com.prod.t_tasks.presentation.screens.createTrip

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.usecase.trips.GetUsersByEmailUseCase
import com.prod.t_tasks.presentation.commonDataModels.UIUserBaseInfoModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import com.prod.t_tasks.presentation.utils.convertGMTToSystemTimezone
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class TripCreateFormState(
	val dateStart: Long? = null,
	val dateEnd: Long? = null,
	val otherCountry: Boolean = false,
	val country: String? = null,
	val city: String? = null,
	val description: String? = null,
	val goal: String? = null,
	val goalV: String? = null,
	val transport: String? = null,
	val transportV: String? = null,
	val countSmallChildren: Int = 0,
	val countMediumChildren: Int = 0,
	val countBigChildren: Int = 0,
	val pets: Boolean = false,
	val members: List<Pair<Long, String>> = emptyList()
)

@KoinViewModel
class CreateTripViewModel(
	private val getUsersByEmailUseCase: GetUsersByEmailUseCase
) : BaseViewModel() {
	private val _formState = MutableStateFlow(TripCreateFormState())
	val formState: StateFlow<TripCreateFormState> = _formState


	fun setDate(dates: Pair<Long, Long>) {
		_formState.update {
			it.copy(
				dateStart = convertGMTToSystemTimezone(dates.first),
				dateEnd = convertGMTToSystemTimezone(dates.second)
			)
		}
	}

	fun setOtherCountry(state: Boolean) {
		_formState.update {
			it.copy(
				otherCountry = state
			)
		}
	}

	fun setCountry(country: String) {
		_formState.update {
			it.copy(
				country = country
			)
		}
	}

	fun setCity(city: String) {
		_formState.update {
			it.copy(
				city = city
			)
		}
	}

	fun setDescription(description: String) {
		_formState.update {
			it.copy(
				description = description
			)
		}
	}


	val goals = listOf(
		Pair("plane", "Самолёт"),
		Pair("car", "Автомобиль"),
		Pair("train", "Поезд")
	)

	fun setGoal(goal: Pair<String, String>) {
		_formState.update {
			it.copy(
				goal = goal.first,
				goalV = goal.second
			)
		}
	}

	val transports = listOf(
		Pair("plane", "Самолёт"),
		Pair("car", "Автомобиль"),
		Pair("train", "Поезд")
	)

	fun setTransport(transport: Pair<String, String>) {
		_formState.update {
			it.copy(
				transport = transport.first,
				transportV = transport.second
			)
		}
	}

	fun upCountOfSmallChildren() {
		_formState.update {
			it.copy(
				countSmallChildren = it.countSmallChildren + 1
			)
		}
	}

	fun downCountOfSmallChildren() {
		_formState.update {
			it.copy(
				countSmallChildren = maxOf(it.countSmallChildren - 1, 0)
			)
		}
	}

	fun upCountOfMediumChildren() {
		_formState.update {
			it.copy(
				countMediumChildren = it.countMediumChildren + 1
			)
		}
	}

	fun downCountOfMediumChildren() {
		_formState.update {
			it.copy(
				countMediumChildren = maxOf(it.countMediumChildren - 1, 0)
			)
		}
	}

	fun upCountOfBigChildren() {
		_formState.update {
			it.copy(
				countBigChildren = it.countBigChildren + 1
			)
		}
	}

	fun downCountOfBigChildren() {
		_formState.update {
			it.copy(
				countBigChildren = maxOf(it.countBigChildren - 1, 0)
			)
		}
	}

	fun setPets(state: Boolean) {
		_formState.update {
			it.copy(
				pets = state
			)
		}
	}

	fun addMember(info: Pair<Long, String>) {
		_formState.update {
			it.copy(
				members = it.members + listOf(info)
			)
		}
	}

	private val searchEmail = MutableStateFlow("")

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