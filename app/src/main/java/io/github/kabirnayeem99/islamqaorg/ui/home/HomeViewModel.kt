package io.github.kabirnayeem99.islamqaorg.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.common.base.UserMessage
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetHomeScreenData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getHomeScreenData: GetHomeScreenData) :
    ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    private var fetchHomeScreenDataJob: Job? = null

    fun getHomeScreenData() {
        fetchHomeScreenDataJob?.cancel()
        fetchHomeScreenDataJob = viewModelScope.launch(Dispatchers.IO) {
            getHomeScreenData.getHomeScreenData().distinctUntilChanged()
                .collect { res ->
                    when (res) {
                        is Resource.Loading -> toggleLoading(true)
                        is Resource.Error -> {
                            toggleLoading(false)
                            makeUserMessage(res.message ?: "")
                        }
                        is Resource.Success -> {
                            toggleLoading(false)
                            val questionAnswers = res.data ?: emptyList()
                            _uiState.update { it.copy(questionAnswers = questionAnswers) }
                        }
                    }
                }
        }
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        _uiState.update { it.copy(isLoading = shouldLoad) }
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
            _uiState.update {
                val messages = it.messages + UserMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = messageText
                )
                it.copy(messages = messages)
            }
        }
    }

    /**
     * Removes the message after it is shown with the given id from the user messages
     *
     * @param messageId The id of the message that was shown.
     */
    fun userMessageShown(messageId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentUiState ->
                val messages = currentUiState.messages.filterNot { it.id == messageId }
                currentUiState.copy(messages = messages)
            }

        }
    }
}