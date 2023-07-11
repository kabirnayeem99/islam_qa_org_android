package io.github.kabirnayeem99.islamqaorg.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.useCase.SearchQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
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


    private val changeQuerySyncKey = "change_query_sync_key"

    fun changeQueryText(text: String) {
        synchronized(changeQuerySyncKey) {
            viewModelScope.launch(Dispatchers.IO) { uiState = uiState.copy(query = text) }
        }
    }

    private var fetchSearchResults: Job? = null

    fun fetchSearchResults() {
        fetchSearchResults?.cancel()
        fetchSearchResults = viewModelScope.launch(Dispatchers.IO) {
            delay(Duration.ofMillis(800L))
            val query = uiState.query
            searchQuestion(query).distinctUntilChanged().collect { resource ->
                uiState = when (resource) {
                    is Resource.Success -> uiState.copy(
                        searchQuestionResults = resource.data ?: emptyList(),
                        isSearchResultLoading = false,
                    )

                    is Resource.Loading -> uiState.copy(
                        isSearchResultLoading = query.isNotBlank(),
                        searchQuestionResults = emptyList(),
                    )

                    else -> uiState.copy(isSearchResultLoading = false)
                }
            }
        }
    }

}