package io.github.kabirnayeem99.islamqaorg.data.dataSource.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random


val SYNC_NOTIFICATION_CHANNEL_NAME: CharSequence = "Background Sync Status"
const val SYNC_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notification when background syncing starts."

val SYNC_NOTIFICATION_TITLE: CharSequence = "Q&A Sync"
const val CHANNEL_ID = "SYNC_NOTIFICATION"
const val NOTIFICATION_ID = 1

class SyncUtils @Inject constructor(
    private val remoteDataSource: IslamQaRemoteDataSource,
    private val localDataSource: IslamQaLocalDataSource,
    private val preferenceDataSource: PreferenceDataSource,
) {

    private var questionCounter = 0

    fun checkIfWeCanCacheMore(): Boolean {
        return questionCounter < 500
    }

    /**
     * Creates a notification channel, then creates a notification using that channel for indicating
     * syncing status
     *
     * @param message The message you want to display in the notification.
     * @param context The context of the app.
     */
    fun makeStatusNotification(message: String, context: Context) {

        val name = SYNC_NOTIFICATION_CHANNEL_NAME
        val description = SYNC_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_islamic_geometric)
            .setContentTitle(SYNC_NOTIFICATION_TITLE)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVibrate(LongArray(0))

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

    @WorkerThread
    /**
     * Checks if the app needs to sync data with the server
     *
     * @return Boolean
     */
    suspend fun checkIfNeedsSyncing(): Boolean {
        return preferenceDataSource.checkIfNeedsSyncing()
    }

    @WorkerThread
    /**
     * Fetches the list of random questions and fiqh based questions from the remote server, and
     * then fetches the details of each question and caches it
     *
     * @return A Boolean value, whether the syncing was finished or not.
     */
    suspend fun syncAllQuestionsInBackground(): Boolean {
        return try {

            val randomDelayToAvoidBlock = Random.nextLong(200, 500)

            val randomQuestions = fetchCacheAndGetRandomQuestionsFromRemote()
            val fiqhBasedQuestions = fetchCacheAndGetFiqhBasedQuestionsFromRemote()

            coroutineScope {
                listOf(
                    async {
                        randomQuestions.forEach { q ->
                            delay(randomDelayToAvoidBlock)
                            if (checkIfWeCanCacheMore()) fetchAndCacheQuestionsDetails(q)
                        }
                    },
                    async {
                        fiqhBasedQuestions.forEach { q ->
                            delay(randomDelayToAvoidBlock)
                            if (checkIfWeCanCacheMore()) fetchAndCacheQuestionsDetails(q)
                        }
                    },
                ).awaitAll()
            }

            preferenceDataSource.updateSyncingStatus()

            true
        } catch (e: Exception) {
            Timber.e(e, "Failed syncFiqhBasedQuestionsInBackground -> ${e.message}")
            false
        }
    }

    @WorkerThread
    /**
     * Fetches the detailed question and answer from the remote data source and caches it in the
     * local data source
     *
     * @param question Question - The question object that we want to fetch the details for.
     */
    private suspend fun fetchAndCacheQuestionsDetails(question: Question) {
        try {

            val randomDelayToAvoidBlock = Random.nextLong(200, 500)

            val details = remoteDataSource.getDetailedQuestionAndAnswer(question.url)
            localDataSource.cacheQuestionDetail(details)

            details.relevantQuestions.forEach {
                delay(randomDelayToAvoidBlock)
                if (checkIfWeCanCacheMore()) fetchAndCacheQuestionsDetails(it)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed fetchAndCacheQuestionsDetails -> ${e.message}")
        }
    }


    /**
     * Fetches a list of random questions from the remote data source, caches them in the local
     * data source, and then returns the list of questions
     *
     * @return A list of questions
     */
    @WorkerThread
    private suspend fun fetchCacheAndGetRandomQuestionsFromRemote(): List<Question> {
        return try {
            val randomQuestions = remoteDataSource.getRandomQuestionsList()
            localDataSource.cacheQuestionList(randomQuestions)
            preferenceDataSource.updateNeedingToRefresh()
            randomQuestions
        } catch (e: Exception) {
            Timber.e(e, "Failed to get home screen data -> ${e.localizedMessage}.")
            emptyList()
        }
    }


    /**
     * Fetches the preferred fiqh from the preference data source, then fetches the questions from
     * the remote data source, then caches the questions in the local data source, then updates the
     * preference data source to indicate that the data is no longer stale, and finally returns the
     * questions
     *
     * @return A list of questions.
     */
    @WorkerThread
    private suspend fun fetchCacheAndGetFiqhBasedQuestionsFromRemote(): List<Question> {
        return try {

            val fiqh = preferenceDataSource.getPreferredFiqh()
            val randomQuestions = remoteDataSource.getFiqhBasedQuestionsList(fiqh, 1)

            localDataSource.cacheQuestionList(randomQuestions, fiqh)
            preferenceDataSource.updateNeedingToRefresh()

            randomQuestions
        } catch (e: Exception) {
            Timber.e(e, "Failed to get fiqh-based questions data -> ${e.localizedMessage}.")
            emptyList()
        }
    }

}