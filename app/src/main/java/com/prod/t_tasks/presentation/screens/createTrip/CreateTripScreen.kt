package com.prod.t_tasks.presentation.screens.createTrip

import TTTextField
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.timestampToDate
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
			.padding(horizontal = 20.dp)
			.verticalScroll(rememberScrollState())
	) {
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = "Расскажите о путешествии вашей мечты",
			style = Typography.titleLarge,
			fontSize = 24.sp,
			color = MaterialTheme.colorScheme.onBackground
		)

		TTTextField(
			value = formState.dateStart?.let { start ->
				formState.dateEnd?.let { end ->
					"${
						timestampToDate(
							start
						)
					} - ${
						timestampToDate(end)
					}"
				}
			} ?: "",
			readOnly = true,
			onValueChange = {},
			label = "Выберите даты поездки",
			trailingIcon = {
				Icon(
					modifier = Modifier
						.size(24.dp)
						.noRippleClickable {
							openCalendarModal.value = true
						},
					painter = painterResource(
						R.drawable.ic_calendar
					),
					tint = MaterialTheme.colorScheme.onPrimary,
					contentDescription = null
				)
			}
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
}