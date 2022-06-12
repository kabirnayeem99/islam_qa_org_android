package io.github.kabirnayeem99.islamqaorg.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun ScreenTitle(homePageTitle: String) {
    Text(
        text = homePageTitle,
        style = MaterialTheme.typography
            .titleLarge
            .copy(color = MaterialTheme.colorScheme.onBackground),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}