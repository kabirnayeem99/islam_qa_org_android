package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetPreferredFiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.SavePreferredFiqh
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePreferredFiqh: SavePreferredFiqh,
    private val getPreferredFiqh: GetPreferredFiqh,
) : ViewModel() {

    var uiState by mutableStateOf(SettingsUiState())
        private set

    private var savePreferredFiqhJob: Job? = null

    fun saveFiqh(fiqh: Fiqh) {
        savePreferredFiqhJob?.cancel()
        savePreferredFiqhJob = viewModelScope.launch {
            savePreferredFiqh(fiqh)
            uiState = uiState.copy(selectedFiqh = fiqh)
        }
    }

    private var getPreferredFiqhJob: Job? = null
    fun getFiqh() {
        getPreferredFiqhJob?.cancel()
        getPreferredFiqhJob = viewModelScope.launch {
            val fiqh = getPreferredFiqh()
            Timber.d("Fiqh is $fiqh")
            uiState = uiState.copy(selectedFiqh = fiqh)
        }
    }
}