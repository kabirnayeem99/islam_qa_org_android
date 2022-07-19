package io.github.kabirnayeem99.islamqaorg.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.useCase.SearchQuestion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchQuestion: SearchQuestion,
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun changeQueryText(text: String) {
        viewModelScope.launch {
            uiState = uiState.copy(query = text)
            if (text.length % 2 == 0) fetchSearchResults()
        }
    }

    private suspend fun fetchSearchResults() {
        viewModelScope.launch {
            val query = uiState.query
            searchQuestion(query).collect { res ->
                when (res) {
                    is Resource.Success -> {
                        uiState = uiState.copy(searchQuestionResults = res.data ?: emptyList())
                    }

                    is Resource.Loading -> {
                        uiState = uiState.copy(isSearchResultLoading = true)
                    }

                    else -> Unit
                }
            }
        }
    }

}