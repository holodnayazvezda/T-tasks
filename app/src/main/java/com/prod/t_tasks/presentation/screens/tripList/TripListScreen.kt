package com.prod.t_tasks.presentation.screens.tripList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.commonDataModels.UITripModel
import com.prod.t_tasks.presentation.components.standart.BigButton
import com.prod.t_tasks.presentation.components.standart.TTFloatingActionButton
import com.prod.t_tasks.presentation.components.standart.TopLogo
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope
import org.koin.androidx.compose.koinViewModel


@Composable
fun ErrorCollectorScope.TripListScreen(
	modifier: Modifier = Modifier,
	navigateToCreateTrip: () -> Unit,
	viewModel: TripListScreenViewModel = koinViewModel()
) {
	Box(
		modifier = modifier
			.padding(horizontal = Paddings.large)
			.fillMaxSize(),
		contentAlignment = Alignment.BottomCenter
	) {
		val tasksIsNotEmpty =
			viewModel.tripsList.value.isSuccess && viewModel.tripsList.value.getOrNull()!!
				.isNotEmpty()

		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(Paddings.large))
			TopLogo()
			Spacer(modifier = Modifier.height(Paddings.large))

			if (tasksIsNotEmpty) {
				Text(
					modifier = Modifier.fillMaxWidth(),
					text = "Ваши поездки",
					style = Typography.titleLarge,
					textAlign = TextAlign.Center,
					fontSize = 32.sp,
					color = MaterialTheme.colorScheme.onBackground
				)
				Spacer(modifier = Modifier.height(Paddings.large))
			}
			viewModel.tripsList.FoldUIStateWithGlobalCallbacks {
				if (it.isEmpty()) {
					Spacer(modifier = Modifier.weight(1f))

					Text(
						"Ничего не нашлось :(\nСамое время запланировать новую поездку",
						style = Typography.titleMedium,
						textAlign = TextAlign.Center,
						fontSize = 18.sp,
						color = MaterialTheme.colorScheme.onBackground
					)

					Spacer(modifier = Modifier.height(Paddings.large))

					BigButton(
						buttonText = "Начать",
						onClick = navigateToCreateTrip,
						isLoading = false
					)

					Spacer(modifier = Modifier.weight(3f))

				} else {
					LazyColumn(verticalArrangement = Arrangement.spacedBy(Paddings.small)) {
						items(it) { trip ->
							TripCard(tripInfo = trip) { }
						}
					}
				}
			}
		}

		if (tasksIsNotEmpty) {
			TTFloatingActionButton(
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.padding(bottom = Paddings.medium),
				onClick = navigateToCreateTrip,
				text = "Добавить поездку"
			)
		}
	}
}

@Composable
fun TripCard(modifier: Modifier = Modifier, tripInfo: UITripModel, onClick: () -> Unit) {
	Card(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.primaryContainer,
			contentColor = MaterialTheme.colorScheme.onPrimaryContainer
		),
		shape = Shapes.smallRoundedBox,
		onClick = {}
	) {
		Row(
			modifier = Modifier
				.height(IntrinsicSize.Min)
				.padding(Paddings.medium)
		) {
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = tripInfo.placeName,
					style = Typography.titleMedium,
					fontSize = 24.sp
				)
				Spacer(modifier.height(Paddings.small))
				Spacer(modifier.weight(1f))
				Text(
					text = tripInfo.dateRangeString,
					style = Typography.labelLarge,
					fontSize = 14.sp
				)
			}

			Spacer(modifier = Modifier.width(Paddings.small))

			Column(modifier = Modifier.weight(1f)) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						modifier = Modifier.size(24.dp),
						painter = painterResource(R.drawable.ic_group),
						contentDescription = "User count"
					)
					Spacer(modifier = Modifier.width(Paddings.verySmall))
					Text(
						text = "${tripInfo.countOfPeoples} чел.",
						style = Typography.labelLarge,
						fontSize = 14.sp
					)
				}
				Spacer(modifier.height(Paddings.small))
				Spacer(modifier.weight(1f))
				Row(verticalAlignment = Alignment.CenterVertically) {
					tripInfo.readyPercentage?.let { readyPercentage ->
						Icon(
							modifier = Modifier.size(24.dp),
							painter = painterResource(R.drawable.ic_chart),
							contentDescription = "User count"
						)
						Spacer(modifier = Modifier.width(Paddings.verySmall))
						Text(
							text = "Готовность: $readyPercentage%",
							style = Typography.labelLarge,
							fontSize = 14.sp
						)
					}
				}
			}
		}
	}
}
