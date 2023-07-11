package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.common.EmptyCacheException
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionDetailDao
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionListDao
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.generateSearchQuery
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestionDetail
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestionDetailEntity
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestionEntities
import io.github.kabirnayeem99.islamqaorg.data.mappers.toQuestions
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import timber.log.Timber
import javax.inject.Inject

class IslamQaLocalDataSource @Inject constructor(
    private val questionListDao: QuestionListDao,
    private val questionDetailDao: QuestionDetailDao,
) {

    suspend fun getRandomQuestionList(): List<Question> {
        try {
            val randomQuestions = questionListDao.getRandomQuestions().toQuestions()
            if (randomQuestions.isEmpty()) throw EmptyCacheException()
            return randomQuestions
        } catch (e: Exception) {
            Timber.e(e, "getRandomQuestionList: ${e.localizedMessage}")
            throw EmptyCacheException()
        }
    }

    suspend fun getFiqhBasedQuestionList(fiqh: Fiqh): List<Question> {
        try {
            val questions = questionListDao.getFiqhBasedQuestions(fiqh.paramName).toQuestions()
            if (questions.isEmpty()) throw EmptyCacheException()
            return questions
        } catch (e: Exception) {
            Timber.e(e, "getFiqhBasedQuestionList: " + e.localizedMessage)
            throw EmptyCacheException()
        }
    }


    suspend fun searchFiqhBasedQuestionList(query: List<String>): List<Question> {
        try {
            val searchQuery = generateSearchQuery(query)
            val questions = questionListDao.searchQuestions(searchQuery).toQuestions()
            if (questions.isEmpty()) throw EmptyCacheException()
            return questions
        } catch (e: Exception) {
            Timber.e(e, "getFiqhBasedQuestionList: " + e.localizedMessage)
            throw EmptyCacheException()
        }
    }

    suspend fun cacheQuestionList(questions: List<Question>, fiqh: Fiqh = Fiqh.UNKNOWN) {
        try {
            val questionEntities = questions.toQuestionEntities(fiqh)
            questionListDao.insertAllQuestions(questionEntities)
        } catch (e: Exception) {
            Timber.e(e, "failed to cache home screen data -> ${e.localizedMessage}.")
        }
    }


    suspend fun cacheQuestionDetail(questionDetail: QuestionDetail) {
        try {
            val questionDetailEntity = questionDetail.toQuestionDetailEntity()
            questionDetailDao.insertQuestion(questionDetailEntity)
            cacheQuestionList(questionDetail.relevantQuestions)
        } catch (e: Exception) {
            Timber.e(e, "failed to cache home screen data -> ${e.localizedMessage}.")
        }
    }


    suspend fun getDetailedQuestionAndAnswer(url: String): QuestionDetail {
        return try {
            questionDetailDao.getQuestionByLink(url)?.toQuestionDetail { getRandomQuestionList() }
                ?: throw EmptyCacheException()
        } catch (e: Exception) {
            Timber.e(e, "Failed to get cached detail answer.")
            throw EmptyCacheException()
        }
    }
}
