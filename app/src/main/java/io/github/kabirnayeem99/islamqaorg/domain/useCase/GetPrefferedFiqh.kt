package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import javax.inject.Inject

class GetPreferredFiqh @Inject constructor(private val repository: SettingsRepository) {

    suspend operator fun invoke(): Fiqh {
        return repository.getPreferredFiqh()
    }
}