package io.github.kabirnayeem99.islamqaorg.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.OneTimeEvent
import io.github.kabirnayeem99.islamqaorg.domain.useCase.FetchAndSavePeriodically
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val fetchAndSavePeriodically: FetchAndSavePeriodically) :
    ViewModel() {

    private val _navEvent = OneTimeEvent<NavigationState>()
    val navEvent = _navEvent.asFlow()

    fun syncQuestionsAndAnswers() {
        viewModelScope.launch(Dispatchers.IO) {
            _navEvent.sendEvent(NavigationState.KeepLoading)
            fetchAndSavePeriodically()?.collect { result ->
                result.fold(
                    onSuccess = {
                        _navEvent.sendEvent(NavigationState.GoToHome)
                    },
                    onFailure = {
                        _navEvent.sendEvent(NavigationState.CloseApp)
                    },
                )
            }
        }
    }
}

enum class NavigationState {
    CloseApp, GoToHome, KeepLoading,
}