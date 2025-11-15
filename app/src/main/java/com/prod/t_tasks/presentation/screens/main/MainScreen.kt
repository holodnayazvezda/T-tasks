package com.prod.t_tasks.presentation.screens.main

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.commonDataModels.UITaskModel
import com.prod.t_tasks.presentation.commonDataModels.UITaskTopicModel
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope
import com.prod.t_tasks.presentation.utils.ui.DateRangePickerModal
import com.prod.t_tasks.presentation.utils.ui.clickable
import com.prod.t_tasks.presentation.utils.ui.noRippleClickable
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
			.fillMaxSize()
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
			Filters(viewModel = viewModel) {
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

@Composable
fun TopLogo(modifier: Modifier = Modifier) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Image(
			modifier = Modifier.size(100.dp),
			painter = painterResource(R.drawable.app_logo_dark_font),
			contentDescription = "App logo"
		)

		Spacer(modifier = Modifier.width(Paddings.medium))
		Text(
			text = stringResource(R.string.app_name),
			style = MaterialTheme.typography.titleLarge,
			fontSize = 48.sp
		)
	}
}

@Composable
fun Filters(
	modifier: Modifier = Modifier,
	viewModel: MainScreenViewModel,
	openCalendarModel: () -> Unit
) {
	Column {

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					MaterialTheme.colorScheme.surfaceVariant,
					shape = Shapes.verySmallRoundedBox
				)
				.clip(Shapes.verySmallRoundedBox)
				.clickable(
					rippleColor = MaterialTheme.colorScheme.primary,
					onClick = openCalendarModel
				)
				.padding(Paddings.small),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(
				Paddings.small,
				Alignment.CenterHorizontally
			)
		) {
			Text(
				text = viewModel.dateString.value,
				style = Typography.labelLarge,
				fontSize = 16.sp,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)

			Icon(
				modifier = Modifier.size(18.dp),
				painter = painterResource(R.drawable.ic_calendar),
				tint = MaterialTheme.colorScheme.onSurfaceVariant,
				contentDescription = "calendar"
			)
		}
		Spacer(modifier = Modifier.height(Paddings.small))
		Row(
			modifier
				.fillMaxWidth()
				.height(IntrinsicSize.Min),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(
				Paddings.small,
				Alignment.CenterHorizontally
			)
		) {
			@Composable
			fun clickableFilterCardModifier(onClick: () -> Unit) = Modifier
				.weight(1f)
				.fillMaxHeight()
				.background(
					MaterialTheme.colorScheme.surfaceVariant,
					shape = Shapes.verySmallRoundedBox
				)
				.clip(Shapes.verySmallRoundedBox)
				.clickable(
					rippleColor = MaterialTheme.colorScheme.primary,
					onClick = onClick
				)
				.padding(Paddings.small)


			Row(
				modifier = clickableFilterCardModifier {
					viewModel.onlyMyTasksState.value = viewModel.onlyMyTasksState.value.not()
				},
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(
					Paddings.small,
					Alignment.CenterHorizontally
				)
			) {
				Text(
					text = "Только мои задачи",
					style = Typography.labelLarge,
					fontSize = 16.sp,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				TCheckBox(
					modifier = Modifier.size(15.dp),
					checked = viewModel.onlyMyTasksState.value,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}

			Row(
				modifier = clickableFilterCardModifier {
					viewModel.showFinished.value = viewModel.showFinished.value.not()
				},
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(
					Paddings.small,
					Alignment.CenterHorizontally
				)
			) {
				Text(
					text = "Завершенные",
					style = Typography.labelLarge,
					fontSize = 16.sp,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				TCheckBox(
					modifier = Modifier.size(15.dp),
					checked = viewModel.showFinished.value,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}

@Composable
fun BubbledCategoryFilters(
	modifier: Modifier = Modifier,
	categories: List<UITaskTopicModel>,
	selectedList: List<Int>,
	onCategoryToggle: (Int) -> Unit
) {
	val widthScreenDp = LocalConfiguration.current.screenWidthDp
	LazyRow(
		modifier = modifier.requiredWidth(widthScreenDp.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(
			Paddings.small
		)
	) {
		item {
			Spacer(modifier = Modifier.width(Paddings.medium - Paddings.small))
		}

		items(categories) { category ->
			CategoryBubble(
				name = category.name,
				isSelected = category.id in selectedList
			) {
				onCategoryToggle(category.id)
			}
		}

		item {
			Spacer(modifier = Modifier.width(Paddings.medium - Paddings.small))
		}
	}
}

@Composable
fun CategoryBubble(
	modifier: Modifier = Modifier,
	name: String,
	isSelected: Boolean,
	onClick: () -> Unit
) {
	val bubbleColor =
		animateColorAsState(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
	val bubbleBorderColor =
		animateColorAsState(if (isSelected) MaterialTheme.colorScheme.primaryFixed else MaterialTheme.colorScheme.secondaryFixed)
	val textColor =
		animateColorAsState(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary)
	Text(
		modifier = modifier
			.background(
				color = bubbleColor.value,
				shape = Shapes.smallRoundedBox
			)
			.border(
				width = 1.dp,
				color = bubbleBorderColor.value,
				shape = Shapes.smallRoundedBox
			)
			.clip(Shapes.smallRoundedBox)
			.clickable(MaterialTheme.colorScheme.primary, onClick = onClick)
			.padding(horizontal = Paddings.small, vertical = Paddings.verySmall),
		text = name,
		style = Typography.labelLarge,
		fontSize = 16.sp,
		color = textColor.value
	)
}

@Composable
fun TaskCard(
	modifier: Modifier = Modifier,
	taskInfo: UITaskModel,
	isArchiveWaiting: Boolean,
	onArchiveToggle: () -> Unit
) {
	Card(
		modifier = modifier
			.then(if (isArchiveWaiting) Modifier.alpha(0.3f) else Modifier),
		shape = Shapes.smallRoundedBox,
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.primaryContainer,
			contentColor = MaterialTheme.colorScheme.onPrimaryContainer
		),
		onClick = {}
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(
					Paddings.small
				),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column {
				Row {
					Icon(
						modifier = Modifier.size(20.dp),
						painter = painterResource(R.drawable.ic_documents),
						contentDescription = null
					)
					Spacer(modifier = Modifier.width(Paddings.small))

					Text(text = taskInfo.name, style = Typography.titleMedium, fontSize = 16.sp)
				}
				Spacer(modifier = Modifier.height(Paddings.small))

				Row(
					modifier =
						Modifier
							.background(
								MaterialTheme.colorScheme.secondaryContainer,
								shape = Shapes.smallRoundedBox
							)
							.padding(Paddings.verySmall)
				) {
					Text(
						text = if (taskInfo.common) "Общая задача" else "Индивидуальная задача",
						style = Typography.labelLarge,
						fontSize = 14.sp
					)
				}
			}
			Icon(
				modifier = Modifier
					.size(32.dp)
					.noRippleClickable {
						onArchiveToggle()
					},
				painter = painterResource(if (isArchiveWaiting || taskInfo.archived) R.drawable.ic_flag_filled else R.drawable.ic_flag_unfilled),
				contentDescription = "Mark as done"
			)
		}
	}
}

@Composable
fun TCheckBox(modifier: Modifier = Modifier, checked: Boolean, color: Color) {
	Box(
		modifier = modifier
			.background(Color.Transparent)
			.border(width = 1.dp, color = color, shape = Shapes.verySmallRoundedBox)
	) {
		AnimatedContent(checked) {
			if (it) {
				Icon(
					modifier = Modifier
						.fillMaxSize()
						.padding(2.dp),
					painter = painterResource(R.drawable.ic_checkmark),
					tint = color,
					contentDescription = "checkmark"
				)
			}
		}
	}
}