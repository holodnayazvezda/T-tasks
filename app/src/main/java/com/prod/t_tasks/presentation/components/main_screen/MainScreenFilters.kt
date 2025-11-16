package com.prod.t_tasks.presentation.components.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.components.standart.TCheckBox
import com.prod.t_tasks.presentation.screens.main.MainScreenViewModel
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ui.clickable

@Composable
fun MainScreenFilters(
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
