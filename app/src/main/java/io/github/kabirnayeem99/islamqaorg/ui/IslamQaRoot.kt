package io.github.kabirnayeem99.islamqaorg.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import io.github.kabirnayeem99.islamqaorg.ui.theme.IslamQaTheme

@Composable
fun IslamQaRoot() {
    IslamQaTheme {

        val navController = rememberNavController()
        val navHostEngine = rememberNavHostEngine()

        DestinationsNavHost(
            navGraph = NavGraphs.root, navController = navController, engine = navHostEngine
        )

    }
}