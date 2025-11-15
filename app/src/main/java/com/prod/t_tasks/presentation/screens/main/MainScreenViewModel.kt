package com.prod.t_tasks.presentation.screens.main

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.res.integerResource
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.prod.t_tasks.domain.model.TaskFilters
import com.prod.t_tasks.domain.usecase.tasks.LoadTasksUseCase
import com.prod.t_tasks.domain.usecase.tasks.MarkTaskAsDoneUseCase
import com.prod.t_tasks.presentation.commonDataModels.UITaskTopicModel
import com.prod.t_tasks.presentation.commonDataModels.mapToUI
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.base.BaseViewModel
import com.prod.t_tasks.presentation.utils.convertGMTToSystemTimezone
import com.prod.t_tasks.presentation.utils.getStartOfTodayTimestamp
import com.prod.t_tasks.presentation.utils.timestampToDate
import com.prod.t_tasks.presentation.utils.toggleItem
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
	private val markTaskAsDoneUseCase: MarkTaskAsDoneUseCase
) : BaseViewModel() {

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

	val selectedTopicIds = mutableStateSetOf<Int>()

	val topicList = MutableUIStateFlow<List<UITaskTopicModel>>()

	fun loadTopicList() {
		viewModelScope.launch {
			topicList.emit(
				UIState.Success(
					listOf(
						UITaskTopicModel(
							0, "Documents"
						),
						UITaskTopicModel(
							1, "Documents2"
						),
						UITaskTopicModel(
							2, "Documents3"
						),
						UITaskTopicModel(
							3, "Documents4"
						)
					)
				)
			)
		}
	}

	fun selectTopic(id: Int) {
		selectedTopicIds.toggleItem(id)
	}

	// Tasks
	@OptIn(ExperimentalCoroutinesApi::class)
	val taskList = snapshotFlow {
		val dates = dateState.value
		TaskFilters(
			dateStart = dates.first,
			dateEnd = dates.second,
			topicIds = selectedTopicIds.toList(),
			onlySelf = onlyMyTasksState.value
		)
	}.flatMapLatest {
		loadTasksUseCase(it)
	}.map { it -> it.map { it.mapToUI() } }

	private val archiveWaitingTaskJobs = mutableStateMapOf<Long, Job>()

	val archiveWaitingTasksIds = derivedStateOf { archiveWaitingTaskJobs.keys }

	fun toggleTaskAsDone(id: Long) {
		if (id in archiveWaitingTasksIds.value) {
			archiveWaitingTaskJobs[id]?.let { job ->
				if (!job.isCompleted) {
					job.cancel()
				}
			}
			archiveWaitingTaskJobs.remove(id)
		} else {
			archiveWaitingTaskJobs[id] = viewModelScope.launch {
				delay(1000)
				markTaskAsDoneUseCase()
			}.also {
				it.start()
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