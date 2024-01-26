package io.github.kabirnayeem99.islamqaorg.ui.questions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetFiqhBasedQuestions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val getFiqhBasedQuestions: GetFiqhBasedQuestions,
) : ViewModel() {

    var uiState by mutableStateOf(QuestionsUiState())
        private set

    private var pageNumber = 1

    fun fetchQuestions(shouldRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (shouldRefresh) pageNumber = 1
            getFiqhBasedQuestions(pageNumber++).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val newQuestions = resource.data ?: emptyList()
                        if (newQuestions.isNotEmpty()) {
                            val currentQuestions = uiState.questions.toMutableList()
                            currentQuestions.addAll(newQuestions)
                            val newQuestionsSummed =
                                if (shouldRefresh) newQuestions else currentQuestions.distinctBy { it.url }
                            uiState =
                                uiState.copy(questions = newQuestionsSummed)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

}