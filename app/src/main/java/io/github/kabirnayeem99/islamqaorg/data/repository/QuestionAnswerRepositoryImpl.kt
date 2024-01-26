package io.github.kabirnayeem99.islamqaorg.data.repository

import android.util.LruCache
import io.github.kabirnayeem99.islamqaorg.common.EmptyCacheException
import io.github.kabirnayeem99.islamqaorg.common.NoNetworkException
import io.github.kabirnayeem99.islamqaorg.common.utility.NetworkUtil
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class QuestionAnswerRepositoryImpl
@Inject constructor(
    private val remoteDataSource: IslamQaRemoteDataSource,
    private val localDataSource: IslamQaLocalDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val networkUtil: NetworkUtil,
) : QuestionAnswerRepository {

    private val isNetworkAvailable by lazy { networkUtil.isNetworkAvailable }

    private val inMemoryMutex by lazy { Mutex() }

    private var inMemoryRandomQuestionList = emptyList<Question>()

    override suspend fun getRandomQuestionList(shouldRefresh: Boolean): Flow<List<Question>> {
        return flow {
            val localQuestionList = localDataSource.getRandomQuestionList()
            inMemoryRandomQuestionList = localQuestionList
            emit(localQuestionList)
        }.onStart {
            if (inMemoryRandomQuestionList.isNotEmpty()) emit(inMemoryRandomQuestionList)
        }.catch { e ->
            Timber.e(e, "getRandomQuestionList: ")
            emit(inMemoryFiqhBasedQuestionList)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun fetchAndSaveQuestionListByFiqh(setProgress: suspend (Int) -> Unit): Boolean {
        try {
            val fiqh = getCurrentlySelectedFiqh()
            val lastSyncedPage = preferenceDataSource.getCurrentFiqhLastPageSynced()
            val newStartingPage = lastSyncedPage + 1
            val newLastSyncingPage = newStartingPage + 10
            for (page in newStartingPage..newLastSyncingPage) {
                val list = getFiqhBasedQuestionListFromRemoteDataSource(fiqh, page)
                if (list.isEmpty()) {
                    throw IllegalStateException("Failed with page $page for fiqh ${fiqh.displayName}")
                } else {
                    localDataSource.cacheQuestionList(list)
                    list.forEachIndexed { index, q ->
                        delay(Random.nextLong(q.url.length.toLong()))
                        getAndCacheQuestionDetailFromRemote(q.url)
                        setProgress(((page + 1) - newStartingPage) * (index + 1))
                    }
                    preferenceDataSource.saveCurrentFiqhLastPageSynced(newLastSyncingPage)
                    Timber.i("fetchAndSaveRandomQuestionList: saved page $page for fiqh ${fiqh.displayName}")
                }
            }
            return true
        } catch (e: Exception) {
            Timber.e("fetchAndSaveRandomQuestionList: ${e.localizedMessage}", e)
            return false
        }
    }


    private suspend fun getRandomQuestionListFromRemoteDataSource(): List<Question> {
        return try {
            val randomQuestionList = remoteDataSource.getRandomQuestionsList()
            inMemoryMutex.withLock { inMemoryRandomQuestionList = randomQuestionList }
            localDataSource.cacheQuestionList(randomQuestionList)
            preferenceDataSource.updateNeedingToRefresh()
            randomQuestionList
        } catch (e: Exception) {
            Timber.e(e, "getRandomQuestionListFromRemoteDataSource: " + e.localizedMessage)
            emptyList()
        }
    }

    private val inMemoryQuestionDetails: LruCache<String, QuestionDetail> = LruCache(18)

    override suspend fun getQuestionDetails(url: String): Flow<QuestionDetail> {
        val inMemoryQuestionDetail = inMemoryQuestionDetails.get(url) ?: QuestionDetail()
        return flow {
            try {
                val questionDetailLocal = localDataSource.getDetailedQuestionAndAnswer(url)
                inMemoryQuestionDetails.put(url, questionDetailLocal)
                emit(questionDetailLocal)

                if (!isNetworkAvailable) throw NoNetworkException()

                val questionDetailRemote = getAndCacheQuestionDetailFromRemote(url)
                emit(questionDetailRemote)
            } catch (e: EmptyCacheException) {
                val questionDetailRemote = getAndCacheQuestionDetailFromRemote(url)
                emit(questionDetailRemote)
            }
        }.onStart { emit(inMemoryQuestionDetail) }
    }

    private suspend fun getAndCacheQuestionDetailFromRemote(url: String): QuestionDetail {
        val questionDetailed = remoteDataSource.getDetailedQuestionAndAnswer(url)
        localDataSource.cacheQuestionDetail(questionDetailed)
        return questionDetailed
    }

    private suspend fun getCurrentlySelectedFiqh(): Fiqh {
        val preferredFiqh = preferenceDataSource.getPreferredFiqh()
        if (preferredFiqh == Fiqh.UNKNOWN) {
            preferenceDataSource.savePreferredFiqh(Fiqh.HANAFI)
            return Fiqh.HANAFI
        }
        return preferredFiqh
    }

    private var inMemoryFiqhBasedQuestionList = emptyList<Question>()


    override suspend fun getFiqhBasedQuestionList(
        pageNumber: Int,
        shouldRefresh: Boolean,
    ): Flow<List<Question>> {
        val cachedFiqhBasedQuestionList = inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList }
        return flow {
            val fiqh = getCurrentlySelectedFiqh()
            if (isNetworkAvailable) {
                val remoteData = getFiqhBasedQuestionListFromRemoteDataSource(fiqh, pageNumber)
                emit(remoteData)
            } else {
                val localQuestionList = localDataSource.getFiqhBasedQuestionList(fiqh)
                if (localQuestionList.isEmpty()) {
                    val remoteData = getRandomQuestionListFromRemoteDataSource()
                    emit(remoteData)
                }
                inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList = localQuestionList }
                preferenceDataSource.updateNeedingToRefresh()
                emit(localQuestionList)
            }
        }.onStart {
            if (cachedFiqhBasedQuestionList.isNotEmpty()) emit(cachedFiqhBasedQuestionList)
        }
    }


    private suspend fun getFiqhBasedQuestionListFromRemoteDataSource(
        fiqh: Fiqh, pageNumber: Int
    ): List<Question> {
        return try {
            val qList = remoteDataSource.getFiqhBasedQuestionsList(fiqh, pageNumber)
            inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList = qList }
            localDataSource.cacheQuestionList(qList, fiqh)
            preferenceDataSource.updateNeedingToRefresh()
            qList
        } catch (e: Exception) {
            emptyList()
        }
    }


    override suspend fun searchQuestions(query: List<String>): Flow<List<Question>> {
        return flow {
            val searchResult = localDataSource.searchFiqhBasedQuestionList(query)
            emit(searchResult)
        }
    }
}
