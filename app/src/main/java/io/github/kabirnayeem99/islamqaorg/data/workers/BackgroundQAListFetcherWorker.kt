package io.github.kabirnayeem99.islamqaorg.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

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

    companion object {
        private const val TAG = "BackgroundQAListFetcher"
        private const val SYNC_INTERVAL_HOURS = 4L
         fun enqueue(workManager: WorkManager) {
            try {
                val constraintsBuilder = Constraints.Builder()
                constraintsBuilder.apply {
                    setRequiredNetworkType(NetworkType.CONNECTED)
                    setRequiresBatteryNotLow(true)
                    setRequiresStorageNotLow(true)
                }
                val constraints = constraintsBuilder.build()

                val periodicRequestBuilder = PeriodicWorkRequest.Builder(
                    BackgroundQAListFetcherWorker::class.java, SYNC_INTERVAL_HOURS, TimeUnit.HOURS
                )
                periodicRequestBuilder.apply {
                    setConstraints(constraints)
                    addTag(TAG)
                }
                val request = periodicRequestBuilder.build()
                workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, request)
                Timber.d("Scheduled $TAG successfully")
            } catch (e: Exception) {
                Timber.e("schedulePeriodicSync: ${e.localizedMessage}", e)
            }
        }
    }
}

class BackgroundQAListFetcherWorkerFactory @Inject constructor(private val questionAnswerRepository: QuestionAnswerRepository) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context, workerClassName: String, workerParameters: WorkerParameters
    ): ListenableWorker =
        BackgroundQAListFetcherWorker(appContext, workerParameters, questionAnswerRepository)


}
