package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.BuildConfig
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

        it.toString()

        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
            item {
                Spacer(modifier = Modifier.height(62.dp))
                val aboutLabel = stringResource(id = R.string.label_about)
                ScreenTitle(homePageTitle = aboutLabel)
            }
            item {
                AboutContent()
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = stringResource(id = R.string.about_app),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
                AboutButtonSection()
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
private fun AboutButtonSection() {

    val uriHandler: UriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        AboutBoxItem(
            icon = Icons.Outlined.Info,
            title = stringResource(id = R.string.label_about)
        ) {
            scope.launch {
                navigateToIslamQaAboutSection(uriHandler)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        AboutBoxItem(
            icon = Icons.Outlined.Build,
            title = stringResource(id = R.string.content_desc_open_github_source_code)
        ) {
            scope.launch {
                navigateToSourceCode(uriHandler)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        AboutBoxItem(
            icon = Icons.Outlined.Person,
            title = stringResource(id = R.string.content_desc_about_developer)
        ) {
            scope.launch {
                navigateToMyPersonalGitHubProfile(uriHandler)
            }
        }
    }
}

@Composable
private fun AboutBoxItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimaryContainer)
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = icon,
            contentDescription = title,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

@Composable
private fun AboutTopAppBar(navigator: DestinationsNavigator) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {},
        navigationIcon = {
            TopBarActionButton(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.content_desc_go_back)
            ) {
                scope.launch { navigator.navigateUp() }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)),
        elevation = 0.dp,
    )
}


@Composable
private fun AboutContent() {
    val scope = rememberCoroutineScope()

    var appVersionVisibility by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween((SPLASH_SCREEN_DURATION * 2).toInt(), easing = LinearEasing)
        )
    )

    LaunchedEffect(true) {
        scope.launch {
            delay(SPLASH_SCREEN_DURATION / 6)
            appVersionVisibility = true
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(),
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
                .height(350.dp)
                .width(350.dp)
                .graphicsLayer {
                    rotationZ = angle
                }
        )

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
                    .height(162.dp)
                    .width(162.dp)
            )
        }

        AnimatedVisibility(
            visible = appVersionVisibility,
            enter = slideInHorizontally() + fadeIn()
        ) {

            Box(
                modifier = Modifier
                    .padding(bottom = 180.dp, start = 180.dp, end = 0.dp)
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


private const val GITHUB_SOURCE_CODE_LINK = "https://github.com/kabirnayeem99/islam_qa_org_android"

private fun navigateToSourceCode(uriHandler: UriHandler) {
    uriHandler.openUri(GITHUB_SOURCE_CODE_LINK)
}

private const val ISLAMQA_ABOUT_SECTION_LINK = "https://islamqa.org/about/"

private fun navigateToIslamQaAboutSection(uriHandler: UriHandler) {
    uriHandler.openUri(ISLAMQA_ABOUT_SECTION_LINK)
}

private const val PERSONAL_GITHUB_PROFILE_LINK = "https://github.com/kabirnayeem99/"

private fun navigateToMyPersonalGitHubProfile(uriHandler: UriHandler) {
    uriHandler.openUri(PERSONAL_GITHUB_PROFILE_LINK)
}