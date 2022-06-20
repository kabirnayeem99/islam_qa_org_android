package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Saves the selected Fiqh of the user
 */
class SavePreferredFiqh @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(fiqh: Fiqh) {
        if (fiqh != Fiqh.UNKNOWN) repository.savePreferredFiqh(fiqh)
    }
}