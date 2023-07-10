package io.github.kabirnayeem99.islamqaorg.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import io.github.kabirnayeem99.islamqaorg.ui.theme.IslamQaTheme

@Composable
fun IslamQaRoot() {
    IslamQaTheme {

        val systemUiController = rememberSystemUiController()
        val shouldUseDarkIcon = !isSystemInDarkTheme()

        SideEffect {
            systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = shouldUseDarkIcon)
        }

        val navController = rememberNavController()
        val navHostEngine = rememberNavHostEngine()

        DestinationsNavHost(
            navGraph = NavGraphs.root, navController = navController, engine = navHostEngine
        )

    }
}