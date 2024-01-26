package io.github.kabirnayeem99.islamqaorg.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.data.dataSource.workers.CHANNEL_ID
import io.github.kabirnayeem99.islamqaorg.data.dataSource.workers.NOTIFICATION_ID
import io.github.kabirnayeem99.islamqaorg.data.dataSource.workers.SYNC_NOTIFICATION_CHANNEL_DESCRIPTION
import io.github.kabirnayeem99.islamqaorg.data.dataSource.workers.SYNC_NOTIFICATION_CHANNEL_NAME
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
            createChannel()
            setForeground(createForegroundInfo(1))
            val successfullySynced = questionAnswerRepository.fetchAndSaveQuestionListByFiqh { p ->
                setForeground(createForegroundInfo(p))
            }
            if (successfullySynced) Result.success() else Result.failure()
        }
    }


    private fun createForegroundInfo(progress: Int): ForegroundInfo {
        val title = applicationContext.getString(R.string.notification_title)
        val cancel = applicationContext.getString(R.string.cancel_download)
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        val progressText = "Loading questions: ${progress}%"
        val notification =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID).setContentTitle(title)
                .setTicker(title).setContentText(progressText).setSmallIcon(R.drawable.ic_islamqa)
                .setOngoing(true).addAction(R.drawable.ic_arrow_forward, cancel, intent)
                .setProgress(100, progress, false)
                .build()
        return ForegroundInfo(
            NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    private fun createChannel() {
        val name = SYNC_NOTIFICATION_CHANNEL_NAME
        val description = SYNC_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
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
