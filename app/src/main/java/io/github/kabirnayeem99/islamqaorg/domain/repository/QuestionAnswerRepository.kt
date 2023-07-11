package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.flow.Flow

interface QuestionAnswerRepository {

    suspend fun getRandomQuestionList(shouldRefresh: Boolean = false): Flow<List<Question>>

    suspend fun getFiqhBasedQuestionList(
        pageNumber: Int, shouldRefresh: Boolean = false
    ): Flow<Resource<List<Question>>>

    suspend fun getQuestionDetails(url: String): Flow<QuestionDetail>

    suspend fun searchQuestions(query: String): Flow<List<Question>>

}