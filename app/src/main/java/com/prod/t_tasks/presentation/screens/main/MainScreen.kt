package com.prod.t_tasks.presentation.screens.main

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.components.mainscreen.BubbledCategoryFilters
import com.prod.t_tasks.presentation.components.mainscreen.MainScreenFilters
import com.prod.t_tasks.presentation.components.mainscreen.TaskCard
import com.prod.t_tasks.presentation.components.standart.TopLogo
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope
import com.prod.t_tasks.presentation.utils.ui.DateRangePickerModal
import org.koin.androidx.compose.koinViewModel


@Composable
fun ErrorCollectorScope.MainScreen(
	modifier: Modifier = Modifier,
	viewModel: MainScreenViewModel = koinViewModel()
) {
    val openCalendarModal = remember { mutableStateOf(false) }

	Column(
		modifier = modifier
			.padding(horizontal = Paddings.medium)
			.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
	) {
		Spacer(modifier = Modifier.height(Paddings.medium))

		TopLogo()

		Spacer(modifier = Modifier.height(Paddings.medium))

		val tasks = viewModel.taskList.collectAsLazyPagingItems()

		if (tasks.loadState.hasError) {
			Text("Error")
		} else if (tasks.loadState.append.endOfPaginationReached && tasks.itemCount == 0) {
			Text("Empty")
		} else {
            MainScreenFilters(viewModel = viewModel) {
				openCalendarModal.value = true
			}
			Spacer(modifier = Modifier.height(Paddings.medium))
			viewModel.topicList.FoldUIStateWithGlobalCallbacks { topics ->
				BubbledCategoryFilters(
					categories = topics,
					selectedList = viewModel.selectedTopicIds.toList()
				) { categoryId ->
					viewModel.selectTopic(categoryId)
				}
			}
			Spacer(modifier = Modifier.height(Paddings.medium))
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
							viewModel.toggleTaskAsDone(id = it.id)
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
}