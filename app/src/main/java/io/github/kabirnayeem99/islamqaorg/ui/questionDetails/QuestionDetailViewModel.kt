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

    fun getQuestionsDetailsJob(url: String) {
        fetchQuestionDetailsJob?.cancel()
        fetchQuestionDetailsJob = viewModelScope.launch(Dispatchers.IO) {
            getQuestionDetails(url).distinctUntilChanged().collect { resource ->
                when (resource) {
                    is Resource.Loading -> toggleLoading(true)

                    is Resource.Error -> {
                        toggleLoading(false)
                        makeUserMessage(resource.message ?: "")
                    }

                    is Resource.Success -> {
                        toggleLoading(false)
                        uiState = uiState.copy(questionDetails = resource.data ?: QuestionDetail())
                    }
                }
            }
        }
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        uiState = uiState.copy(isLoading = shouldLoad)
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