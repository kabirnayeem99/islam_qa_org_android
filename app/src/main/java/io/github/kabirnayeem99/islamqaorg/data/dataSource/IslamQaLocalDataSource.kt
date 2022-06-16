package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionDetailDao
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionListDao
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionDetailEntity
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class IslamQaLocalDataSource @Inject constructor(
    private val questionListDao: QuestionListDao,
    private val questionDetailDao: QuestionDetailDao,
) {

    /**
     * Fetches all the questions from the database and returns a list of Question objects
     *
     * @return A list of Question objects
     */
    suspend fun getRandomQuestionList(): List<Question> {
        val questions = try {
            questionListDao.getRandomQuestions().map {
                Question(it.id, it.question, it.url)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get cached random question list -> ${e.localizedMessage}.")
            emptyList()
        }
        Timber.d(questions.toString())
        return questions
    }

    suspend fun getFiqhBasedQuestionList(fiqh: Fiqh): List<Question> {
        val questions = try {
            questionListDao.getFiqhBasedQuestions(fiqh.paramName).map {
                Question(it.id, it.question, it.url)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get cached fiqh-based question list -> ${e.message}.")
            emptyList()
        }
        Timber.d(questions.toString())
        return questions
    }

    /**
     * Takes a list of questions, converts them to question entities, and inserts them into the
     * database
     *
     * @param questions List<Question> - The list of questions to be cached.
     */
    suspend fun cacheQuestionList(questions: List<Question>, fiqh: Fiqh = Fiqh.UNKNOWN) {
        withContext(Dispatchers.IO) {
            try {
                val questionEntities = questions.map {
                    QuestionEntity(id = it.id, question = it.question, url = it.url, fiqh = fiqh)
                }
                questionListDao.insertAllQuestions(questionEntities)
            } catch (e: Exception) {
                Timber.e(e, "failed to cache home screen data -> ${e.localizedMessage}.")
            }
        }
    }


    suspend fun cacheQuestionDetail(questionDetail: QuestionDetail) {
        withContext(Dispatchers.IO) {
            try {
                val questionDetailEntity = QuestionDetailEntity(
                    questionDetail.questionTitle,
                    questionDetail.detailedQuestion,
                    questionDetail.detailedAnswer,
                    questionDetail.fiqh,
                    questionDetail.source,
                    questionDetail.originalLink,
                    questionDetail.nextQuestionLink,
                    questionDetail.previousQuestionLink,
                )
                questionDetailDao.insertQuestion(questionDetailEntity)
                cacheQuestionList(questionDetail.relevantQuestions)
            } catch (e: Exception) {
                Timber.e(e, "failed to cache home screen data -> ${e.localizedMessage}.")
            }
        }
    }

    suspend fun getDetailedQuestionAndAnswer(url: String): QuestionDetail? {

        return withContext(Dispatchers.IO) {

            try {
                var detail: QuestionDetail? = null

                questionDetailDao.getQuestionByLink(url)?.let { dto ->

                    detail = QuestionDetail(
                        questionTitle = dto.questionTitle,
                        detailedQuestion = dto.detailedQuestion,
                        detailedAnswer = dto.detailedAnswer,
                        fiqh = dto.fiqh,
                        source = dto.source,
                        originalLink = dto.originalLink,
                        nextQuestionLink = dto.nextQuestionLink,
                        previousQuestionLink = dto.previousQuestionLink,
                        relevantQuestions = getRandomQuestionList(),
                    )
                }

                Timber.d(detail.toString())

                detail
            } catch (e: Exception) {
                Timber.e(e, "Failed to get cached detail answer.")
                null
            }
        }
    }
}