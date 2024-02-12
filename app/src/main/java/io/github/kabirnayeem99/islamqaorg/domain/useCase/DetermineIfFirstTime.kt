package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import javax.inject.Inject

class DetermineIfFirstTime @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(): Boolean = settingsRepository.determineIfFirstTime()
}