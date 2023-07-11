package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

interface SettingsRepository {
    suspend fun savePreferredFiqh(fiqh: Fiqh)

    suspend fun getPreferredFiqh(): Fiqh
}