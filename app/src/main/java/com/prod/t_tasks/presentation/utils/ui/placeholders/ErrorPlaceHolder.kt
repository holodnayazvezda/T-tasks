package com.prod.t_tasks.presentation.utils.ui.placeholders

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.prod.t_tasks.presentation.common_ui.theme.TemplateProdChebTheme


data class ErrorTexts(
	val title: String = "Error",
	val mainText: String = "Oh mio dio! \n" +
			"Sembra che qualcosa non va",
	val description: String = "Lavoreremo per sistemare le cose, ti chiediamo tornare più tardi."
)

@Composable
fun ErrorPlaceholder(
	modifier: Modifier = Modifier,
	showTop: Boolean = false,
	small: Boolean = false,
	showButton: Boolean = true,
	errorTexts: ErrorTexts = ErrorTexts(),
	actionText: String = "Ok",
	onAction: () -> Unit
) {
	Text("Error")
}


@Preview
@Composable
fun ErrorPlaceHolderPreview() {
	TemplateProdChebTheme {
		Scaffold {
			ErrorPlaceholder(modifier = Modifier.padding(it), showTop = true) { }
		}
	}
}