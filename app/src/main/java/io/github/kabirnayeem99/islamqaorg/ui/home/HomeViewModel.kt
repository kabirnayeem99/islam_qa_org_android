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
import kotlinx.coroutines.delay
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

    init {
        viewModelScope.launch {
            delay(500)
            getRandomQuestions(true)
            getFiqhBasedQuestions()
        }
    }

    private var fetchRandomQuestionJob: Job? = null

    private fun getRandomQuestions(shouldRefresh: Boolean = false) {
        fetchRandomQuestionJob?.cancel()
        fetchRandomQuestionJob = viewModelScope.launch(Dispatchers.IO) {
            getRandomQuestion(shouldRefresh).distinctUntilChanged().collect { resource ->
                uiState = when (resource) {
                    is Resource.Loading -> uiState.copy(isRandomQuestionLoading = true)

                    is Resource.Error -> {
                        makeUserMessage(resource.message ?: "")
                        uiState.copy(isRandomQuestionLoading = false)
                    }

                    is Resource.Success -> {
                        val questionAnswers = resource.data?.sortedBy { it.question } ?: emptyList()
                        makeUserMessage("Loaded successfully ${questionAnswers.size} questions.")
                        uiState.copy(
                            randomQuestions = questionAnswers, isRandomQuestionLoading = false
                        )
                    }
                }
            }
        }
    }

    private var fetchFiqhBasedQuestionJob: Job? = null

    private fun getFiqhBasedQuestions(shouldRefresh: Boolean = false) {
        fetchFiqhBasedQuestionJob?.cancel()
        fetchFiqhBasedQuestionJob = viewModelScope.launch(Dispatchers.IO) {
            val currentPage = uiState.currentPage
            getFiqhBasedQuestions(currentPage, shouldRefresh).distinctUntilChanged()
                .collect { resource ->
                    uiState = when (resource) {
                        is Resource.Loading -> {
                            uiState.copy(isFiqhBasedQuestionsLoading = true)
                        }

                        is Resource.Error -> {
                            makeUserMessage(resource.message ?: "")
                            uiState.copy(isFiqhBasedQuestionsLoading = false)
                        }

                        is Resource.Success -> {
                            val questionAnswers = resource.data ?: emptyList()
                            uiState.copy(
                                fiqhBasedQuestions = questionAnswers,
                                isFiqhBasedQuestionsLoading = false,
                            )
                        }
                    }
                }
        }
    }


    private fun makeUserMessage(messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val messages = uiState.messages + UserMessage(
                id = UUID.randomUUID().mostSignificantBits, message = messageText
            )
            uiState = uiState.copy(messages = messages)
        }
    }

    fun userMessageShown(messageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = uiState.messages.filterNot { it.id == messageId }
            uiState = uiState.copy(messages = messages)
        }
    }
}