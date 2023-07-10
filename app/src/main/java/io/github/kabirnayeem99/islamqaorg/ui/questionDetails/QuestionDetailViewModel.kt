package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.common.base.UserMessage
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetQuestionDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    private val getQuestionDetails: GetQuestionDetails
) : ViewModel() {


    var uiState by mutableStateOf(QuestionDetailsUiState())
        private set

    private var fetchQuestionDetailsJob: Job? = null

    /**
     * Fetches the question details and updates the UI state.
     *
     * @param url The url of the question details to be fetched
     */
    fun getQuestionsDetailsJob(url: String) {
        fetchQuestionDetailsJob?.cancel()
        fetchQuestionDetailsJob = viewModelScope.launch {
            getQuestionDetails(url).distinctUntilChanged().collect { res ->
                when (res) {
                    is Resource.Loading -> toggleLoading(true)
                    is Resource.Error -> {
                        toggleLoading(false)
                        makeUserMessage(res.message ?: "")
                    }

                    is Resource.Success -> {
                        toggleLoading(false)
                        uiState = uiState.copy(questionDetails = res.data ?: QuestionDetail())
                    }
                }
            }
        }
    }

    /**
     * Toggles loading on or off based on the parameter
     *
     * @param shouldLoad Boolean - This is a boolean value that determines whether the loading
     * indicator should be shown or not.
     */
    private fun toggleLoading(shouldLoad: Boolean) {
        uiState = uiState.copy(isLoading = shouldLoad)
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