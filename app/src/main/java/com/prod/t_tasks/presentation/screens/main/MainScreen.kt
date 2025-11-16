package com.prod.t_tasks.presentation.screens.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.prod.t_tasks.presentation.components.main_screen.BubbledCategoryFilters
import com.prod.t_tasks.presentation.components.main_screen.MainScreenFilters
import com.prod.t_tasks.presentation.components.main_screen.TaskCard
import com.prod.t_tasks.presentation.components.mainscreen.addTaskBottomSheet.AddTaskBottomSheet
import com.prod.t_tasks.presentation.components.standart.BigButton
import com.prod.t_tasks.presentation.components.standart.TTFloatingActionButton
import com.prod.t_tasks.presentation.components.standart.TopLogo
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope
import com.prod.t_tasks.presentation.utils.ui.DateRangePickerModal
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorCollectorScope.MainScreen(
	modifier: Modifier = Modifier,
	viewModel: MainScreenViewModel = koinViewModel()
) {
	val openCalendarModal = remember { mutableStateOf(false) }
	val openTaskAddSheet = remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	val tasks = viewModel.taskList.collectAsLazyPagingItems()

	Box(
		modifier = modifier
			.fillMaxSize()
			.padding(horizontal = Paddings.large),
		contentAlignment = Alignment.BottomCenter
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(Paddings.large))
			TopLogo()
			Spacer(modifier = Modifier.height(Paddings.large))

			MainScreenFilters(viewModel = viewModel) {
				openCalendarModal.value = true
			}

			Spacer(modifier = Modifier.height(Paddings.large))

			viewModel.topicList.FoldUIStateWithGlobalCallbacks { topics ->
				BubbledCategoryFilters(
					categories = topics,
					selectedItemId = viewModel.selectedTopicId.value ?: -1
				) { categoryId ->
					viewModel.selectTopic(categoryId)
				}
			}
			Spacer(modifier = Modifier.height(Paddings.large))

			if (tasks.loadState.hasError) {
				Text(
					"Упс, кажется что-то пошло не так :(\nНо мы уже со всем разбираемся!",
					style = Typography.titleMedium,
					textAlign = TextAlign.Center,
					fontSize = 18.sp,
					color = MaterialTheme.colorScheme.error
				)
			} else if (tasks.loadState.append.endOfPaginationReached && tasks.itemCount == 0) {
				Spacer(modifier = Modifier.weight(1f))

				Text(
					"Сделал дело - гуляй смело\nСамое время запланировать новую поездку",
					style = Typography.titleMedium,
					textAlign = TextAlign.Center,
					fontSize = 18.sp,
					color = MaterialTheme.colorScheme.onBackground
				)
				Spacer(modifier = Modifier.height(Paddings.large))
				BigButton(buttonText = "Начать", onClick = {

				}, isLoading = false)

				Spacer(modifier = Modifier.weight(3f))

			} else {
				LazyColumn(
					verticalArrangement = Arrangement.spacedBy(Paddings.small),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					items(tasks.itemCount) { it ->
						val task = tasks[it]
						task?.let {
							TaskCard(
								taskInfo = it,
								isArchiveWaiting = it.id in viewModel.archiveWaitingTasksIds.value
							) {
								viewModel.toggleTaskAsDone(tripId = it.tripId, taskId = it.id, currState = it.archived)
								tasks.refresh()
							}
						}
					}

					item {
						if (!tasks.loadState.append.endOfPaginationReached) {
							CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
						}
					}
				}
			}
		}

		TTFloatingActionButton(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.padding(bottom = Paddings.medium),
			onClick = {
				openTaskAddSheet.value = true
			},
			text = "Добавить задачу"
		)
	}


	AnimatedVisibility(openCalendarModal.value) {
		DateRangePickerModal({
			Log.d("DatePicker", it.toString())
			if (it.first != null && it.second != null) {
				viewModel.setDate(Pair(it.first!!, it.second!!))
				openCalendarModal.value = false
			}
		}) {
			openCalendarModal.value = false
		}
	}

	if (openTaskAddSheet.value) {
		AddTaskBottomSheet(
			sheetState = sheetState,
			onDismiss = {
				openTaskAddSheet.value = false
			}
		)
	}
}
