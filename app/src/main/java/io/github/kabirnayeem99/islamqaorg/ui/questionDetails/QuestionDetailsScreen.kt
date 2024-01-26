package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.valentinilk.shimmer.shimmer
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.common.HtmlText
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import io.github.kabirnayeem99.islamqaorg.ui.destinations.QuestionDetailsScreenDestination
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemCard
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemPlaceholder
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun QuestionDetailsScreen(
    url: String,
    questionDetailViewModel: QuestionDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {

    val scope = rememberCoroutineScope()
    val screenScrollState = rememberLazyListState()

    LaunchedEffect(true) {
        questionDetailViewModel.getQuestionsDetailsJob(url)
        Timber.d("Loading $url")
    }

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

        it.toString()

        AnimatedContent(
            transitionSpec = {
                if (questionDetailViewModel.uiState.questionDetails.questionTitle.isNotBlank()) {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) + slideInVertically(
                        initialOffsetY = { 90 }, animationSpec = tween(220, delayMillis = 90)
                    )).togetherWith(fadeOut(animationSpec = tween(90)))
                } else fadeIn(animationSpec = tween(220, delayMillis = 90)) togetherWith fadeOut(
                    animationSpec = tween(90)
                )
            },
            targetState = questionDetailViewModel.uiState.isLoading,
            label = "QUESTION_LIST_RELEVANT"
        ) { isLoading ->
            if (isLoading) LazyColumn(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                state = screenScrollState
            ) {

                item { Spacer(modifier = Modifier.height(52.dp)) }

                item { QuestionDetailsLoadingIndicator() }

                item {
                    Spacer(modifier = Modifier.height(22.dp))
                    RelevantQaLabelLoadingIndicator()
                }

                items(6) { QuestionItemPlaceholder(false) }

            }
            else LazyColumn(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {

                item { Spacer(modifier = Modifier.height(52.dp)) }

                val questionDetail = questionDetailViewModel.uiState.questionDetails

                item {
                    QuestionTitleText(questionDetail.questionTitle)
                    Spacer(modifier = Modifier.height(6.dp))
                    QuestionSourceText(questionDetail.fiqh, questionDetail.source)
                    Spacer(modifier = Modifier.height(12.dp))
                    QuestionDetailText(questionDetail.detailedQuestion)
                    Spacer(modifier = Modifier.height(12.dp))
                    QuestionAnswerText(questionDetail.detailedAnswer)
                    Spacer(modifier = Modifier.height(22.dp))
                    RelevantQaLabelText()
                }

                itemsIndexed(
                    questionDetail.relevantQuestions,
                    key = { _, q -> q.url },
                ) { _, question ->
                    QuestionItemCard(question = question, shouldHavePadding = false, onClick = {
                        scope.launch {
                            navigator.navigate(QuestionDetailsScreenDestination(question.url))
                        }
                    }, modifier = Modifier.animateItemPlacement())
                }
            }
        }


    }

}

@Composable
private fun RelevantQaLabelLoadingIndicator() {
    Text(
        text = stringResource(id = R.string.label_relevant_q_n_a),
        style = MaterialTheme.typography.headlineLarge.copy(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20F)
        )
    )
}

@Composable
private fun QuestionTitleText(questionTitle: String) {
    Text(
        text = questionTitle, style = MaterialTheme.typography.headlineLarge.copy(
            color = MaterialTheme.colorScheme.onBackground, fontStyle = FontStyle.Normal
        )
    )
}

@Composable
private fun QuestionSourceText(fiqh: String, source: String) {
    Text(
        text = stringResource(id = R.string.question_source_text, fiqh, source),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8F),
            fontStyle = FontStyle.Italic
        )
    )
}

@Composable
private fun QuestionDetailText(questionDetail: String) {
    HtmlText(
        text = questionDetail,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
    )
}

@Composable
private fun QuestionAnswerText(questionAnswer: String) {
    HtmlText(
        text = questionAnswer,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
    )
}

@Composable
private fun RelevantQaLabelText() {
    Text(
        text = stringResource(id = R.string.label_relevant_q_n_a),
        style = MaterialTheme.typography.headlineLarge.copy(
            color = MaterialTheme.colorScheme.onBackground, fontStyle = FontStyle.Italic
        )
    )
}

@Composable
private fun QuestionDetailsLoadingIndicator() {
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20F),
                shape = RoundedCornerShape(12.dp)
            )
            .shimmer()
    ) {

    }
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20F),
                shape = RoundedCornerShape(12.dp)
            )
            .shimmer()
    ) {

    }

    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20F),
                shape = RoundedCornerShape(12.dp)
            )
            .shimmer()
    ) {

    }
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20F),
                shape = RoundedCornerShape(12.dp)
            )
            .shimmer()
    ) {

    }
}