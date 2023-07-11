package io.github.kabirnayeem99.islamqaorg.data.repository

import android.util.LruCache
import io.github.kabirnayeem99.islamqaorg.common.EmptyCacheException
import io.github.kabirnayeem99.islamqaorg.common.NoNetworkException
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.common.utility.NetworkUtil
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

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
        val cachedRandomQuestionList = inMemoryMutex.withLock { inMemoryRandomQuestionList }
        return flow {
            if (isNetworkAvailable) {
                val remoteData = getRandomQuestionListFromRemoteDataSource()
                emit(remoteData)
            } else {
                try {
                    val localQuestionList = localDataSource.getRandomQuestionList()
                    if (localQuestionList.isEmpty()) {
                        val remoteData = getRandomQuestionListFromRemoteDataSource()
                        emit(remoteData)
                    }
                    inMemoryMutex.withLock { inMemoryRandomQuestionList = localQuestionList }
                    preferenceDataSource.updateNeedingToRefresh()
                    emit(Resource.Success(localQuestionList))
                } catch (e: Exception) {
                    Timber.e(e, "Failed to get cached question list -> ${e.localizedMessage}.")
                    emit(Resource.Error(e.localizedMessage ?: "Failed to get home screen data."))
                }
            }
        }.onStart {
            if (cachedRandomQuestionList.isNotEmpty()) emit(
                Resource.Success(
                    cachedRandomQuestionList
                )
            )
            else emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)
    }


    private suspend fun getRandomQuestionListFromRemoteDataSource(): List<Question> {
        return try {
            val homeScreen = remoteDataSource.getRandomQuestionsList()
            inMemoryMutex.withLock { inMemoryRandomQuestionList = homeScreen }
            localDataSource.cacheQuestionList(homeScreen)
            preferenceDataSource.updateNeedingToRefresh()
            Resource.Success(homeScreen)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get home screen data -> ${e.localizedMessage}.")
            Resource.Error(e.localizedMessage ?: "Failed to get home screen data.")
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
                try {
                    val localQuestionList = localDataSource.getFiqhBasedQuestionList(fiqh)
                    if (localQuestionList.isEmpty()) {
                        val remoteData = getRandomQuestionListFromRemoteDataSource()
                        emit(remoteData)
                    }
                    inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList = localQuestionList }
                    preferenceDataSource.updateNeedingToRefresh()
                    emit(Resource.Success(localQuestionList))
                } catch (e: Exception) {
                    Timber.e(e, "getFiqhBasedQuestionList failed -> ${e.localizedMessage}.")
                    emit(Resource.Error(e.localizedMessage ?: "Failed to get questions."))
                }
            }
        }.onStart {
            if (cachedFiqhBasedQuestionList.isNotEmpty()) emit(
                Resource.Success(
                    cachedFiqhBasedQuestionList
                )
            )
            else emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)
    }


    private suspend fun getFiqhBasedQuestionListFromRemoteDataSource(
        fiqh: Fiqh, pageNumber: Int
    ): List<Question> {
        return try {
            val qList = remoteDataSource.getFiqhBasedQuestionsList(fiqh, pageNumber)
            inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList = qList }
            localDataSource.cacheQuestionList(qList, fiqh)
            preferenceDataSource.updateNeedingToRefresh()
            Resource.Success(qList)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get fiqh based question data -> ${e.localizedMessage}.")
            Resource.Error(e.localizedMessage ?: "Failed to get home screen data.")
        }
    }


    override suspend fun searchQuestions(query: String): Flow<List<Question>> {

        val cachedFiqhBasedQuestionList = inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList }

        return flow {
            if (query.isNotBlank()) {

                val searchResult = localDataSource.searchFiqhBasedQuestionList(query)

                if (searchResult.isNotEmpty()) emit(Resource.Success(searchResult))
                else emit(Resource.Error("Could not find any questions for $query."))

            }
        }.onStart {

            if (query.isBlank()) emit(Resource.Success(cachedFiqhBasedQuestionList))
            else emit(Resource.Loading())

        }.flowOn(Dispatchers.IO)
    }
}