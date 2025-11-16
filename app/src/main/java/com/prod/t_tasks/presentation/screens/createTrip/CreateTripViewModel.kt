package com.prod.t_tasks.presentation.screens.createTrip

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.model.trip.TripCreate
import com.prod.t_tasks.domain.usecase.trips.CreateTripFields
import com.prod.t_tasks.domain.usecase.trips.CreateTripUseCase
import com.prod.t_tasks.domain.usecase.trips.GetUsersByEmailUseCase
import com.prod.t_tasks.domain.usecase.trips.ValidateTripFieldsUseCase
import com.prod.t_tasks.presentation.commonDataModels.UIUserBaseInfoModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import com.prod.t_tasks.presentation.utils.convertGMTToSystemTimezone
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
	val members: List<Pair<Long, String>> = emptyList(),
    val errors: Map<CreateTripFields, String> = emptyMap()
)

@KoinViewModel
class CreateTripViewModel(
	private val getUsersByEmailUseCase: GetUsersByEmailUseCase,
    private val validateTripFieldsUseCase: ValidateTripFieldsUseCase,
    private val createTripUseCase: CreateTripUseCase
) : BaseViewModel() {
	private val _formState = MutableStateFlow(TripCreateFormState())
	val formState: StateFlow<TripCreateFormState> = _formState


    fun setDate(dates: Pair<Long, Long>) {
        _formState.update {
            it.copy(
                dateStart = convertGMTToSystemTimezone(dates.first),
                dateEnd = convertGMTToSystemTimezone(dates.second),
                errors = it.errors - CreateTripFields.DateStart
            )
        }
    }

    fun setOtherCountry(state: Boolean) {
        _formState.update {
            it.copy(
                otherCountry = state,
                errors = if (!state) it.errors - CreateTripFields.Country else it.errors
            )
        }
    }

    fun setCountry(country: String) {
        _formState.update {
            it.copy(
                country = country,
                errors = it.errors - CreateTripFields.Country
            )
        }
    }

    fun setCity(city: String) {
        _formState.update {
            it.copy(
                city = city,
                errors = it.errors - CreateTripFields.City
            )
        }
    }

    fun setDescription(description: String) {
        _formState.update {
            it.copy(
                description = description,
                errors = it.errors - CreateTripFields.Description
            )
        }
    }


    val goals = listOf(
        Pair("culture", "Культура"),
        Pair("adventure", "Приключения"),
        Pair("relaxation", "Отдых"),
        Pair("business", "Бизнес"),
        Pair("nature", "Природа"),
        Pair("gastronomy", "Гастрономия"),
        Pair("beach", "Пляжный отдых"),
        Pair("shopping", "Шопинг"),
        Pair("romantic", "Романтика"),
        Pair("wellness", "Оздоровление")
    )

    fun setGoal(goal: Pair<String, String>) {
        _formState.update {
            it.copy(
                goal = goal.first,
                goalV = goal.second,
                errors = it.errors - CreateTripFields.Goal
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
                transportV = transport.second,
                errors = it.errors - CreateTripFields.Transport
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

	fun addMember(info: UIUserBaseInfoModel) {
		_formState.update {
			it.copy(
				members = it.members + listOf(Pair(info.id, info.name)),
                errors = it.errors - CreateTripFields.Members
			)
		}
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

    fun submit() {
        viewModelScope.launch {
            val s = _formState.value
            val validation = validateTripFieldsUseCase.validate(
                dateStart = s.dateStart,
                dateEnd = s.dateEnd,
                otherCountry = s.otherCountry,
                country = s.country,
                city = s.city,
                description = s.description,
                goal = s.goal,
                goalV = s.goalV,
                transport = s.transport,
                transportV = s.transportV,
                members = s.members
            )

            if (!validation.isValid) {
                _formState.update { it.copy(errors = validation.errors) }
                return@launch
            }
            val membersIds = s.members.map { (lng, string) -> lng }
            val kidsAges = mutableListOf<Int>()
            repeat(s.countSmallChildren) {
                kidsAges.add(1)
            }
            repeat(s.countMediumChildren) {
                kidsAges.add(8)
            }
            if (s.countSmallChildren > 0) {
                kidsAges.add(14)
            }
            val createdTrip = createTripUseCase(
                TripCreate(
                    startDate = if (s.dateStart != null) s.dateStart else 0,
                    endDate = if (s.dateEnd != null) s.dateEnd else 0,
                    destinationType = if (!s.otherCountry) "domestic" else "international",
                    city = s.city ?: "",
                    country = s.country ?: "",
                    description = s.description ?: "",
                    purpose = s.goal ?: "",
                    transport = s.transport ?: "",
                    withKids = s.countBigChildren > 0 || s.countMediumChildren > 0 || s.countSmallChildren > 0,
                    withPets = s.pets,
                    kidsAges = kidsAges.toList(),
                    membersIds = membersIds,
                    autoGenerateTasks = true
                )
            )
        }

    }
}