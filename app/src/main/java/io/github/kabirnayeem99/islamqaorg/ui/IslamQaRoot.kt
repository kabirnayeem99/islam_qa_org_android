package io.github.kabirnayeem99.islamqaorg.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
        val navigationActions = remember(navController) {
            NavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: IslamQaDestinations.HOME_ROUTE

        IslamQaNavGraph(navController = navController)

    }
}