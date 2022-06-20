package io.github.kabirnayeem99.islamqaorg.data.repository

import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) : SettingsRepository {

    /**
     * Saves the preferred fiqh in the preferences
     *
     * @param fiqh Fiqh - preferred islamic jurisprudence
     */
    override suspend fun savePreferredFiqh(fiqh: Fiqh) {
        return withContext(Dispatchers.IO) {
            preferenceDataSource.savePreferredFiqh(fiqh)
        }
    }

    /**
     * Gets the preferred fiqh from the preferences
     *
     * @return currently selected [Fiqh]
     */
    override suspend fun getPreferredFiqh(): Fiqh {
        return withContext(Dispatchers.IO) {
            preferenceDataSource.getPreferredFiqh()
        }
    }

}