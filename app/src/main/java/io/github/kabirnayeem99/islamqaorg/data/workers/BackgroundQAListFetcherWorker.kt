package io.github.kabirnayeem99.islamqaorg.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

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
        fun enqueue(workManager: WorkManager): Flow<kotlin.Result<Boolean>>? {
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
                workManager.enqueueUniquePeriodicWork(
                    TAG, ExistingPeriodicWorkPolicy.KEEP, request
                )
                val workManagerFlow = workManager.getWorkInfosByTagFlow(TAG)
                return workManagerFlow.map { infoList ->
                    infoList.firstOrNull()?.let { info ->
                        val currentState = info.state
                        val failed = currentState == WorkInfo.State.FAILED
                        val successful = currentState == WorkInfo.State.SUCCEEDED
                        if (failed) kotlin.Result.failure(Exception())
                        else if (successful) kotlin.Result.success(true)
                        else kotlin.Result.failure(Exception())
                    } ?: kotlin.Result.failure(Exception())
                }
//                    workManager.getWorkInfosByTagFlow(TAG).collect { infoList ->
//                        infoList.firstOrNull()?.let { info ->
//                            val currentState = info.state
//                            val failed = currentState == WorkInfo.State.FAILED
//                            val successful = currentState == WorkInfo.State.SUCCEEDED
//                            if (successful) onSuccess() else if (failed) onError()
//                        }
//                    }
            } catch (e: Exception) {
                Timber.e("schedulePeriodicSync: ${e.localizedMessage}", e)
                return null
            }
        }
    }
}