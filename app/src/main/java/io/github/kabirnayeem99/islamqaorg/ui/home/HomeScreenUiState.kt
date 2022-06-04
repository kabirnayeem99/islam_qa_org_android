package io.github.kabirnayeem99.islamqaorg.ui.home

import io.github.kabirnayeem99.islamqaorg.common.base.UserMessage
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

data class HomeScreenUiState(
    val isFiqhBasedQuestionsLoading: Boolean = false,
    val isRandomQuestionLoading: Boolean = false,
    val messages: List<UserMessage> = emptyList(),
    val randomQuestions: List<Question> = emptyList(),
    val fiqhBasedQuestions: List<Question> = emptyList(),
    val currentPage: Int = 1,
)