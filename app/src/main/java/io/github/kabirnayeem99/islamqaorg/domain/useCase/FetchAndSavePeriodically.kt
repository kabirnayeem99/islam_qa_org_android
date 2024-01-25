package io.github.kabirnayeem99.islamqaorg.domain.useCase

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.github.kabirnayeem99.islamqaorg.data.workers.BackgroundQAListFetcherWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FetchAndSavePeriodically
@Inject constructor(private val workManager: WorkManager) {

    operator fun invoke() = schedulePeriodicSync()

    private fun schedulePeriodicSync() {
        try {
            val tag = BackgroundQAListFetcherWorker.TAG

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
                addTag(tag)
            }
            val request = periodicRequestBuilder.build()
            workManager.enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.KEEP, request)
        } catch (e: Exception) {
            Timber.e("schedulePeriodicSync: ${e.localizedMessage}", e)
        }
    }

    companion object {
        private const val SYNC_INTERVAL_HOURS = 4L
    }
}