package io.github.kabirnayeem99.islamqaorg.domain.repository

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.flow.Flow

interface QuestionAnswerRepository {

    /**
     * Fetches the list of random questions
     *
     * @param shouldRefresh Boolean - This is a flag that tells the repository to refresh the data from
     * the remote data source.
     * @return A flow of [QuestionDetail] wrapped in [Resource].
     */
    suspend fun getRandomQuestionList(shouldRefresh: Boolean = false): Flow<Resource<List<Question>>>

    /**
     * Gets questions list based on the preferred [Fiqh] of the user
     *
     * @param pageNumber Int - the page number from which the questions will be loaded.
     * @param shouldRefresh Boolean - whether the data should be fetched from website or from local cache
     * @return a flow of the [Question] list wrapped in a [Resource] class.
     */
    suspend fun getFiqhBasedQuestionList(
        pageNumber: Int,
        shouldRefresh: Boolean = false
    ): Flow<Resource<List<Question>>>

    /**
     * Fetches the question details based on the URL
     *
     * @param url The url of the question to be fetched.
     * @return A flow of [QuestionDetail] wrapped in [Resource].
     */
    suspend fun getQuestionDetails(url: String): Flow<Resource<QuestionDetail>>

}