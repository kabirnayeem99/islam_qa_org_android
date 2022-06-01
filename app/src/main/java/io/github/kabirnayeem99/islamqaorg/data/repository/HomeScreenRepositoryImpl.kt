package io.github.kabirnayeem99.islamqaorg.data.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
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
@Inject constructor(private val remoteDataSource: IslamQaRemoteDataSource) : HomeScreenRepository {

    private val inMemoryMutex = Mutex()
    private var inMemoryHomeScreenData = emptyList<Question>()

    override suspend fun getQuestionList(shouldRefresh: Boolean): Flow<Resource<List<Question>>> {
        val cachedList = inMemoryMutex.withLock { inMemoryHomeScreenData }
        return flow {
            if (shouldRefresh || cachedList.isEmpty()) {
                try {
                    val homeScreen = remoteDataSource.getHomeScreenData()
                    inMemoryMutex.withLock { inMemoryHomeScreenData = homeScreen }
                    emit(Resource.Success(homeScreen))
                } catch (e: Exception) {
                    Timber.e(e, "Failed to get home screen data -> ${e.localizedMessage}.")
                    emit(Resource.Error(e.localizedMessage ?: "Failed to get home screen data."))
                }
            }
        }.onStart {
            if (cachedList.isNotEmpty())
                emit(Resource.Success(cachedList))
            else Resource.Loading<List<Question>>()
        }.flowOn(Dispatchers.IO)
    }
}