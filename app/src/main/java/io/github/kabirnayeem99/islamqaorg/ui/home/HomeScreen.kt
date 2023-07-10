package io.github.kabirnayeem99.islamqaorg.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.ui.common.ScreenTitle
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val uiState = homeViewModel.uiState

    val randomQuestionListHeading = stringResource(id = R.string.label_random_q_n_a)
    val homePageTitle = stringResource(id = R.string.q_n_a)
    val latestQuestionListHeading = stringResource(id = R.string.label_latest_q_n_a)

    val fiqhBasedQuestions = uiState.fiqhBasedQuestions
    val randomQuestions = uiState.randomQuestions

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        topBar = { HomeScreenTopAppBar(navigator) }
    ) {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(top = it.calculateTopPadding())
        ) {

            // Header Title
            item {
                Spacer(modifier = Modifier.height(62.dp))
                ScreenTitle(homePageTitle)
                Spacer(modifier = Modifier.height(22.dp))
            }

            // Random Question Sliders Section
            item {

                QuestionListHeading(randomQuestionListHeading)
                Spacer(modifier = Modifier.height(12.dp))

                AnimatedContent(targetState = uiState.isRandomQuestionLoading) { isLoading ->
                    if (isLoading) {
                        RandomQuestionLoadingIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                    } else {
                        RandomQuestionSlider(randomQuestions, navigator)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

            }

            // Fiqh-based question list section
            item {
                QuestionListHeading(latestQuestionListHeading)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.isFiqhBasedQuestionsLoading)
                items(10) { QuestionItemPlaceholder() }
            else {
                itemsIndexed(fiqhBasedQuestions, key = { _, item -> item.url }) { _, question ->
                    QuestionItemCard(
                        question = question,
                        modifier = Modifier.animateItemPlacement()
                    ) {
//                        scope.launch { navigator.navigate(QuestionDetailsScreenDestination(question.url)) }
                    }
                }
            }
        }
    }

}

@Composable
private fun HomeScreenTopAppBar(
    navigator: DestinationsNavigator,
) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)),
        elevation = 0.dp,

        ) {
        TopBarActionButton(
            Icons.Outlined.Settings,
            stringResource(id = R.string.content_desc_settings)
        ) {
//            scope.launch { navigator.navigate(SettingsScreenDestination()) }
        }

        Spacer(modifier = Modifier.weight(0.9F))

        TopBarActionButton(
            Icons.Outlined.Search,
            stringResource(id = R.string.content_desc_search)
        ) {
//            scope.launch { navigator.navigate(SearchScreenDestination()) }
        }

    }
}


@Composable
private fun RandomQuestionSlider(
    randomQuestions: List<Question>,
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        itemsIndexed(randomQuestions, key = { _, q -> q.url }) { index, question ->
            QuestionSliderItemCard(question = question, index = index, onClick = {
                scope.launch {
//                    navigator.navigate(QuestionDetailsScreenDestination(question.url))
                }
            })
        }
    }
}

@Composable
private fun RandomQuestionLoadingIndicator() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        val list = listOf("", "", "", "", "")
        itemsIndexed(list) { index, _ ->
            QuestionSliderItemCardPlaceholder(index)
        }
    }
}


@Composable
private fun QuestionListHeading(headingLabel: String) {

    Text(
        text = headingLabel,
        style = MaterialTheme.typography.headlineSmall
            .copy(color = MaterialTheme.colorScheme.primary),
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )

}




