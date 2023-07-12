package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class SavePreferredFiqh @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(fiqh: Fiqh): Flow<Resource<Boolean>> {
        return flow {
            try {
                if (fiqh != Fiqh.UNKNOWN) repository.savePreferredFiqh(fiqh)
                emit(Resource.Success(true))
            } catch (e: Exception) {
                Timber.e(e, "invoke: " + e.localizedMessage)
                emit(Resource.Error(generateErrorMessage(e.localizedMessage ?: "")))
            }
        }.onStart { emit(Resource.Loading()) }
    }

    private fun generateErrorMessage(message: String) = "Failed to save Fiqh. $message"
}