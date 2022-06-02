package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.repository.HomeScreenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeScreenData
@Inject constructor(private val homeScreenRepository: HomeScreenRepository) {
    suspend fun getHomeScreenData(shouldRefresh: Boolean): Flow<Resource<List<Question>>> {
        return homeScreenRepository.getQuestionList(shouldRefresh)
    }
}