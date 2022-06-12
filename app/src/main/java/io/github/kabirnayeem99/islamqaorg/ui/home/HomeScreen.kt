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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.ui.destinations.QuestionDetailsScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {

    val viewModel: HomeViewModel = hiltViewModel()

    val uiState = viewModel.uiState

    LaunchedEffect(true) {
        viewModel.getRandomQuestions()
        viewModel.getFiqhBasedQuestions()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        topBar = { HomeScreenTopAppBar() }
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
                if (uiState.isRandomQuestionLoading) {
                    RandomQuestionLoadingIndicator()
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    val randomQuestions: List<Question> = uiState.randomQuestions
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
            if (uiState.isFiqhBasedQuestionsLoading)
                items(10) { QuestionItemPlaceholder() }
            else {
                val fiqhBasedQuestions: List<Question> = uiState.fiqhBasedQuestions
                itemsIndexed(fiqhBasedQuestions) { _, question ->
                    QuestionItemCard(question = question) {
                        navigator.navigate(QuestionDetailsScreenDestination(question.url))
                    }
                }
            }
        }
    }

}

@Composable
private fun HomeScreenTopAppBar() {
    val context = LocalContext.current
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier.padding(top = 12.dp),
        elevation = 0.dp,
    ) {
        TopBarActionButton(
            painterResource(id = R.drawable.ic_settings),
            stringResource(id = R.string.content_desc_settings)
        ) {
            Toast.makeText(context, "Go to settings", Toast.LENGTH_SHORT).show()
        }
        Spacer(modifier = Modifier.weight(0.9F))

        TopBarActionButton(
            painterResource(id = R.drawable.ic_sync),
            stringResource(id = R.string.content_desc_sync)
        ) {
            Toast.makeText(context, "Sync contents.", Toast.LENGTH_SHORT).show()
        }

    }
}

@Composable
private fun TopBarActionButton(painter: Painter, contentDescription: String, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.padding(top = 18.dp, start = 12.dp, bottom = 8.dp),
        onClick = { onClick() }) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
        )
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




