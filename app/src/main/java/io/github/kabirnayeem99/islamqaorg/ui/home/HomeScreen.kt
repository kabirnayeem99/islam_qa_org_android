package io.github.kabirnayeem99.islamqaorg.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {


    val context = LocalContext.current


    LaunchedEffect(true) {
        viewModel.getRandomQuestions()
        viewModel.getFiqhBasedQuestions()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
                modifier = Modifier.padding(top = 12.dp),
                elevation = 0.dp,
            ) {
                IconButton(
                    modifier = Modifier.padding(top = 18.dp, start = 12.dp, bottom = 8.dp),
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.content_desc_settings),
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = stringResource(id = R.string.content_desc_settings),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    )
                }
                Spacer(modifier = Modifier.weight(0.9F))
                IconButton(
                    modifier = Modifier.padding(top = 18.dp, end = 12.dp, bottom = 8.dp),
                    onClick = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.content_desc_sync),
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_sync),
                        contentDescription = stringResource(id = R.string.content_desc_sync),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),

                        )
                }

            }
        }
    ) {


        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Header Title
            item {
                Spacer(modifier = Modifier.height(62.dp))
                val homePageTitle = stringResource(id = R.string.q_n_a)
                ScreenTitle(homePageTitle)
                Spacer(modifier = Modifier.height(22.dp))
            }

            // Random Question Sliders Section
            item {
                val randomQuestionListHeading = stringResource(id = R.string.label_random_q_n_a)
                QuestionListHeading(randomQuestionListHeading)
                Spacer(modifier = Modifier.height(12.dp))
                if (viewModel.uiState.isRandomQuestionLoading) {
                    RandomQuestionLoadingIndicator()
                } else {
                    val randomQuestions: List<Question> = viewModel.uiState.randomQuestions
                    RandomQuestionSlider(randomQuestions)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Fiqh-based question list section
            item {
                val latestQuestionListHeading = stringResource(id = R.string.label_latest_q_n_a)
                QuestionListHeading(latestQuestionListHeading)
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (viewModel.uiState.isFiqhBasedQuestionsLoading)
                items(10) { QuestionItemPlaceholder() }
            else {
                val fiqhBasedQuestions: List<Question> = viewModel.uiState.fiqhBasedQuestions
                itemsIndexed(fiqhBasedQuestions) { _, question -> QuestionItemCard(question = question) }
            }
        }
    }

}

@Composable
private fun RandomQuestionSlider(randomQuestions: List<Question>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
    ) {
        itemsIndexed(randomQuestions) { index, question ->
            QuestionSliderItemCard(question = question, index = index)
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
private fun FiqhBasedQuestionListLoadingIndicator() {
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
private fun FiqhBasedQuestionList(questions: List<Question>) {
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


