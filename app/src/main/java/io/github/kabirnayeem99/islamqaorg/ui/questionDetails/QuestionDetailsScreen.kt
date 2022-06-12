package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.valentinilk.shimmer.shimmer
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.common.HtmlText
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemCard
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemPlaceholder
import timber.log.Timber

@Destination
@Composable
fun QuestionDetailsScreen(url: String) {

    val questionDetailViewModel: QuestionDetailViewModel = hiltViewModel()


    LaunchedEffect(true) {
        questionDetailViewModel.getQuestionsDetailsJob(url)
        Timber.d("Loading $url")
    }

    LazyColumn(modifier = Modifier.padding(24.dp)) {
        if (questionDetailViewModel.uiState.isLoading) {
            item {
                QuestionDetailsLoadingIndicator()
            }
            item {
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = stringResource(id = R.string.label_relevant_q_n_a),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20F)
                    )
                )
            }
            items(8) {
                QuestionItemPlaceholder(false)
            }
        } else {
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

            itemsIndexed(questionDetail.relevantQuestions) { _, question ->
                QuestionItemCard(question = question, shouldHavePadding = false, onClick = {
                    questionDetailViewModel.getQuestionsDetailsJob(question.url)
                })
            }
        }
    }

}

@Composable
private fun QuestionTitleText(questionTitle: String) {
    Text(
        text = questionTitle,
        style = MaterialTheme.typography.headlineLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = FontStyle.Normal
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
        text = questionDetail.ifBlank { "..." },
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
    )
}

@Composable
private fun QuestionAnswerText(questionAnswer: String) {
    HtmlText(
        text = questionAnswer.ifBlank { "..." },
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
    )
}

@Composable
private fun RelevantQaLabelText() {
    Text(
        text = stringResource(id = R.string.label_relevant_q_n_a),
        style = MaterialTheme.typography.headlineLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = FontStyle.Italic
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