package com.prod.t_tasks.presentation.components.mainscreen.addTaskBottomSheet

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.prod.t_tasks.domain.model.CreateTask
import com.prod.t_tasks.domain.model.trip.TripsTask
import com.prod.t_tasks.domain.usecase.tasks.CreateTaskFields
import com.prod.t_tasks.domain.usecase.tasks.CreateTaskUseCase
import com.prod.t_tasks.domain.usecase.tasks.LoadTasksTopicListUseCase
import com.prod.t_tasks.domain.usecase.tasks.ValidateTaskFieldsUseCase
import com.prod.t_tasks.domain.usecase.trips.GetAllUserTripsUseCase
import com.prod.t_tasks.presentation.commonDataModels.UITaskTopicModel
import com.prod.t_tasks.presentation.commonDataModels.UITripModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import com.prod.t_tasks.presentation.utils.convertGMTToSystemTimezone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.String

data class TaskCreateFormState(
	val tripId: Long? = null,
	val tripName: String = "",
	val taskName: String = "",
	val whatNeedToDo: String = "",
	val reason: String = "",
	val deadline: Long? = null,
	val commonTask: Boolean = false,
	val selectedCategories: List<Int> = emptyList(),
    val errors: Map<CreateTaskFields, String> = emptyMap()
)

@KoinViewModel
class AddTaskBottomSheetViewModel(
	private val loadTasksTopicsListUseCase: LoadTasksTopicListUseCase,
    private val validateTaskFieldsUseCase: ValidateTaskFieldsUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
	private val getAllUserTripsUseCase: GetAllUserTripsUseCase
) : BaseViewModel() {

	val userTrips = MutableUIStateFlow<List<UITripModel>>()

	val topicList = MutableUIStateFlow<List<UITaskTopicModel>>()

	fun loadTopicList() {
		loadTasksTopicsListUseCase().map { it -> it.map { it -> it.map { it.mapToUI() } } }
			.collectRequest(topicList)
	}

	fun loadUserTrips() {
		getAllUserTripsUseCase().map { it.map { it.map { it.mapToUI() } } }
			.collectRequest(userTrips)
	}

	private val _formState = MutableStateFlow(TaskCreateFormState())
	val formState: StateFlow<TaskCreateFormState> = _formState
    private val _createTaskState = MutableUIStateFlow<TripsTask>()
    val createTaskState: StateFlow<UIState<TripsTask>> = _createTaskState


	fun setTrip(tripInfo: UITripModel) {
		_formState.update {
			it.copy(
				tripId = tripInfo.id,
				tripName = tripInfo.placeName,
                errors = it.errors - CreateTaskFields.Trip
			)
		}
	}

	fun setTaskName(name: String) {
		_formState.update {
			it.copy(
				taskName = name,
                errors = it.errors - CreateTaskFields.Name
			)
		}
	}

	fun setWhatNeedToDo(text: String) {
		_formState.update {
			it.copy(
				whatNeedToDo = text,
                errors = it.errors - CreateTaskFields.WhatNeedToDo
			)
		}
	}

	fun setReason(text: String) {
		_formState.update {
			it.copy(
				reason = text,
                errors = it.errors - CreateTaskFields.Reason
			)
		}
	}

	fun setDeadline(date: Long) {
		_formState.update {
			it.copy(
				deadline = convertGMTToSystemTimezone(date),
                errors = it.errors - CreateTaskFields.Deadline
			)
		}
	}

	fun setIsCommonTaskState(state: Boolean) {
		_formState.update {
			it.copy(
				commonTask = state
			)
		}
	}

	fun toggleCategory(id: Int) {
		Log.d("AddTaskBottomSheetViewModel", "toggle category $id")
		if (id in _formState.value.selectedCategories) {
			Log.d("AddTaskBottomSheetViewModel", "exists - deleting")
			_formState.update {
				it.copy(
					selectedCategories = it.selectedCategories.minus(id),
				)
			}
		} else {
			Log.d("AddTaskBottomSheetViewModel", "adding")
			_formState.update {
				it.copy(
					selectedCategories = it.selectedCategories.plus(id),
                    errors = it.errors - CreateTaskFields.Categories
				)
			}
		}
	}

    fun submit() {
        viewModelScope.launch {
            val validation = validateTaskFieldsUseCase.validate(
                tripId = _formState.value.tripId,
                taskName = _formState.value.taskName,
                whatNeedToDo = _formState.value.whatNeedToDo,
                reason = _formState.value.reason,
                deadline = _formState.value.deadline,
                selectedCategories = _formState.value.selectedCategories
            )
            if (!validation.isValid) {
                _formState.update { it.copy(errors = validation.errors) }
                return@launch
            }

            _createTaskState.emit(UIState.Loading())
            val createdTask = createTaskUseCase(
                CreateTask(
                    tripId = _formState.value.tripId ?: 0,
                    title = _formState.value.taskName,
                    description = _formState.value.whatNeedToDo,
                    dueTime = _formState.value.deadline ?: 0,
                    status = "pending",
                    categoriesIds = _formState.value.selectedCategories,
                    isCommon = _formState.value.commonTask
                )
            )
        }
    }

    fun reset() {
        _formState.update {
            it.copy(
                tripId = 0,
                tripName = "",
                taskName = "",
                whatNeedToDo = "",
                reason = "",
                deadline = null,
                commonTask = false,
                selectedCategories = emptyList(),
                errors = emptyMap()
            )
        }
    }

	fun update() {
		loadUserTrips()
		loadTopicList()
	}

	init {
		update()
	}
}