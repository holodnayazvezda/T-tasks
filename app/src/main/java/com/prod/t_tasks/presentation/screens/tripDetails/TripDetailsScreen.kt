package com.prod.t_tasks.presentation.screens.tripDetails

import TTTextFieldWithSearch
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.components.main_screen.CategoryBubble
import com.prod.t_tasks.presentation.components.standart.TopLogo
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ErrorCollectorScope


@Composable
fun ErrorCollectorScope.TripDetails(
	viewModel: TripDetailsViewModel
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = Paddings.large),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Spacer(modifier = Modifier.height(Paddings.large))
		TopLogo()
		Spacer(modifier = Modifier.height(Paddings.large))

		Text(
			modifier = Modifier.fillMaxWidth(),
			text = "Предстоящая поездка",
			style = Typography.titleLarge,
			textAlign = TextAlign.Center,
			fontSize = 32.sp,
			color = MaterialTheme.colorScheme.onBackground
		)

		Spacer(modifier = Modifier.height(Paddings.large))

		viewModel.tripInfo.FoldUIStateWithGlobalCallbacks { trip ->
			Card(
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.secondaryContainer,
					contentColor = MaterialTheme.colorScheme.onSecondaryContainer
				),
				shape = Shapes.smallRoundedBox
			) {
				Column(
					modifier = Modifier.padding(Paddings.large),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = "${trip.country}  •  ${trip.city}",
						style = Typography.titleMedium,
						textAlign = TextAlign.Center,
						fontSize = 24.sp
					)
					Spacer(modifier = Modifier.height(Paddings.large))
					Text(
						text = trip.dateRangeString,
						style = Typography.labelLarge,
						textAlign = TextAlign.Center,
						fontSize = 16.sp
					)

					Spacer(modifier = Modifier.height(Paddings.large))

					Text(
						modifier = Modifier
							.background(
								MaterialTheme.colorScheme.tertiaryContainer,
								Shapes.verySmallRoundedBox
							)
							.padding(Paddings.small),
						text = trip.dateRangeString,
						style = Typography.labelLarge,
						textAlign = TextAlign.Center,
						fontSize = 14.sp,
						color = MaterialTheme.colorScheme.onTertiaryContainer
					)

					Spacer(modifier = Modifier.height(Paddings.large))

					Text(
						text = trip.description,
						style = Typography.labelLarge,
						textAlign = TextAlign.Center,
						fontSize = 14.sp,
						maxLines = 3,
						overflow = TextOverflow.Ellipsis
					)

					Spacer(modifier = Modifier.height(Paddings.large))

					Text(
						text = "Участники поездки",
						style = Typography.titleLarge,
						textAlign = TextAlign.Center,
						fontSize = 20.sp
					)

					Spacer(modifier = Modifier.height(Paddings.medium))

					FlowRow(
						horizontalArrangement = Arrangement.spacedBy(Paddings.small),
						verticalArrangement = Arrangement.spacedBy(Paddings.small)
					) {
						trip.members.forEach {
							CategoryBubble(
								name = it.second,
								isSelected = false
							) {

							}
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
		}
	}
}