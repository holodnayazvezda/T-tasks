package com.prod.t_tasks.presentation.utils.ui.placeholders

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.prod.t_tasks.presentation.common_ui.theme.TemplateProdChebTheme

@Composable
fun LoadingPlaceholder(
	modifier: Modifier = Modifier,
	text: String = "Già quasi scaricato, per favore aspetta un po"
) {
	Text(modifier = modifier, text = text)
}

@Preview
@Composable
private fun LoadingPlaceholderPreview() {
	Scaffold { paddingValues ->
		TemplateProdChebTheme {
			LoadingPlaceholder(
				modifier = Modifier
					.fillMaxWidth()
					.padding(paddingValues)
			)
		}
	}
}