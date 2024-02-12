package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.common.EmptyCacheException
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionAnswerDao
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.generateSearchQuery
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestionDetail
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestionEntity
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestions
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class IslamQaLocalDataSource @Inject constructor(
    private val questionAnswerDao: QuestionAnswerDao,
) {

    fun getRandomQuestionList(): Flow<List<Question>> {
        return questionAnswerDao.getRandomQuestions().map { qs -> qs.toQuestions() }
    }

    suspend fun getRandomQuestionListByFiqhAsync(fiqh: String): List<Question> {
        return questionAnswerDao.getRandomQuestionListByFiqhAsync(fiqh).toQuestions()
    }

    fun getFiqhBasedQuestionListByPage(fiqh: Fiqh, pageNumber: Int): Flow<List<Question>> {
        val limit = 10
        val offset = if (pageNumber != 0 && pageNumber > 1) pageNumber * limit else 0
        return questionAnswerDao.getFiqhBasedQuestions(fiqh.paramName, limit, offset)
            .map { qs -> qs.toQuestions() }
    }


    suspend fun searchFiqhBasedQuestionList(query: List<String>): List<Question> {
        try {
            val searchQuery = generateSearchQuery(query)
            val questions = questionAnswerDao.searchQuestions(searchQuery).toQuestions()
            if (questions.isEmpty()) throw EmptyCacheException()
            return questions
        } catch (e: Exception) {
            Timber.e(e, "getFiqhBasedQuestionList: " + e.localizedMessage)
            throw EmptyCacheException()
        }
    }


    suspend fun cacheQuestionDetail(questionDetail: QuestionDetail) {
        try {
            val questionDetailEntity = questionDetail.toQuestionEntity()
            questionAnswerDao.insertQuestion(questionDetailEntity)
        } catch (e: Exception) {
            Timber.e(e, "failed to cache home screen data -> ${e.localizedMessage}.")
        }
    }


    suspend fun getDetailedQuestionAndAnswer(url: String): QuestionDetail {
        return try {
            questionAnswerDao.getQuestionByLink(url)
                ?.toQuestionDetail { f -> getRandomQuestionListByFiqhAsync(f) }
                ?: throw EmptyCacheException()
        } catch (e: Exception) {
            Timber.e(e, "Failed to get cached detail answer.")
            throw EmptyCacheException()
        }
    }
}
