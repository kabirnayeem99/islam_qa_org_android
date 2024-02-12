package io.github.kabirnayeem99.islamqaorg.data.repository

import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) : SettingsRepository {

    override suspend fun savePreferredFiqh(fiqh: Fiqh) =
        preferenceDataSource.savePreferredFiqh(fiqh)

    override suspend fun getPreferredFiqh() = preferenceDataSource.getPreferredFiqh()

    override suspend fun determineIfFirstTime() = preferenceDataSource.determineIfFirstTime()

    override suspend fun markAsOpened() = preferenceDataSource.markAsOpened()

}