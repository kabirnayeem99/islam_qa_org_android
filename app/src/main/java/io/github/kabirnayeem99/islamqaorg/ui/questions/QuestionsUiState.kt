package io.github.kabirnayeem99.islamqaorg.ui.questions

import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

data class QuestionsUiState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList()
)