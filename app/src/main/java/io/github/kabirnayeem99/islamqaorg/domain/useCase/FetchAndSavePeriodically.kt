package io.github.kabirnayeem99.islamqaorg.domain.useCase

import androidx.work.WorkManager
import io.github.kabirnayeem99.islamqaorg.data.workers.BackgroundQAListFetcherWorker
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import javax.inject.Inject

class FetchAndSavePeriodically
@Inject constructor(
    private val workManager: WorkManager,
    private val questionAnswerRepository: QuestionAnswerRepository,
) {

    suspend operator fun invoke() =
        BackgroundQAListFetcherWorker.enqueue(workManager, questionAnswerRepository)


}