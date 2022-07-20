package io.github.kabirnayeem99.islamqaorg.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.useCase.SearchQuestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchQuestion: SearchQuestion,
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    init {
        fetchSearchResults()
    }

    /**
     * > When the user types in the search box, we update the UI state with the new text, and if the
     * text is an even number of characters, we fetch the search results
     *
     * @param text The text to be searched.
     */
    fun changeQueryText(text: String) {
        viewModelScope.launch {
            uiState = uiState.copy(query = text)
        }
    }

    private var fetchSearchResults: Job? = null

    /**
     * Fetches the search question results based on the query
     */
    fun fetchSearchResults() {
        fetchSearchResults?.cancel()
        viewModelScope.launch {
            val query = uiState.query
            searchQuestion(query).collect { res ->
                when (res) {

                    is Resource.Success -> {
                        uiState = uiState.copy(
                            searchQuestionResults = res.data ?: emptyList(),
                            isSearchResultLoading = false,
                        )
                    }

                    is Resource.Loading -> {
                        uiState = uiState.copy(
                            isSearchResultLoading = query.isNotBlank(),
                            searchQuestionResults = emptyList(),
                        )
                    }

                    else -> {
                        uiState = uiState.copy(isSearchResultLoading = false)
                    }
                }
            }
        }
    }

}