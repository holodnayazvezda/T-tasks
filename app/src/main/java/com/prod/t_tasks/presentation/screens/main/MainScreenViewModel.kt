package com.prod.t_tasks.presentation.screens.main

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.prod.t_tasks.domain.model.TaskFilters
import com.prod.t_tasks.domain.usecase.tasks.LoadTasksTopicListUseCase
import com.prod.t_tasks.domain.usecase.tasks.LoadTasksUseCase
import com.prod.t_tasks.domain.usecase.tasks.SetFinishedStateToTaskUseCase
import com.prod.t_tasks.presentation.commonDataModels.UITaskTopicModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import com.prod.t_tasks.presentation.utils.convertGMTToSystemTimezone
import com.prod.t_tasks.presentation.utils.getStartOfTodayTimestamp
import com.prod.t_tasks.presentation.utils.timestampToDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class MainScreenViewModel(
	private val loadTasksUseCase: LoadTasksUseCase,
	private val loadTasksTopicsListUseCase: LoadTasksTopicListUseCase,
	private val setFinishedStateToTaskUseCase: SetFinishedStateToTaskUseCase
) : BaseViewModel() {

	var userChanged = false

	// Date filter
	private val defaultDateFilterState =
		getStartOfTodayTimestamp().let { Pair(it, it + 86400000) }


	private val dateState =
		mutableStateOf(defaultDateFilterState)

	val dateString = derivedStateOf {
		Log.d(
			"MainScreenViewModel",
			"deriving state <dateString>, defaultDateFilterState - $defaultDateFilterState"
		)
		when (dateState.value.first) {
			defaultDateFilterState.first -> "Сегодня"
			defaultDateFilterState.second -> "Завтра"
			else -> timestampToDate(dateState.value.first)
		} + "-" +
				when (dateState.value.second) {
					defaultDateFilterState.first -> "Сегодня"
					defaultDateFilterState.second -> "Завтра"
					else -> timestampToDate(dateState.value.second)
				}
	}

	fun setDate(dates: Pair<Long, Long>) {
		userChanged = true
		dateState.value =
			Pair(
				convertGMTToSystemTimezone(dates.first),
				convertGMTToSystemTimezone(dates.second)
			)

		Log.d("MainScreenViewModel", "updated dates ${dateState.value}")
	}

	// Other
	val onlyMyTasksState = mutableStateOf(true)

	val showFinished = mutableStateOf(false)

	// Topic filters

	val selectedTopicId = mutableStateOf<Int?>(null)

	val topicList = MutableUIStateFlow<List<UITaskTopicModel>>()

	fun loadTopicList() {
		loadTasksTopicsListUseCase().map { it -> it.map { it -> it.map { it.mapToUI() } } }
			.collectRequest(topicList)
	}

	fun selectTopic(id: Int) {
		if (selectedTopicId.value == id) {
			selectedTopicId.value = null
		} else {
			selectedTopicId.value = id
		}
	}

	// Tasks
	@OptIn(ExperimentalCoroutinesApi::class)
	val taskList = snapshotFlow {
		val dates = dateState.value
		TaskFilters(
			dateStart = dates.first,
			dateEnd = dates.second,
			topicId = selectedTopicId.value,
			onlySelf = onlyMyTasksState.value,
			showArchived = showFinished.value
		)
	}.flatMapLatest {
		loadTasksUseCase(it)
	}.map { it -> it.map { it.mapToUI() } }

	private val archiveWaitingTaskJobs = mutableStateMapOf<Long, Job>()

	val archiveWaitingTasksIds = derivedStateOf { archiveWaitingTaskJobs.keys }

	fun toggleTaskAsDone(tripId: Long, taskId: Long, currState: Boolean) {
		if (currState) {
			viewModelScope.launch {
				setFinishedStateToTaskUseCase(
					tripId = tripId,
					taskId = taskId,
					finishedState = false
				)
			}
		} else {
			if (taskId in archiveWaitingTasksIds.value) {
				archiveWaitingTaskJobs[taskId]?.let { job ->
					if (!job.isCompleted) {
						job.cancel()
					}
				}
				archiveWaitingTaskJobs.remove(taskId)
			} else {
				archiveWaitingTaskJobs[taskId] = viewModelScope.launch {
					delay(1000)
					setFinishedStateToTaskUseCase(
						tripId = tripId,
						taskId = taskId,
						finishedState = true
					)
				}.also {
					it.start()
				}
			}
		}
	}

	fun update() {
		loadTopicList()
	}

	init {
		update()
	}
}
