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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRandomQuestion: GetRandomQuestion,
    private val getFiqhBasedQuestions: GetFiqhBasedQuestions,
) : ViewModel() {

    var uiState by mutableStateOf(HomeScreenUiState())
        private set


    private var fetchRandomQuestionJob: Job? = null

    /**
     * Fetches random questions.
     *
     * @param shouldRefresh Boolean = false
     */
    fun getRandomQuestions(shouldRefresh: Boolean = false) {
        fetchRandomQuestionJob?.cancel()
        fetchRandomQuestionJob = viewModelScope.launch(Dispatchers.IO) {
            getRandomQuestion(shouldRefresh).distinctUntilChanged()
                .collect { res ->
                    when (res) {
                        is Resource.Loading -> {
                            uiState = uiState.copy(isRandomQuestionLoading = true)
                        }
                        is Resource.Error -> {
                            uiState = uiState.copy(isRandomQuestionLoading = false)
                            makeUserMessage(res.message ?: "")
                        }
                        is Resource.Success -> {

                            val questionAnswers = res.data?.sortedBy { it.question } ?: emptyList()
                            uiState = uiState.copy(
                                randomQuestions = questionAnswers,
                                isRandomQuestionLoading = false
                            )
                            makeUserMessage("Loaded successfully ${questionAnswers.size} questions.")
                        }
                    }
                }
        }
    }

    private var fetchFiqhBasedQuestionJob: Job? = null

    /**
     * Fetches the questions based on currently selected Fiqh and updates the UI state.
     *
     * @param shouldRefresh Boolean = false
     */
    fun getFiqhBasedQuestions(shouldRefresh: Boolean = false) {
        fetchFiqhBasedQuestionJob?.cancel()
        fetchFiqhBasedQuestionJob = viewModelScope.launch(Dispatchers.IO) {
            val currentPage = uiState.currentPage
            getFiqhBasedQuestions(currentPage, shouldRefresh).distinctUntilChanged()
                .collect { res ->
                    when (res) {
                        is Resource.Loading -> {
                            uiState = uiState.copy(isFiqhBasedQuestionsLoading = true)
                        }
                        is Resource.Error -> {
                            uiState = uiState.copy(isFiqhBasedQuestionsLoading = false)
                            makeUserMessage(res.message ?: "")
                        }
                        is Resource.Success -> {

                            val questionAnswers = res.data ?: emptyList()
                            uiState = uiState.copy(
                                fiqhBasedQuestions = questionAnswers,
                                isFiqhBasedQuestionsLoading = false,
                            )
                        }
                    }
                }
        }
    }


    /**
     * Makes a new user message with a unique id and add it to the list of user messages.
     *
     * @param messageText The text of the message to be sent.
     * @return Nothing.
     */
    private fun makeUserMessage(messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val messages = uiState.messages + UserMessage(
                id = UUID.randomUUID().mostSignificantBits,
                message = messageText
            )
            uiState = uiState.copy(messages = messages)
        }
    }

    /**
     * Removes the message after it is shown with the given id from the user messages
     *
     * @param messageId The id of the message that was shown.
     */
    fun userMessageShown(messageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = uiState.messages.filterNot { it.id == messageId }
            uiState = uiState.copy(messages = messages)
        }
    }
}