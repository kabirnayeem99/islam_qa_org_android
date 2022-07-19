package io.github.kabirnayeem99.islamqaorg.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun changeQueryText(text: String) {
        viewModelScope.launch {
            uiState = uiState.copy(query = text)
            val questionCount = Random.nextInt(3, 15)
            val questions =
                List(questionCount) { Question(question = "Who $it?", url = it.toString()) }
            uiState = uiState.copy(searchQuestionResults = questions)
        }
    }
}