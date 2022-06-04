package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetPreferredFiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.SavePreferredFiqh
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePreferredFiqh: SavePreferredFiqh,
    private val getPreferredFiqh: GetPreferredFiqh,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private var savePreferredFiqhJob: Job? = null

    fun saveFiqh(fiqh: Fiqh) {
        savePreferredFiqhJob?.cancel()
        savePreferredFiqhJob = viewModelScope.launch {
            savePreferredFiqh(fiqh)
            _uiState.update { it.copy(selectedFiqh = fiqh) }
        }
    }

    private var getPreferredFiqhJob: Job? = null
    fun getFiqh() {
        getPreferredFiqhJob?.cancel()
        getPreferredFiqhJob = viewModelScope.launch {
            val fiqh = getPreferredFiqh()
            _uiState.update { it.copy(selectedFiqh = fiqh) }
        }
    }
}