package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepository {
    /**
     * Fetches home screen data
     *
     * @param shouldRefresh Boolean - This is a flag that tells the repository whether to fetch the
     * data from the network or from the in memory cache.
     *
     * @return a flow of list of questions
     */
    suspend fun getQuestionList(shouldRefresh: Boolean = false): Flow<Resource<List<Question>>>

}