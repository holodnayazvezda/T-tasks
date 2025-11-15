package com.prod.t_tasks.presentation.components.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
	onDismiss: () -> Unit,
	onSave: () -> Unit = onDismiss
) {
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
			onSave = onSave
		)
	}
}

@Composable
fun AddTaskContent(
	modifier: Modifier = Modifier,
	onSave: () -> Unit
) {
	val cs = MaterialTheme.colorScheme
	var taskType by remember { mutableStateOf("Поездка") }
	var title by remember { mutableStateOf("") }
	var whatToDo by remember { mutableStateOf("") }
	var whyNeed by remember { mutableStateOf("") }
	var deadline by remember { mutableStateOf("") }
	var forEveryone by remember { mutableStateOf(true) }
	var selectedCategory by remember { mutableStateOf("Документы") }

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

		TaskTypeDropdown(
			currentType = taskType,
			onTypeSelected = { taskType = it }
		)

		Spacer(Modifier.height(12.dp))
		TTTextField(
			value = title,
			onValueChange = { title = it },
			label = "Название",
			modifier = Modifier.fillMaxWidth()
		)

		Spacer(Modifier.height(12.dp))
		TTTextField(
			value = whatToDo,
			onValueChange = { whatToDo = it },
			label = "Что нужно сделать",
			modifier = Modifier.fillMaxWidth(),
			singleLine = false
		)

		Spacer(Modifier.height(12.dp))
		TTTextField(
			value = whyNeed,
			onValueChange = { whyNeed = it },
			label = "Зачем это нужно",
			modifier = Modifier.fillMaxWidth(),
			singleLine = false
		)

		Spacer(Modifier.height(12.dp))
		TTTextField(
			value = deadline,
			onValueChange = { deadline = it },
			label = "Дедлайн",
			modifier = Modifier.fillMaxWidth(),
			trailingIcon = {
				Icon(
					imageVector = Icons.Default.CalendarToday,
					contentDescription = null,
					tint = cs.onSurfaceVariant
				)
			}
		)

		Spacer(Modifier.height(20.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "Задачу должен\nвыполнить каждый",
				color = cs.onSurface,
				fontSize = 13.sp,
				modifier = Modifier.weight(1f)
			)

			CustomSwitch(
				checked = forEveryone,
				onCheckedChange = { forEveryone = it }
			)
		}

		Spacer(Modifier.height(24.dp))
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(16.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				listOf("Документы", "Вещи", "Другое1").forEach { text ->
					CategoryChip(
						text = text,
						selected = selectedCategory == text,
						onClick = { selectedCategory = text }
					)
				}
			}
			Spacer(Modifier.height(10.dp))
			CategoryChip(
				text = "Другое2",
				selected = selectedCategory == "Другое2",
				onClick = { selectedCategory = "Другое2" }
			)
		}

		Spacer(Modifier.height(28.dp))
		Button(
			modifier = Modifier
				.fillMaxWidth()
				.height(56.dp),
			onClick = onSave,
			shape = RoundedCornerShape(18.dp)
		) {
			Text(text = "Сохранить", fontSize = 18.sp)
		}
		Spacer(Modifier.height(12.dp))
		TextButton(
			onClick = onSave,
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Отмена")
		}
	}
}

@Composable
private fun TaskTypeDropdown(
	currentType: String,
	onTypeSelected: (String) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }
	val options = listOf("Поездка", "Задача", "Событие")
	Column {
		Button(
			onClick = { expanded = true },
			modifier = Modifier
				.fillMaxWidth()
				.height(56.dp),
			shape = RoundedCornerShape(18.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(text = currentType)
				Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
			}
		}

		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false }
		) {
			options.forEach { option ->
				DropdownMenuItem(
					text = { Text(option) },
					onClick = {
						onTypeSelected(option)
						expanded = false
					}
				)
			}
		}
	}
}

@Composable
private fun CustomSwitch(
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit
) {
	Switch(checked = checked, onCheckedChange = onCheckedChange)
}

@Composable
private fun CategoryChip(
	text: String,
	selected: Boolean,
	onClick: () -> Unit
) {
	val cs = MaterialTheme.colorScheme
	AssistChip(
		onClick = onClick,
		label = { Text(text = text) },
		colors = AssistChipDefaults.assistChipColors(
			containerColor = if (selected) cs.primaryContainer else cs.surfaceVariant,
			labelColor = if (selected) cs.onPrimaryContainer else cs.onSurfaceVariant
		)
	)
}
