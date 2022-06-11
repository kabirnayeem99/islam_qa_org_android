package io.github.kabirnayeem99.islamqaorg.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {


    LaunchedEffect(true) {
        viewModel.getRandomQuestions()
        viewModel.getFiqhBasedQuestions()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(62.dp))

            val homePageTitle = stringResource(id = R.string.q_n_a)
            ScreenTitle(homePageTitle)

            Spacer(modifier = Modifier.height(22.dp))

            val randomQuestionListHeading = stringResource(id = R.string.label_random_q_n_a)
            QuestionListHeading(randomQuestionListHeading)

            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.uiState.isRandomQuestionLoading) RandomQuestionListLoadingIndicator()
            else {
                val questions: List<Question> = viewModel.uiState.randomQuestions
                RandomQuestionList(questions)
            }
        }
    }

}

@Composable
private fun ScreenTitle(homePageTitle: String) {
    Text(
        text = homePageTitle,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
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

@Composable
private fun RandomQuestionListLoadingIndicator() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        items(10) {
            QuestionItemPlaceholder()
        }
    }
}

@Composable
private fun RandomQuestionList(questions: List<Question>) {
    if (questions.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            itemsIndexed(questions) { _, question ->
                QuestionItemCard(question = question)
            }
        }
    }
}


