package io.github.kabirnayeem99.islamqaorg.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.OneTimeEvent
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.DetermineIfFirstTime
import io.github.kabirnayeem99.islamqaorg.domain.useCase.OnboardUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class StartViewModel @Inject constructor(
    private val determineIfFirstTime: DetermineIfFirstTime,
    private val onboardUser: OnboardUser,
) : ViewModel() {

    private val _navEvent = OneTimeEvent<NavigationState>()
    val navEvent = _navEvent.asFlow()

    fun syncQuestionsAndAnswers() {
        viewModelScope.launch(Dispatchers.IO) {
            _navEvent.sendEvent(NavigationState.NonsensicalLoading)
            delay(randomLoadingTime)

            val isFirstTime = determineIfFirstTime()
            if (!isFirstTime) _navEvent.sendEvent(NavigationState.GoToHome)
            else _navEvent.sendEvent(NavigationState.ShowFiqhOption)
        }
    }

    fun onFiqhOptionSelected(fiqh: Fiqh) {
        viewModelScope.launch(Dispatchers.IO) {
            _navEvent.sendEvent(NavigationState.FetchingResources)
            onboardUser(fiqh).collect { res ->
                when (res) {
                    is Resource.Success -> _navEvent.sendEvent(NavigationState.GoToHome)
                    is Resource.Loading -> _navEvent.sendEvent(NavigationState.FetchingResources)
                    is Resource.Error -> {
                        Timber.e(res.message)
                        _navEvent.sendEvent(NavigationState.CloseApp)
                    }
                }
            }
        }
    }

    private val randomLoadingTime: Long
        get() = 225L + Random(System.currentTimeMillis()).nextLong(875L)
}

enum class NavigationState {
    CloseApp, GoToHome, NonsensicalLoading, ShowFiqhOption, FetchingResources,
}