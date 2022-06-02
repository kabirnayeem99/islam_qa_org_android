package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.flow.Flow

interface QuestionAnswerRepository {
    /**
     * Fetches home screen data
     *
     * @param shouldRefresh Boolean - This is a flag that tells the repository whether to fetch the
     * data from the network or from the in memory cache.
     *
     * @return a flow of list of questions
     */
    suspend fun getRandomQuestionList(shouldRefresh: Boolean = false): Flow<Resource<List<Question>>>

    /**
     * Fetches detailed answer of the questions
     *
     * @param url of the questions
     * @return a flow of [QuestionDetail] wrapped in a [Resource] class
     */
    suspend fun getQuestionDetails(url: String): Flow<Resource<QuestionDetail>>

}