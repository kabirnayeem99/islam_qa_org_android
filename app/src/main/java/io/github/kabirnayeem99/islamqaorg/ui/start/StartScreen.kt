package io.github.kabirnayeem99.islamqaorg.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.kabirnayeem99.islamqaorg.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StartScreen(onTimeUp: () -> Unit) {

    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        scope.launch {
            delay(SPLASH_SCREEN_DURATION / 3)
            delay((SPLASH_SCREEN_DURATION * 2) / 3)
            onTimeUp()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_islamic_geometric),
            contentDescription = stringResource(id = R.string.content_desc_app_logo),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
            alignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .height(400.dp)
                .width(400.dp)
        )

        BoxWithConstraints(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_islamqa),
                contentDescription = stringResource(id = R.string.content_desc_app_logo),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
                alignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 22.dp)

            )
        }
    }
}