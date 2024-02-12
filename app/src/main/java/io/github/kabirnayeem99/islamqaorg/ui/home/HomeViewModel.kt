package io.github.kabirnayeem99.islamqaorg.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.common.base.UserMessage
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetFiqhBasedQuestions
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetRandomQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRandomQuestion: GetRandomQuestion,
    private val getFiqhBasedQuestions: GetFiqhBasedQuestions,
) : ViewModel() {

    var uiState by mutableStateOf(HomeScreenUiState())
        private set


    fun fetchData() {
        fetchRandomQuestions()
        fetchFiqhBasedQuestions()
    }


    private fun fetchRandomQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            getRandomQuestion().collect { resource ->
                uiState = when (resource) {
                    is Resource.Loading -> uiState.copy(isRandomQuestionLoading = true)

                    is Resource.Error -> {
                        showUserMessage(resource.message ?: "")
                        uiState.copy(isRandomQuestionLoading = false)
                    }

                    is Resource.Success -> {
                        val questionAnswers = resource.data ?: emptyList()
                        uiState.copy(
                            randomQuestions = questionAnswers,
                            isRandomQuestionLoading = questionAnswers.isEmpty(),
                        )
                    }
                }
            }
        }
    }

    private var fetchFiqhBasedQuestionsJob: Job? = null

    private fun fetchFiqhBasedQuestions(shouldRefresh: Boolean = false) {
        fetchFiqhBasedQuestionsJob?.cancel()
        fetchFiqhBasedQuestionsJob = viewModelScope.launch(Dispatchers.IO) {
            getFiqhBasedQuestions(1).distinctUntilChanged().collect { resource ->
                uiState = when (resource) {
                    is Resource.Loading -> {
                        uiState.copy(isFiqhBasedQuestionsLoading = true)
                    }

                    is Resource.Error -> {
                        showUserMessage(resource.message ?: "")
                        uiState.copy(isFiqhBasedQuestionsLoading = false)
                    }

                    is Resource.Success -> {
                        val newQuestionAnswers = resource.data ?: emptyList()
                        if (newQuestionAnswers.isNotEmpty()) {
                            if (shouldRefresh) {
                                uiState.copy(
                                    fiqhBasedQuestions = newQuestionAnswers,
                                    isFiqhBasedQuestionsLoading = false,
                                )
                            } else {
                                var questionAnswers = uiState.fiqhBasedQuestions.toMutableList()
                                questionAnswers.addAll(newQuestionAnswers)
                                questionAnswers =
                                    questionAnswers.distinctBy { it.url }.toMutableList()
                                uiState.copy(
                                    fiqhBasedQuestions = questionAnswers,
                                    isFiqhBasedQuestionsLoading = questionAnswers.isEmpty(),
                                )
                            }
                        } else uiState.copy(isFiqhBasedQuestionsLoading = false)
                    }
                }
            }
        }
    }


    private fun showUserMessage(messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val messages = uiState.messages + UserMessage(
                id = UUID.randomUUID().mostSignificantBits, message = messageText
            )
            uiState = uiState.copy(messages = messages)
        }
    }

}