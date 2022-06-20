package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

interface SettingsRepository {

    /**
     * Saves the preferred fiqh in the preferences
     *
     * @param fiqh Fiqh - preferred islamic jurisprudence
     */
    suspend fun savePreferredFiqh(fiqh: Fiqh)

    /**
     * Gets the preferred fiqh from the preferences
     *
     * @return currently selected [Fiqh]
     */
    suspend fun getPreferredFiqh(): Fiqh
}