package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class GetPreferredFiqh @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(): Flow<Resource<Fiqh>> {
        return flow {
            try {
                val fiqh = repository.getPreferredFiqh()
                emit(Resource.Success(fiqh))
            } catch (e: Exception) {
                Timber.e(e, "invoke: " + e.localizedMessage)
                emit(Resource.Error(e.localizedMessage ?: "Error"))
            }
        }.onStart { emit(Resource.Loading()) }
    }
}