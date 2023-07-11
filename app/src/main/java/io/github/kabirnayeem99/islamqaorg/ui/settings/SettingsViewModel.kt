package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetPreferredFiqh
import io.github.kabirnayeem99.islamqaorg.domain.useCase.SavePreferredFiqh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePreferredFiqh: SavePreferredFiqh,
    private val getPreferredFiqh: GetPreferredFiqh,
) : ViewModel() {

    var uiState by mutableStateOf(SettingsUiState())
        private set

    private var savePreferredFiqhJob: Job? = null

    /**
     * Saves the selected fiqh and updates the UI state with the selected fiqh
     *
     * @param fiqh [Fiqh] - The fiqh that the user has selected
     */
    fun saveFiqh(fiqh: Fiqh) {
        savePreferredFiqhJob?.cancel()
        savePreferredFiqhJob = viewModelScope.launch(Dispatchers.IO) {
            uiState = uiState.copy(selectedFiqh = fiqh)
            savePreferredFiqh(fiqh)
        }
    }

    private var getPreferredFiqhJob: Job? = null

    fun getFiqh() {
        getPreferredFiqhJob?.cancel()
        getPreferredFiqhJob = viewModelScope.launch(Dispatchers.IO) {
            getPreferredFiqh().collect { resource ->
                uiState = when (resource) {
                    is Resource.Loading -> uiState.copy(isLoading = true)

                    is Resource.Success -> {
                        val selectedFiqh = resource.data ?: Fiqh.UNKNOWN
                        uiState.copy(isLoading = false, selectedFiqh = selectedFiqh)
                    }

                    is Resource.Error -> uiState.copy(isLoading = false)
                }
            }
        }
    }
}