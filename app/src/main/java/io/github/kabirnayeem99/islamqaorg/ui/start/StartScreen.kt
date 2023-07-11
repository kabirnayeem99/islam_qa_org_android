package io.github.kabirnayeem99.islamqaorg.ui.start

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.theme.ArabicFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun StartScreen(
    onTimeUp: () -> Unit = {},
    viewModel: StartViewModel = hiltViewModel(),
) {

    val scope = rememberCoroutineScope()

    var shouldNavigateNow by remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= 33) {
        val postNotificationPermission =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

        LaunchedEffect(key1 = true) {
            postNotificationPermission.launchPermissionRequest()
        }

        shouldNavigateNow = when (postNotificationPermission.status) {
            PermissionStatus.Granted -> {
                viewModel.syncQuestionsAndAnswers()
                true
            }

            is PermissionStatus.Denied -> {
                false
            }
        }
    } else {
        shouldNavigateNow = true
    }

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
            if (shouldNavigateNow) onTimeUp()
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
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {

            AsyncImage(
                R.drawable.ic_islamic_geometric,
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
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
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
                AsyncImage(
                    R.drawable.ic_islamqa,
                    contentDescription = stringResource(id = R.string.content_desc_app_logo),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(182.dp)
                        .width(182.dp)
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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.TopEnd,
            ) {
                Text(
                    text = BuildConfig.VERSION_NAME,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontFamily = ArabicFontFamily,
                        fontStyle = FontStyle.Italic,
                    ),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                )
            }
        }
    }
}