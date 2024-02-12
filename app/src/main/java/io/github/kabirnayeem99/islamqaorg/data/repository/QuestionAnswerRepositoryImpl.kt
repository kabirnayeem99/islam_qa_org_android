package io.github.kabirnayeem99.islamqaorg.data.repository

import android.util.LruCache
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class QuestionAnswerRepositoryImpl
@Inject constructor(
    private val remoteDataSource: IslamQaRemoteDataSource,
    private val localDataSource: IslamQaLocalDataSource,
    private val preferenceDataSource: PreferenceDataSource,
) : QuestionAnswerRepository {

    override suspend fun getRandomQuestionList(): Flow<List<Question>> {
        return localDataSource.getRandomQuestionList().catch { e ->
            Timber.w(e)
            emit(emptyList())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getFiqhBasedQuestionList(
        pageNumber: Int
    ): Flow<List<Question>> {
        val fiqh = getCurrentFiqh()
        return localDataSource.getFiqhBasedQuestionListByPage(fiqh, pageNumber).catch { e ->
            Timber.w(e)
            emit(emptyList())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun fetchAndSaveQuestionListByFiqh(setProgress: suspend (Int) -> Unit): Boolean {
        try {
            var currentProgress = 0
            val fiqh = getCurrentFiqh()

            val lastSyncedPage = preferenceDataSource.getCurrentFiqhLastPageSynced()
            val firstTime = lastSyncedPage < 1
            val newStartingPage = lastSyncedPage + 1
            val newLastSyncingPage = newStartingPage + (if (firstTime) 2 else 10)

            for (page in newStartingPage..newLastSyncingPage) {

                val list = remoteDataSource.getFiqhBasedQuestionsList(fiqh, page)

                if (list.isEmpty()) {
                    throw IllegalStateException("Failed with page $page for fiqh ${fiqh.displayName}")
                }

                list.forEachIndexed { index, q ->
                    delay(Random.nextLong((index + 1) * 100L))
                    val questionDetailed = remoteDataSource.getDetailedQuestionAndAnswer(q.url)
                    localDataSource.cacheQuestionDetail(questionDetailed)
                    if (firstTime) currentProgress += 50 else currentProgress++
                    setProgress(currentProgress)
                }

                preferenceDataSource.saveCurrentFiqhLastPageSynced(newLastSyncingPage)
            }
            return true
        } catch (e: Exception) {
            Timber.e(e, "fetchAndSaveRandomQuestionList: ${e.localizedMessage}", e)
            return false
        }
    }

    private val inMemoryQuestionDetails: LruCache<String, QuestionDetail> = LruCache(18)

    override suspend fun getQuestionDetails(url: String): Flow<QuestionDetail> {
        val inMemoryQuestionDetail = inMemoryQuestionDetails.get(url) ?: QuestionDetail()
        return flow {
            val questionDetailLocal = localDataSource.getDetailedQuestionAndAnswer(url)
            inMemoryQuestionDetails.put(url, questionDetailLocal)
            emit(questionDetailLocal)
        }.catch { e ->
            Timber.w(e)
            emit(inMemoryQuestionDetail)
        }.onStart { emit(inMemoryQuestionDetail) }.flowOn(Dispatchers.IO)
    }


    override suspend fun searchQuestions(query: List<String>): Flow<List<Question>> {
        return flow {
            val searchResult = localDataSource.searchFiqhBasedQuestionList(query)
            emit(searchResult)
        }
    }

    override suspend fun getCurrentFiqh(): Fiqh {
        val preferredFiqh = preferenceDataSource.getPreferredFiqh()
        if (preferredFiqh == Fiqh.UNKNOWN) {
            preferenceDataSource.savePreferredFiqh(Fiqh.HANAFI)
            return Fiqh.HANAFI
        }
        return preferredFiqh
    }
}
