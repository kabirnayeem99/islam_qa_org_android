package io.github.kabirnayeem99.islamqaorg.domain.useCase

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.github.kabirnayeem99.islamqaorg.data.workers.BackgroundQAListFetcherWorker
import javax.inject.Inject

class FetchAndSavePeriodically
@Inject constructor(private val appContext: Context) {

    operator suspend fun invoke() {
        schedulePeriodicSync(appContext)
    }

    suspend fun schedulePeriodicSync(context: Context) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val periodicRequest = PeriodicWorkRequest.Builder(
            BackgroundQAListFetcherWorker::class.java, SYNC_INTERVAL_HOURS, TimeUnit.HOURS
        ).setConstraints(constraints).addTag(WORK_TAG).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, periodicRequest
        )
    }
}