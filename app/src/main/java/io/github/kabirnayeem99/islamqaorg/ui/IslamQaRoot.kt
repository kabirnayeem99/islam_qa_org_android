package io.github.kabirnayeem99.islamqaorg.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import io.github.kabirnayeem99.islamqaorg.ui.theme.IslamQaTheme

@Composable
fun IslamQaRoot() {
    IslamQaTheme {
        val systemUiController = rememberSystemUiController()
        val shouldUseDarkIcon = !isSystemInDarkTheme()
        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = shouldUseDarkIcon)
        }
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}