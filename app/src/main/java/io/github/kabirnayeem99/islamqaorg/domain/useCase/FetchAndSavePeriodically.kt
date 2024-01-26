package io.github.kabirnayeem99.islamqaorg.domain.useCase

import androidx.work.WorkManager
import io.github.kabirnayeem99.islamqaorg.data.workers.BackgroundQAListFetcherWorker
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchAndSavePeriodically
@Inject constructor(private val workManager: WorkManager) {

    operator fun invoke() =
        BackgroundQAListFetcherWorker.enqueue(workManager)


}