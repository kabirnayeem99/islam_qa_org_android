package io.github.kabirnayeem99.islamqaorg.ui.questions


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.common.PageTransitionAnimation
import io.github.kabirnayeem99.islamqaorg.ui.common.ScreenTitle
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import io.github.kabirnayeem99.islamqaorg.ui.destinations.QuestionDetailsScreenDestination
import io.github.kabirnayeem99.islamqaorg.ui.destinations.QuestionScreenDestination
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemCard
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemPlaceholder
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Destination(style = PageTransitionAnimation::class)
@Composable
fun QuestionScreen(
    viewModel: QuestionsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    val uiState = viewModel.uiState

    val pageTitle = stringResource(id = R.string.label_q_n_a)

    val questions = uiState.questions

    val scope = rememberCoroutineScope()

    LaunchedEffect(true) { viewModel.fetchQuestions() }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(top = 12.dp), topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                TopBarActionButton(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(id = R.string.content_desc_go_back)
                ) {
                    scope.launch { navigator.navigateUp() }
                }
            },
            backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)
            ),
            elevation = 0.dp,
        )
    }) {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(top = it.calculateTopPadding())
        ) {

            // Header Title
            item {
                Spacer(modifier = Modifier.height(62.dp))
                ScreenTitle(pageTitle)
                Spacer(modifier = Modifier.height(22.dp))
            }


            // Fiqh-based question list section
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.isLoading || questions.isEmpty()) {
                items(10) {
                    QuestionItemPlaceholder()
                }
            }

            val lastQuestionUrl = questions.lastOrNull()?.url
            if (!uiState.isLoading && questions.isNotEmpty()) {
                itemsIndexed(questions, key = { _, item -> item.url }) { _, question ->
                    QuestionItemCard(
                        question = question, modifier = Modifier.animateItemPlacement()
                    ) {
                        navigator.navigate(QuestionDetailsScreenDestination(question.url))
                    }
                    LaunchedEffect(lastQuestionUrl) {
                        if (lastQuestionUrl == question.url) {
                            viewModel.fetchQuestions(false)
                        }
                    }
                }
            }


        }
    }

}




