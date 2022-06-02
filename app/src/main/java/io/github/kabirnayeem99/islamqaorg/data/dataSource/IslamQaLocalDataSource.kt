package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionListDao
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import timber.log.Timber
import javax.inject.Inject

class IslamQaLocalDataSource @Inject constructor(
    private val questionListDao: QuestionListDao,
) {

    /**
     * Fetches all the questions from the database and returns a list of Question objects
     *
     * @return A list of Question objects
     */
    suspend fun getHomeScreenData(): List<Question> {
        val questions = questionListDao.getAllQuestions().map {
            Question(it.id, it.question, it.url)
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
    suspend fun cacheHomeScreenData(questions: List<Question>) {
        try {
            val questionEntities = questions.map {
                QuestionEntity(
                    id = it.id,
                    question = it.question,
                    url = it.url
                )
            }
            questionListDao.insertAllQuestions(questionEntities)
        } catch (e: Exception) {
            Timber.e(e, "failed to cache home screen data -> ${e.localizedMessage}.")
        }
    }
}