package com.prod.t_tasks.presentation.screens.createTrip

import TTTextField
import TTTextFieldWithDropdown
import TTTextFieldWithSearch
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.prod.t_tasks.presentation.components.main_screen.CategoryBubble
import com.prod.t_tasks.presentation.components.standart.BigButton
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.timestampToDateWithYear
import com.prod.t_tasks.presentation.utils.ui.DateRangePickerModal
import com.prod.t_tasks.presentation.utils.ui.noRippleClickable
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateTripScreen(
	modifier: Modifier = Modifier,
	viewModel: CreateTripViewModel = koinViewModel()
) {
	val openCalendarModal = remember { mutableStateOf(false) }

	val formState by viewModel.formState.collectAsState()

	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(horizontal = Paddings.large)
			.verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Spacer(modifier = Modifier.height(Paddings.large))
		Text(
			modifier = Modifier.fillMaxWidth(0.7f),
			text = "Расскажите о путешествии вашей мечты",
			style = Typography.titleLarge,
			fontSize = 24.sp,
			textAlign = TextAlign.Center,
			color = MaterialTheme.colorScheme.onBackground
		)

		Spacer(modifier = Modifier.height(Paddings.large))

		TTTextField(
			onClick =  {
				openCalendarModal.value = true
			},
			value = formState . dateStart ?. let { start ->
				formState.dateEnd?.let { end ->
					"${
						timestampToDateWithYear(
							start
						)
					} - ${
						timestampToDateWithYear(end)
					}"
				}
			} ?: "",
			readOnly = true,
			onValueChange = {},
			label = "Выберите даты поездки",
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

		Spacer(modifier = Modifier.height(Paddings.small))

		TSwitchField(
			modifier = Modifier.padding(horizontal = 2.dp),
			name = "Вы едете в другую страну?",
			state = formState.otherCountry,
			onStateChange = viewModel::setOtherCountry
		)

		AnimatedVisibility(formState.otherCountry) {
			Spacer(modifier = Modifier.height(Paddings.small))
			TTTextField(
				value = formState.country ?: "",
				onValueChange = viewModel::setCountry,
				label = "Страна"
			)
		}


		Spacer(modifier = Modifier.height(Paddings.small))

		TTTextField(
			value = formState.city ?: "",
			onValueChange = viewModel::setCity,
			label = "Город"
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TTTextField(
			value = formState.description ?: "",
			onValueChange = viewModel::setCity,
			label = "Описание поездки",
			singleLine = false,
			maxLines = 5
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TTTextFieldWithDropdown(
			value = formState.goalV ?: "",
			onValueChange = {},
			label = "Цель поездки",
			dropdownItems = viewModel.goals,
			dropDownItem = {
				Text(it.second, style = Typography.labelLarge)
			},
			onDropdownItemSelected = viewModel::setGoal
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TTTextFieldWithDropdown(
			value = formState.transportV ?: "",
			onValueChange = {},
			label = "Транспорт",
			dropdownItems = viewModel.transports,
			dropDownItem = {
				Text(it.second, style = Typography.labelLarge)
			},
			onDropdownItemSelected = viewModel::setTransport
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TCounterField(
			name = "Кол-во детей до 3 лет",
			value = formState.countSmallChildren,
			onMinus = viewModel::downCountOfSmallChildren,
			onPlus = viewModel::upCountOfSmallChildren
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TCounterField(
			name = "Кол-во детей 3-7 лет",
			value = formState.countMediumChildren,
			onMinus = viewModel::downCountOfMediumChildren,
			onPlus = viewModel::upCountOfMediumChildren
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TCounterField(
			name = "Кол-во детей 7-12 лет",
			value = formState.countBigChildren,
			onMinus = viewModel::downCountOfBigChildren,
			onPlus = viewModel::upCountOfBigChildren
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		TSwitchField(
			modifier = Modifier.padding(horizontal = 2.dp),
			name = "Едут ли с вами питомцы?",
			state = formState.pets,
			onStateChange = viewModel::setPets
		)

		Spacer(modifier = Modifier.height(Paddings.small))

		Card(
			shape = Shapes.smallRoundedBox,
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.secondaryContainer,
				contentColor = MaterialTheme.colorScheme.onSecondaryContainer
			)
		) {
			Column(modifier = Modifier.padding(Paddings.large)) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Добавьте других участников",
						style = Typography.titleMedium,
						fontSize = 15.sp
					)
					Spacer(modifier = Modifier.width(Paddings.small))
					Icon(
						modifier = Modifier.size(18.dp),
						painter = painterResource(R.drawable.ic_group),
						contentDescription = null
					)
				}

				Spacer(modifier = Modifier.height(Paddings.medium))

				FlowRow(
					horizontalArrangement = Arrangement.spacedBy(Paddings.small),
					verticalArrangement = Arrangement.spacedBy(Paddings.small)
				) {
					formState.members.forEach {
						CategoryBubble(
							name = it.second,
							isSelected = false,
                            error = null
						) {}
					}
				}

				Spacer(modifier = Modifier.height(Paddings.medium))

				TTTextFieldWithSearch(
					value = viewModel.currentEmailValue.collectAsState().value,
					onValueChange = viewModel::searchEmailChanged,
					readOnly = false,
					label = "E-mail",
					dropdownItems = viewModel.foundUsers.collectAsState().value.also {
						Log.d("CreateTripScreen", it.toString())
					},
					dropDownItem = {
						Column {
							Text(it.email, style = Typography.titleMedium)
							Text(it.name, style = Typography.labelLarge)
						}
					},
					onDropdownItemSelected = viewModel::addMember,
					trailingIcon = {
						Icon(
							modifier = Modifier.size(24.dp),
							painter = painterResource(R.drawable.ic_magnifer),
							tint = MaterialTheme.colorScheme.onPrimary,
							contentDescription = null
						)
					}
				)
			}
		}

		Spacer(modifier = Modifier.height(Paddings.large))

		BigButton(onClick = {}, buttonText = "Поехали!", isLoading = false)

		Spacer(modifier = Modifier.height(Paddings.large))
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
fun TSwitchField(
	modifier: Modifier = Modifier,
	name: String,
	state: Boolean,
	onStateChange: (Boolean) -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = name,
			style = Typography.labelLarge,
			color = MaterialTheme.colorScheme.onBackground,
			fontSize = 14.sp
		)

		Switch(state, onCheckedChange = onStateChange)
	}
}


@Composable
fun TCounterField(
	modifier: Modifier = Modifier,
	name: String,
	value: Int,
	onPlus: () -> Unit,
	onMinus: () -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = name,
			style = Typography.labelLarge,
			color = MaterialTheme.colorScheme.onBackground,
			fontSize = 14.sp

		)

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(Paddings.small)
		) {
			Icon(
				modifier = Modifier
					.size(24.dp)
					.noRippleClickable(onMinus),
				painter = painterResource(R.drawable.ic_minus),
				tint = MaterialTheme.colorScheme.onBackground,
				contentDescription = "minus"
			)
			Text(
				modifier = Modifier.width(20.dp),
				text = value.toString(),
				textAlign = TextAlign.Center,
				style = Typography.titleMedium,
				color = MaterialTheme.colorScheme.onBackground,
				fontSize = 24.sp
			)
			Icon(
				modifier = Modifier
					.size(24.dp)
					.noRippleClickable(onPlus),
				painter = painterResource(R.drawable.ic_plus),
				tint = MaterialTheme.colorScheme.onBackground,
				contentDescription = "plus"
			)
		}
	}
}