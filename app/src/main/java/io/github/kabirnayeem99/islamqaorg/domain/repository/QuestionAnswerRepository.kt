package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.flow.Flow

interface QuestionAnswerRepository {

    suspend fun getRandomQuestionList(shouldRefresh: Boolean = false): Flow<List<Question>>

    suspend fun fetchAndSaveQuestionListByFiqh(setProgress: suspend (Int) -> Unit = {}): Boolean

    suspend fun getFiqhBasedQuestionList(
        pageNumber: Int, shouldRefresh: Boolean = false
    ): Flow<List<Question>>

    suspend fun getQuestionDetails(url: String): Flow<QuestionDetail>

    suspend fun searchQuestions(query: List<String>): Flow<List<Question>>

}