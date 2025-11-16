package com.prod.t_tasks.presentation.components.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.commonDataModels.UITaskModel
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ui.noRippleClickable

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
                    Paddings.medium
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
