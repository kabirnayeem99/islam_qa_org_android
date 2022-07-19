package io.github.kabirnayeem99.islamqaorg.ui.search

import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

data class SearchUiState(
    val query: String = "",
    val isSearchResultLoading: Boolean = false,
    val searchQuestionResults: List<Question> = emptyList(),
)