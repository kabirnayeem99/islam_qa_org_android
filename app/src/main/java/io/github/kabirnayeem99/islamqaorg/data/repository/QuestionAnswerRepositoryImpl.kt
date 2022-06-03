package io.github.kabirnayeem99.islamqaorg.data.repository

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

    /**
     * Fetches the list of random questions
     *
     * If the network is available, gets the data from the remote data source,
     * otherwise get the data from the local data source
     *
     * @param shouldRefresh Boolean - This is a flag that tells the repository to refresh the data from
     * the remote data source.
     * @return A Flow of Resource<List<Question>>
     */
    override suspend fun getRandomQuestionList(shouldRefresh: Boolean): Flow<Resource<List<Question>>> {
        val cachedRandomQuestionList = inMemoryMutex.withLock { inMemoryRandomQuestionList }
        return flow {
            val needRefresh = preferenceDataSource.checkIfNeedsRefreshing()
            if (isNetworkAvailable) {
                val remoteData = getRandomQuestionListFromRemoteDataSource()
                emit(remoteData)
            } else {
                try {
                    val localQuestionList = localDataSource.getQuestionList()
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
            if (cachedRandomQuestionList.isNotEmpty())
                emit(Resource.Success(cachedRandomQuestionList))
            else emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetches the data from the remote data source, caches it in the local data source, and then
     * returns the data to the caller
     *
     * @return A Resource object that contains a list of Question objects.
     */
    private suspend fun getRandomQuestionListFromRemoteDataSource(): Resource<List<Question>> {
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

    private var inMemoryQuestionDetail = QuestionDetail()

    /**
     * Fetches the question details based on the URL
     *
     * First checks if the question detail is already in memory, if it is, it emits it.
     * If not, it checks if the question detail is in the local database, if it is, it emits it.
     *
     * If not, it checks if the network is available, if it is, it fetches the question detail from
     * the remote data source, caches it in the local database and emits it.
     * If the network is not available, it emits an error
     *
     * @param url The url of the question to be fetched.
     * @return A flow of resources.
     */
    override suspend fun getQuestionDetails(url: String): Flow<Resource<QuestionDetail>> {
        val questionDetail = inMemoryMutex.withLock { inMemoryQuestionDetail }
        return flow {
            try {
                val questionDetailLocal = localDataSource.getDetailedQuestionAndAnswer(url)
                questionDetailLocal?.let {
                    inMemoryMutex.withLock { inMemoryQuestionDetail = it }
                    emit(Resource.Success(it))
                }

                if (isNetworkAvailable) {
                    val questionDetailed = remoteDataSource.getDetailedQuestionAndAnswer(url)
                    inMemoryMutex.withLock { inMemoryQuestionDetail = questionDetailed }
                    localDataSource.cacheQuestionDetail(questionDetail)
                    emit(Resource.Success(questionDetailed))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Failed to get the detailed question."))
            }
        }.onStart {
            if (questionDetail.originalLink == url) emit(Resource.Success(questionDetail))
            else emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)
    }


    private fun getCurrentlySelectedFiqh(): Fiqh {
        return Fiqh.HANAFI
    }

    private var inMemoryFiqhBasedQuestionList = emptyList<Question>()

    override suspend fun getFiqhBasedQuestionList(
        pageNumber: Int,
        shouldRefresh: Boolean,
    ): Flow<Resource<List<Question>>> {
        val cachedFiqhBasedQuestionList = inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList }
        return flow {
            val fiqh = getCurrentlySelectedFiqh()
            if (isNetworkAvailable) {
                val remoteData = getFiqhBasedQuestionListFromRemoteDataSource(fiqh, pageNumber)
                emit(remoteData)
            } else {
                try {
                    val localQuestionList = localDataSource.getQuestionList()
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
            if (cachedFiqhBasedQuestionList.isNotEmpty())
                emit(Resource.Success(cachedFiqhBasedQuestionList))
            else emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Fetches the data from the remote data source, caches it in the local data source, and then
     * returns the data to the caller
     *
     * @return A Resource object that contains a list of Question objects.
     */
    private suspend fun getFiqhBasedQuestionListFromRemoteDataSource(
        fiqh: Fiqh,
        pageNumber: Int
    ): Resource<List<Question>> {
        return try {
            val qList = remoteDataSource.getFiqhBasedQuestionsList(fiqh, pageNumber)
            inMemoryMutex.withLock { inMemoryFiqhBasedQuestionList = qList }
            localDataSource.cacheQuestionList(qList)
            preferenceDataSource.updateNeedingToRefresh()
            Resource.Success(qList)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get fiqh based question data -> ${e.localizedMessage}.")
            Resource.Error(e.localizedMessage ?: "Failed to get home screen data.")
        }
    }
}