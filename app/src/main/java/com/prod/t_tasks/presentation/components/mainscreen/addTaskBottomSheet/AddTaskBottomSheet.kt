package com.prod.t_tasks.presentation.components.mainscreen.addTaskBottomSheet

import TTTextField
import TTTextFieldWithDropdown
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.domain.usecase.tasks.CreateTaskFields
import com.prod.t_tasks.presentation.components.main_screen.CategoryBubble
import com.prod.t_tasks.presentation.components.standart.MediumButton
import com.prod.t_tasks.presentation.screens.createTrip.TSwitchField
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope
import com.prod.t_tasks.presentation.utils.UIState
import com.prod.t_tasks.presentation.utils.timestampToDateWithYear
import com.prod.t_tasks.presentation.utils.ui.DatePickerModal
import com.prod.t_tasks.presentation.utils.ui.noRippleClickable
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorCollectorScope.AddTaskBottomSheet(
	sheetState: SheetState,
	onDismiss: () -> Unit,
	viewModel: AddTaskBottomSheetViewModel = koinViewModel()
) {

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		dragHandle = {},
		shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
	) {
		AddTaskContent(
			modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding(),
			onDismiss = onDismiss,
			viewModel = viewModel
		)
	}
}

@Composable
fun ErrorCollectorScope.AddTaskContent(
	modifier: Modifier = Modifier,
	onDismiss: () -> Unit,
	viewModel: AddTaskBottomSheetViewModel
) {
	val cs = MaterialTheme.colorScheme

	val formState by viewModel.formState.collectAsState()
    val createTaskState by viewModel.createTaskState.collectAsState()

	val openCalendarModal = remember { mutableStateOf(false) }
    val errorCategories = formState.errors[CreateTaskFields.Categories]


	Column(
		modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "Добавление задачи",
			color = cs.onSurface,
			fontSize = 22.sp,
			textAlign = TextAlign.Center,
			modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 8.dp)
		)

		TTTextFieldWithDropdown(
			value = formState.tripName,
			onValueChange = {},
			label = "Выберите поездку",
			dropdownItems = viewModel.userTrips.collectAsValueStateWithCallbacks().value
				?: emptyList(),
			dropDownItem = {
				Column {
					Text(it.placeName, style = Typography.titleMedium)
					Text(it.dateRangeString, style = Typography.labelLarge)
				}
			},
			onDropdownItemSelected = viewModel::setTrip,
            error = formState.errors[CreateTaskFields.Trip]
		)

		Spacer(Modifier.height(Paddings.medium))
		TTTextField(
			value = formState.taskName,
			onValueChange = viewModel::setTaskName,
			label = "Название",
            error = formState.errors[CreateTaskFields.Name]
		)

		Spacer(Modifier.height(Paddings.medium))

		TTTextField(
			value = formState.whatNeedToDo,
			onValueChange = viewModel::setWhatNeedToDo,
			label = "Что нужно сделать",
			singleLine = false,
			maxLines = 5,
            error = formState.errors[CreateTaskFields.WhatNeedToDo]
		)

		Spacer(Modifier.height(Paddings.medium))
		TTTextField(
			value = formState.reason,
			onValueChange = viewModel::setReason,
			label = "Зачем это нужно",
			singleLine = false,
			maxLines = 5,
            error = formState.errors[CreateTaskFields.Reason]
		)

		Spacer(Modifier.height(Paddings.medium))

		TTTextField(
			onClick = {
				openCalendarModal.value = true
			},
			value = formState.deadline?.let { timestampToDateWithYear(it) } ?: "",
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
			},
            error = formState.errors[CreateTaskFields.Deadline]
		)

		Spacer(Modifier.height(Paddings.large))

		TSwitchField(
			modifier = Modifier.padding(horizontal = 2.dp),
			name = "Должны выполнить все",
			state = formState.commonTask,
			onStateChange = viewModel::setIsCommonTaskState
		)

		Spacer(modifier = Modifier.height(Paddings.large))

		viewModel.topicList.FoldUIStateWithGlobalCallbacks {
            if (errorCategories != null) {
                Text(
                    text = errorCategories,
                    style = typography.labelLarge,
                    fontSize = 14.sp,
                    color = colorScheme.error
                )
                Spacer(Modifier.height(5.dp))
            }
			FlowRow(
				horizontalArrangement = Arrangement.spacedBy(Paddings.small),
				verticalArrangement = Arrangement.spacedBy(Paddings.small)
			) {
				it.forEach { topic ->
					CategoryBubble(
						name = topic.name,
						isSelected = topic.id in formState.selectedCategories,
                        error = formState.errors[CreateTaskFields.Categories]
					) {
						viewModel.toggleCategory(topic.id)
					}
				}
			}
		}

		Spacer(modifier = Modifier.height(Paddings.large))

		MediumButton(
            onClick = {
                onDismiss()
                viewModel.submit()
            },
            buttonText = "Сохранить",
            isLoading = createTaskState is UIState.Loading
        )

		Spacer(Modifier.height(12.dp))

		TextButton(
			onClick = {
                onDismiss()
                viewModel.reset()
              },
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = "Отмена",
				style = Typography.titleMedium,
				fontSize = 16.sp
			)
		}
	}
	AnimatedVisibility(openCalendarModal.value) {
		DatePickerModal({
			Log.d("DatePicker", it.toString())
			it?.let { date ->
				viewModel.setDeadline(date)
				openCalendarModal.value = false
			}
		}) {
			openCalendarModal.value = false
		}
	}
}