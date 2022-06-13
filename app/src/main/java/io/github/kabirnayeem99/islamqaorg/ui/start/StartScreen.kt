package io.github.kabirnayeem99.islamqaorg.ui.start

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.theme.ArabicFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun StartScreen(onTimeUp: () -> Unit = {}) {

    val scope = rememberCoroutineScope()

    var centerAppLogoVisibility by remember { mutableStateOf(false) }
    var appVersionVisibility by remember { mutableStateOf(false) }
    var geometryVisibility by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(SPLASH_SCREEN_DURATION.toInt(), easing = FastOutSlowInEasing)
        )
    )

    LaunchedEffect(true) {
        scope.launch {
            delay(SPLASH_SCREEN_DURATION / 6)
            centerAppLogoVisibility = true
            delay(SPLASH_SCREEN_DURATION / 6)
            geometryVisibility = true
            delay(SPLASH_SCREEN_DURATION / 6)
            appVersionVisibility = true
            delay(SPLASH_SCREEN_DURATION / 3)
            delay(SPLASH_SCREEN_DURATION / 3)

            onTimeUp()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {


        AnimatedVisibility(
            visible = geometryVisibility,
            enter = scaleIn(),
            exit = scaleOut(),
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
                    .graphicsLayer {
                        rotationZ = angle
                    }
            )

        }

        AnimatedVisibility(
            visible = centerAppLogoVisibility,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Box(
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

        AnimatedVisibility(
            visible = appVersionVisibility,
            enter = slideInHorizontally() + fadeIn()
        ) {

            Box(
                modifier = Modifier
                    .padding(bottom = 200.dp, start = 200.dp, end = 0.dp)
                    .align(Alignment.CenterEnd)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.TopEnd,
            ) {
                Text(
                    text = "1.1.0-alpha",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontFamily = ArabicFontFamily,
                        fontStyle = FontStyle.Italic,
                    ),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                )
            }
        }
    }
}