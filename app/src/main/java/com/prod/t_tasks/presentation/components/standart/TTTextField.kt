import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prod.t_tasks.R
import com.prod.t_tasks.presentation.theme.Typography
import com.prod.t_tasks.presentation.utils.ui.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TTTextField(
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
	value: String,
	onValueChange: (String) -> Unit,
	readOnly: Boolean = false,
	label: String = "",
	error: String? = null,
	singleLine: Boolean = true,
	maxLines: Int = 1,
	keyboardOptions: KeyboardOptions = KeyboardOptions(
		keyboardType = KeyboardType.Text,
		imeAction = ImeAction.Next
	),
	onDone: (() -> Unit)? = null,
	trailingIcon: @Composable () -> Unit = {}
) {
	val typography = MaterialTheme.typography
	val colorScheme = MaterialTheme.colorScheme

	Box(
		Modifier.height(70.dp),
	) {
		Box(
			Modifier
				.fillMaxWidth()
				.height(56.dp)
				.offset(x = 5.dp)
				.background(
					color = Color.White,
					shape = RoundedCornerShape(15.dp)
				)
		)
		OutlinedTextField(
			modifier = modifier
				.fillMaxWidth()
				.offset(y = 5.dp),
			value = value,
			readOnly = readOnly,
			onValueChange = onValueChange,
			textStyle = typography.labelLarge,
			placeholder = {
				Text(
					label,
					style = typography.labelLarge,
					fontSize = 14.sp,
				)
			},
			isError = error != null,
			supportingText = {
				if (error != null) {
					Spacer(Modifier.height(5.dp))
					Text(
						text = error,
						style = typography.labelLarge,
						fontSize = 12.sp
					)
				}
			},
			keyboardOptions = keyboardOptions,
			keyboardActions = KeyboardActions(
				onDone = {
					onDone?.invoke()
				}
			),
			singleLine = singleLine,
			maxLines = maxLines,
			colors = OutlinedTextFieldDefaults.colors(
				focusedContainerColor = colorScheme.primary,
				unfocusedContainerColor = colorScheme.primary,
				errorContainerColor = colorScheme.error,

				focusedBorderColor = Color.Transparent,
				unfocusedBorderColor = Color.Transparent,
				errorBorderColor = Color.Transparent,

				focusedPlaceholderColor = colorScheme.onPrimary,
				unfocusedPlaceholderColor = colorScheme.onPrimary,
				errorPlaceholderColor = colorScheme.onError,

				focusedTextColor = colorScheme.onPrimary,
				unfocusedTextColor = colorScheme.onPrimary,
				errorTextColor = colorScheme.onError,

				cursorColor = colorScheme.onPrimary,
				errorCursorColor = colorScheme.onError
			),
			shape = RoundedCornerShape(15.dp),
			trailingIcon = trailingIcon
		)

		if (readOnly && onClick != null) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.noRippleClickable(onClick)
			)
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TTTextFieldWithDropdown(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit = {},
	readOnly: Boolean = true,
	label: String,
	error: String? = null,
	singleLine: Boolean = true,
	maxLines: Int = 1,
	keyboardOptions: KeyboardOptions = KeyboardOptions(
		keyboardType = KeyboardType.Text,
		imeAction = ImeAction.Next
	),
	dropdownItems: List<T> = emptyList(),
	onDropdownItemSelected: (T) -> Unit = {},
	dropDownItem: @Composable (T) -> Unit,
	trailingIcon: @Composable (Boolean) -> Unit = {
		Icon(
			modifier = Modifier
				.size(24.dp)
				.rotate(animateFloatAsState(if (it) 180f else 0f).value),
			painter = painterResource(R.drawable.ic_arr_dropdown),
			tint = MaterialTheme.colorScheme.onPrimary,
			contentDescription = null
		)
	}
) {
	val typography = MaterialTheme.typography
	val colorScheme = MaterialTheme.colorScheme

	var expanded by remember { mutableStateOf(false) }

	Box(
		modifier.height(70.dp),
	) {
		Box(
			Modifier
				.fillMaxWidth()
				.height(56.dp)
				.offset(x = 5.dp)
				.background(
					color = Color.White,
					shape = RoundedCornerShape(15.dp)
				)
		)

		ExposedDropdownMenuBox(
			expanded = expanded,
			onExpandedChange = { expanded = !expanded },
			modifier = Modifier.offset(y = 5.dp)
		) {
			OutlinedTextField(
				modifier = Modifier
					.fillMaxWidth()
					.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
				value = value,
				readOnly = readOnly,
				onValueChange = onValueChange,
				textStyle = typography.labelLarge,
				placeholder = {
					Text(
						label,
						style = typography.labelLarge,
						fontSize = 14.sp,
					)
				},
				isError = error != null,
				supportingText = {
					if (error != null) {
						Spacer(Modifier.height(5.dp))
						Text(
							text = error,
							style = typography.labelLarge,
							fontSize = 12.sp
						)
					}
				},
				keyboardOptions = keyboardOptions,
				singleLine = singleLine,
				maxLines = maxLines,
				colors = OutlinedTextFieldDefaults.colors(
					focusedContainerColor = colorScheme.primary,
					unfocusedContainerColor = colorScheme.primary,
					errorContainerColor = colorScheme.error,

					focusedBorderColor = Color.Transparent,
					unfocusedBorderColor = Color.Transparent,
					errorBorderColor = Color.Transparent,

					focusedPlaceholderColor = colorScheme.onPrimary,
					unfocusedPlaceholderColor = colorScheme.onPrimary,
					errorPlaceholderColor = colorScheme.onError,

					focusedTextColor = colorScheme.onPrimary,
					unfocusedTextColor = colorScheme.onPrimary,
					errorTextColor = colorScheme.onError,

					cursorColor = colorScheme.onPrimary,
					errorCursorColor = colorScheme.onError
				),
				shape = RoundedCornerShape(15.dp),
				trailingIcon = {
					trailingIcon(expanded)
				}
			)

			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false },
				modifier = Modifier.exposedDropdownSize()
			) {
				if (dropdownItems.isEmpty()) {
					DropdownMenuItem(
						text = {
							Text("Здесь пока ничего нет", style = Typography.titleMedium)
						},
						onClick = {
							expanded = false
						}
					)
				}
				dropdownItems.forEach { item ->
					DropdownMenuItem(
						text = {
							dropDownItem(item)
						},
						onClick = {
							onDropdownItemSelected(item)
							expanded = false
						}
					)
				}
			}
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TTTextFieldWithSearch(
	modifier: Modifier = Modifier,
	value: String,
	onValueChange: (String) -> Unit = {},
	readOnly: Boolean = true,
	label: String,
	error: String? = null,
	singleLine: Boolean = true,
	maxLines: Int = 1,
	keyboardOptions: KeyboardOptions = KeyboardOptions(
		keyboardType = KeyboardType.Text,
		imeAction = ImeAction.Next
	),
	dropdownItems: List<T> = emptyList(),
	onDropdownItemSelected: (T) -> Unit = {},
	dropDownItem: @Composable (T) -> Unit,
	trailingIcon: @Composable (Boolean) -> Unit = {}
) {
	val typography = MaterialTheme.typography
	val colorScheme = MaterialTheme.colorScheme

	var expanded by remember { mutableStateOf(false) }

	Box(
		modifier.height(70.dp),
	) {
		Box(
			Modifier
				.fillMaxWidth()
				.height(56.dp)
				.offset(x = 5.dp)
				.background(
					color = Color.White,
					shape = RoundedCornerShape(15.dp)
				)
		)

		ExposedDropdownMenuBox(
			expanded = expanded,
			onExpandedChange = { expanded = !expanded },
			modifier = Modifier.offset(y = 5.dp)
		) {
			OutlinedTextField(
				modifier = Modifier
					.fillMaxWidth()
					.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
				value = value,
				readOnly = readOnly,
				onValueChange = onValueChange,
				textStyle = typography.labelLarge,
				placeholder = {
					Text(
						label,
						style = typography.labelLarge,
						fontSize = 14.sp,
					)
				},
				isError = error != null,
				supportingText = {
					if (error != null) {
						Spacer(Modifier.height(5.dp))
						Text(
							text = error,
							style = typography.labelLarge,
							fontSize = 12.sp
						)
					}
				},
				keyboardOptions = keyboardOptions,
				singleLine = singleLine,
				maxLines = maxLines,
				colors = OutlinedTextFieldDefaults.colors(
					focusedContainerColor = colorScheme.primary,
					unfocusedContainerColor = colorScheme.primary,
					errorContainerColor = colorScheme.error,

					focusedBorderColor = Color.Transparent,
					unfocusedBorderColor = Color.Transparent,
					errorBorderColor = Color.Transparent,

					focusedPlaceholderColor = colorScheme.onPrimary,
					unfocusedPlaceholderColor = colorScheme.onPrimary,
					errorPlaceholderColor = colorScheme.onError,

					focusedTextColor = colorScheme.onPrimary,
					unfocusedTextColor = colorScheme.onPrimary,
					errorTextColor = colorScheme.onError,

					cursorColor = colorScheme.onPrimary,
					errorCursorColor = colorScheme.onError
				),
				shape = RoundedCornerShape(15.dp),
				trailingIcon = {
					trailingIcon(expanded)
				}
			)

			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false },
				modifier = Modifier.exposedDropdownSize()
			) {
				dropdownItems.forEach { item ->
					DropdownMenuItem(
						text = {
							dropDownItem(item)
						},
						onClick = {
							onDropdownItemSelected(item)
							expanded = false
						}
					)
				}
			}
		}
	}
}