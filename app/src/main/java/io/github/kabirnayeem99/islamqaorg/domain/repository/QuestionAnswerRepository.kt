package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.flow.Flow

interface QuestionAnswerRepository {

    suspend fun getRandomQuestionList(shouldRefresh: Boolean = false): Flow<Resource<List<Question>>>

    suspend fun getFiqhBasedQuestionList(
        pageNumber: Int,
        shouldRefresh: Boolean = false
    ): Flow<Resource<List<Question>>>

    /**
     * Fetches detailed answer of the questions
     *
     * @param url of the questions
     * @return a flow of [QuestionDetail] wrapped in a [Resource] class
     */
    suspend fun getQuestionDetails(url: String): Flow<Resource<QuestionDetail>>

}