package io.github.kabirnayeem99.islamqaorg.ui.home

import io.github.kabirnayeem99.islamqaorg.common.base.UserMessage
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val messages: List<UserMessage> = emptyList(),
    val questionAnswers: List<Question> = emptyList(),
)