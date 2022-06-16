package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.common.PageTransitionAnimation
import io.github.kabirnayeem99.islamqaorg.ui.common.ScreenTitle
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import io.github.kabirnayeem99.islamqaorg.ui.start.SPLASH_SCREEN_DURATION
import io.github.kabirnayeem99.islamqaorg.ui.theme.ArabicFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = PageTransitionAnimation::class)
@Composable
fun AboutScreen(navigator: DestinationsNavigator) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        topBar = { AboutTopAppBar(navigator) },
    ) {
        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
            item {
                Spacer(modifier = Modifier.height(62.dp))
                val aboutLabel = stringResource(id = R.string.label_about)
                ScreenTitle(homePageTitle = aboutLabel)
                AboutContent()
            }
        }
    }
}

@Composable
private fun AboutTopAppBar(navigator: DestinationsNavigator) {
    TopAppBar(
        title = {},
        navigationIcon = {
            TopBarActionButton(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.content_desc_go_back)
            ) {
                navigator.navigateUp()
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)),
        elevation = 0.dp,
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AboutContent() {
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
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {


        AnimatedVisibility(
            visible = geometryVisibility,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_islamic_geometric),
                contentDescription = stringResource(id = R.string.content_desc_app_logo),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                alignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(350.dp)
                    .width(350.dp)
                    .graphicsLayer {
                        rotationZ = angle
                    }
            )

        }

        AnimatedVisibility(
            visible = centerAppLogoVisibility,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(260.dp)
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