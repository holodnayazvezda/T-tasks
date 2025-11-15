package com.prod.t_tasks.presentation.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.theme.Paddings
import com.prod.t_tasks.presentation.theme.Shapes
import com.prod.t_tasks.presentation.theme.TemplateProdChebTheme
import com.prod.t_tasks.presentation.utils.ui.noRippleClickable

@Composable
fun TBottomNavigation(modifier: Modifier = Modifier, selectedPage: Int, onSelect: (Int) -> Unit) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.surfaceContainer)
			.padding(vertical = Paddings.small),
		contentAlignment = Alignment.Center
	) {

		val firstIconPos = remember { mutableFloatStateOf(0f) }
		val secondIconPos = remember { mutableFloatStateOf(0f) }
		val thirdIconPos = remember { mutableFloatStateOf(0f) }

		val indicatorOffset =
			with(LocalDensity.current) {
				when (selectedPage) {
					0 -> firstIconPos.floatValue - secondIconPos.floatValue
					1 -> 0f
					2 -> thirdIconPos.floatValue - secondIconPos.floatValue
					else -> null
				}?.toDp()
			}
		AnimatedVisibility(indicatorOffset != null) {
			indicatorOffset?.let {
				Box(
					modifier = Modifier
						.size(85.dp, 45.dp)
						.offset(x = animateDpAsState(it).value)
						.background(
							MaterialTheme.colorScheme.onSurfaceVariant,
							shape = Shapes.smallRoundedBox
						)
				)
			}
		}
		Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
			Icon(
				modifier = Modifier
					.size(30.dp)
					.onGloballyPositioned {
						it.parentCoordinates?.positionInParent()?.let {
							firstIconPos.floatValue = it.x
						}
					}
					.noRippleClickable {
						onSelect(0)
					},
				painter = painterResource(R.drawable.ic_trips),
				tint = animateColorAsState(if (selectedPage == 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant).value,
				contentDescription = "open trips list screen"
			)

			Icon(
				modifier = Modifier
					.size(30.dp)
					.onGloballyPositioned {
						it.parentCoordinates?.positionInParent()?.let {
							secondIconPos.floatValue = it.x
						}
					}
					.noRippleClickable {
						onSelect(1)
					},
				painter = painterResource(R.drawable.ic_home),
				tint = animateColorAsState(if (selectedPage == 1) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant).value,
				contentDescription = "open tasks screen"
			)

			Icon(
				modifier = Modifier
					.size(30.dp)
					.onGloballyPositioned {
						it.parentCoordinates?.positionInParent()?.let {
							thirdIconPos.floatValue = it.x
						}
					}
					.noRippleClickable {
						onSelect(2)
					},
				painter = painterResource(R.drawable.ic_profile),
				tint = animateColorAsState(if (selectedPage == 2) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant).value,
				contentDescription = "open tasks screen"
			)
		}

	}
}

@Preview
@Composable
fun TBottomNavigationPreview() {
	TemplateProdChebTheme {
		Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
			val page = remember { mutableIntStateOf(0) }
			TBottomNavigation(selectedPage = page.intValue) {
				Log.d("click", it.toString())
				page.intValue = it
			}
		}
	}
}