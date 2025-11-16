package com.prod.t_tasks.presentation.components.mainscreen.addTaskBottomSheet

import android.util.Log
import com.prod.t_tasks.domain.usecase.tasks.LoadTasksTopicListUseCase
import com.prod.t_tasks.domain.usecase.trips.GetAllUserTripsUseCase
import com.prod.t_tasks.presentation.commonDataModels.UITaskTopicModel
import com.prod.t_tasks.presentation.commonDataModels.UITripModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import com.prod.t_tasks.presentation.utils.convertGMTToSystemTimezone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

data class TaskCreateFormState(
	val tripId: Long? = null,
	val tripName: String = "",
	val taskName: String = "",
	val whatNeedToDo: String = "",
	val reason: String = "",
	val deadline: Long? = null,
	val commonTask: Boolean = false,
	val selectedCategories: List<Int> = emptyList()
)

@KoinViewModel
class AddTaskBottomSheetViewModel(
	private val loadTasksTopicsListUseCase: LoadTasksTopicListUseCase,
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


	fun setTrip(tripInfo: UITripModel) {
		_formState.update {
			it.copy(
				tripId = tripInfo.id,
				tripName = tripInfo.placeName
			)
		}
	}

	fun setTaskName(name: String) {
		_formState.update {
			it.copy(
				taskName = name
			)
		}
	}

	fun setWhatNeedToDo(text: String) {
		_formState.update {
			it.copy(
				whatNeedToDo = text
			)
		}
	}

	fun setReason(text: String) {
		_formState.update {
			it.copy(
				reason = text
			)
		}
	}

	fun setDeadline(date: Long) {
		_formState.update {
			it.copy(
				deadline = convertGMTToSystemTimezone(date)
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
					selectedCategories = it.selectedCategories.minus(id)
				)
			}
		} else {
			Log.d("AddTaskBottomSheetViewModel", "adding")
			_formState.update {
				it.copy(
					selectedCategories = it.selectedCategories.plus(id)
				)
			}
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