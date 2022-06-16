package io.github.kabirnayeem99.islamqaorg.data.dataSource.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.kabirnayeem99.islamqaorg.R
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val syncUtils: SyncUtils,
) : Worker(context, params) {


    override fun doWork(): Result {
        return runBlocking {
            val needSyncing = syncUtils.checkIfNeedsSyncing()
            if (needSyncing) {
                Timber.d("Running work manager -> doWork.")
                syncUtils.makeStatusNotification(context.getString(R.string.label_syncing), context)
                val isSuccessful = syncUtils.syncAllQuestionsInBackground()
                syncUtils.makeStatusNotification(context.getString(R.string.label_synced), context)
                Timber.d("Finished running work managers")
                if (isSuccessful) Result.success() else Result.failure()
            } else {
                Timber.d("We don't need syncing now.")
                Result.failure()
            }
        }
    }
}