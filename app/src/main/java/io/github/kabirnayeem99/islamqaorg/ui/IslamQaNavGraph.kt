package io.github.kabirnayeem99.islamqaorg.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.kabirnayeem99.islamqaorg.ui.home.HomeScreen

@Composable
fun IslamQaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = IslamQaDestinations.HOME_ROUTE
) {


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(IslamQaDestinations.HOME_ROUTE) { HomeScreen() }

//        composable(IslamQaDestinations.DETAILS_ROUTE) {
//            val homeViewModel: QuestionDetailViewModel = viewModel()
//            DetailsRoute(homeViewModel = homeViewModel)
//        }
    }
}