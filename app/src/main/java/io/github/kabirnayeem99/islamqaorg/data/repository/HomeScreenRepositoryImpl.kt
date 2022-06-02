package io.github.kabirnayeem99.islamqaorg.data.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.common.utility.NetworkUtil
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.repository.HomeScreenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

class HomeScreenRepositoryImpl
@Inject constructor(
    private val remoteDataSource: IslamQaRemoteDataSource,
    private val localDataSource: IslamQaLocalDataSource,
    private val preferenceDataSource: PreferenceDataSource,
    private val networkUtil: NetworkUtil,
) : HomeScreenRepository {

    private val isNetworkAvailable by lazy {
        networkUtil.isNetworkAvailable
    }

    private val inMemoryMutex = Mutex()
    private var inMemoryHomeScreenData = emptyList<Question>()

    override suspend fun getQuestionList(shouldRefresh: Boolean): Flow<Resource<List<Question>>> {
        val cachedList = inMemoryMutex.withLock { inMemoryHomeScreenData }
        return flow {
            val needRefresh = preferenceDataSource.checkIfNeedsRefreshing()
            if ((shouldRefresh || needRefresh) && isNetworkAvailable) {
                val remoteData = getQuestionListFromRemoteDataSource()
                emit(remoteData)
            } else {
                try {
                    val homeScreen = localDataSource.getQuestionList()
                    inMemoryMutex.withLock { inMemoryHomeScreenData = homeScreen }
                    preferenceDataSource.updateNeedingToRefresh()
                    if (homeScreen.isEmpty()) {
                        val remoteData = getQuestionListFromRemoteDataSource()
                        emit(remoteData)
                    }
                    emit(Resource.Success(homeScreen))
                } catch (e: Exception) {
                    Timber.e(e, "Failed to get cached question list -> ${e.localizedMessage}.")
                    emit(Resource.Error(e.localizedMessage ?: "Failed to get home screen data."))
                }
            }
        }.onStart {
            if (cachedList.isNotEmpty())
                emit(Resource.Success(cachedList))
            else emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun getQuestionListFromRemoteDataSource(): Resource<List<Question>> {
        return try {
            val homeScreen = remoteDataSource.getHomeScreenData()
            inMemoryMutex.withLock { inMemoryHomeScreenData = homeScreen }
            localDataSource.cacheQuestionList(homeScreen)
            preferenceDataSource.updateNeedingToRefresh()
            Resource.Success(homeScreen)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get home screen data -> ${e.localizedMessage}.")
            Resource.Error(e.localizedMessage ?: "Failed to get home screen data.")
        }
    }

    private var inMemoryQuestionDetail = QuestionDetail()

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
}