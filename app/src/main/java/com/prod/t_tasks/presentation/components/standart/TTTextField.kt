import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TTTextField(
	value: String,
	onValueChange: (String) -> Unit,
	readOnly: Boolean = false,
	label: String,
	error: String? = null,
	singleLine: Boolean = true,
	keyboardOptions: KeyboardOptions = KeyboardOptions(
		keyboardType = KeyboardType.Text,
		imeAction = ImeAction.Next
	),
	trailingIcon: @Composable () -> Unit = {},
	// Новые параметры для выпадающего списка
	isDropdown: Boolean = false,
	dropdownItems: List<String> = emptyList(),
	onItemSelected: (String) -> Unit = {}
) {
	val typography = MaterialTheme.typography
	val colorScheme = MaterialTheme.colorScheme

	// Состояние для раскрытия/закрытия меню
	var expanded by remember { mutableStateOf(false) }

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

		if (isDropdown) {
			// Режим выпадающего списка
			ExposedDropdownMenuBox(
				expanded = expanded,
				onExpandedChange = { expanded = !expanded },
			) {
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.offset(y = 5.dp)
						.menuAnchor(),
					value = value,
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
					readOnly = true, // Для dropdown поле только для чтения
					trailingIcon = {
						if (trailingIcon != null) {
							trailingIcon()
						} else {
							ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
						}
					}
				)

				ExposedDropdownMenuBox (
					expanded = expanded,
					onExpandedChange = { expanded = it},
				) {
					dropdownItems.forEach { item ->
						DropdownMenuItem(
							text = { Text(text = item) },
							onClick = {
								onItemSelected(item)
								expanded = false
							}
						)
					}
				}
			}
		} else {
			// Обычный режим текстового поля
			OutlinedTextField(
				modifier = Modifier
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
				singleLine = singleLine,
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
		}
	}
}
