package com.prod.t_tasks.presentation.components.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.prod.t_tasks.presentation.commonDataModels.UITaskTopicModel
import com.prod.t_tasks.presentation.theme.Paddings


@Composable
fun BubbledCategoryFilters(
    modifier: Modifier = Modifier,
    categories: List<UITaskTopicModel>,
    selectedItemId: Int,
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
            Spacer(modifier = Modifier.width(Paddings.large - Paddings.small))
        }

        items(categories) { category ->
            CategoryBubble(
                name = category.name,
                isSelected = category.id == selectedItemId
            ) {
                onCategoryToggle(category.id)
            }
        }

        item {
            Spacer(modifier = Modifier.width(Paddings.large - Paddings.small))
        }
    }
}