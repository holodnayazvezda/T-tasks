package com.templates.templateprodcheb.presentation.utils.ui.placeholders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.templates.templateprodcheb.presentation.common_ui.theme.TemplateProdChebTheme


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