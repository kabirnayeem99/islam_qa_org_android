package io.github.kabirnayeem99.islamqaorg.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BackgroundQAListFetcherWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val questionAnswerRepository: QuestionAnswerRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val successfullySynced = questionAnswerRepository.fetchAndSaveRandomQuestionList()
            if (successfullySynced) Result.success() else Result.failure()
        }
    }
}