package io.github.kabirnayeem99.islamqaorg.domain.useCase

import androidx.work.WorkManager
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.data.workers.BackgroundQAListFetcherWorker
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnboardUser @Inject constructor(
    private val settingsRepo: SettingsRepository,
    private val qaRepo: QuestionAnswerRepository,
    private val workManager: WorkManager,
) {
    suspend operator fun invoke(fiqh: Fiqh): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            settingsRepo.savePreferredFiqh(fiqh)
            BackgroundQAListFetcherWorker.enqueuePeriodically(
                workManager,
                qaRepo,
                onSuccess = {
                    settingsRepo.markAsOpened()
                    emit(Resource.Success(true))
                },
                onFailure = {
                    emit(Resource.Error<Boolean>("Failed to start fetching resources."))
                },
            )
        }.catch { e ->
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        }
    }

}