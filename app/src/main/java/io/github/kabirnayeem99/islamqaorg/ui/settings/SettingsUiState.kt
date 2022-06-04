package io.github.kabirnayeem99.islamqaorg.ui.settings

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

data class SettingsUiState(
    val selectedFiqh: Fiqh = Fiqh.UNKNOWN
)