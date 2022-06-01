package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import io.github.kabirnayeem99.islamqaorg.common.base.UserMessage
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail

data class QuestionDetailsUiState(
    val isLoading: Boolean = false,
    val messages: List<UserMessage> = emptyList(),
    val questionDetails: QuestionDetail = QuestionDetail(),
)