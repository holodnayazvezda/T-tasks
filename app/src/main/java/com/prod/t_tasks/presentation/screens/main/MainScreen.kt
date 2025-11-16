package com.prod.t_tasks.presentation.screens.main

import TTTextField
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.prod.t_tasks.R
import com.prod.t_tasks.domain.usecase.tasks.CreateTaskFields
import com.prod.t_tasks.presentation.commonDataModels.UITaskModel
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
import com.prod.t_tasks.presentation.utils.timestampToDateWithYear
import com.prod.t_tasks.presentation.utils.ui.DatePickerModal
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

	val selectedTask = remember { mutableStateOf<UITaskModel?>(null) }

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
								onClick = {
									selectedTask.value = it
								},
								taskInfo = it,
								isArchiveWaiting = it.id in viewModel.archiveWaitingTasksIds.value
							) {
								viewModel.toggleTaskAsDone(
									tripId = it.tripId,
									taskId = it.id,
									currState = it.archived
								)
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

	val cs = MaterialTheme.colorScheme

	val viewSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	if (selectedTask.value != null) {

		val openCalendarModal2 = remember { mutableStateOf(false) }

		ModalBottomSheet(
			onDismissRequest = {
				selectedTask.value = null
			},
			sheetState = viewSheetState,
			dragHandle = {},
			shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
		) {
			Column(
				modifier = Modifier
					.padding(horizontal = 24.dp, vertical = 16.dp)
					.verticalScroll(rememberScrollState()),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Просмотр задачи",
					color = cs.onSurface,
					style = Typography.titleMedium,
					fontSize = 22.sp,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 24.dp, top = 8.dp)
				)

				Spacer(modifier = Modifier.height(Paddings.medium))

				Text(
					text = selectedTask.value!!.name,
					color = cs.onSurface,
					style = Typography.titleMedium,
					fontSize = 20.sp,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 24.dp, top = 8.dp)
				)

				Spacer(modifier = Modifier.height(Paddings.medium))


				Text(
					text = "Что нужно сделать",
					color = cs.onSurface,
					style = Typography.titleMedium,
					fontSize = 18.sp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 24.dp, top = 8.dp)
				)

				Spacer(modifier = Modifier.height(Paddings.small))

				Text(
					text = selectedTask.value!!.whatNeedToDo,
					color = cs.onSurface,
					style = Typography.labelLarge,
					fontSize = 16.sp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 24.dp, top = 8.dp)
				)

				Spacer(modifier = Modifier.height(Paddings.medium))

				Text(
					text = "Для чего",
					color = cs.onSurface,
					style = Typography.titleMedium,
					fontSize = 18.sp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 24.dp, top = 8.dp)
				)

				Spacer(modifier = Modifier.height(Paddings.small))

				Text(
					text = selectedTask.value!!.reason,
					color = cs.onSurface,
					style = Typography.labelLarge,
					fontSize = 16.sp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 24.dp, top = 8.dp)
				)

				Spacer(modifier = Modifier.height(Paddings.large))

				TTTextField(
					onClick = {
						openCalendarModal2.value = true
					},
					value = timestampToDateWithYear(selectedTask.value!!.deadline),
					readOnly = true,
					onValueChange = {},
					label = "Дедлайн",
					trailingIcon = {
						Icon(
							modifier = Modifier
								.size(24.dp),
							painter = painterResource(
								R.drawable.ic_calendar
							),
							tint = MaterialTheme.colorScheme.onPrimary,
							contentDescription = null
						)
					}
				)
			}
		}

		AnimatedVisibility(openCalendarModal2.value) {
			DatePickerModal({
				Log.d("DatePicker", it.toString())
				it?.let { date ->
					viewModel.changeTaskDeadline(selectedTask.value, date)
					selectedTask.value = null
					openCalendarModal.value = false
				}
			}) {
				openCalendarModal.value = false
			}
		}
	}
}


