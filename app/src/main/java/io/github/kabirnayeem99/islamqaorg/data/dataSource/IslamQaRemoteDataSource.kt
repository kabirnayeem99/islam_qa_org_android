package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.data.dataSource.service.ScrapingService
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class IslamQaRemoteDataSource @Inject constructor(private val scrapingService: ScrapingService) {
    /**
     * Gets the home screen data from the website and returns it as a list of pairs of questions and
     * their answers
     *
     * @return A list of pairs of questions and their links.
     */
    suspend fun getHomeScreenData(): List<Question> {
        return withContext(Dispatchers.IO) {
            val home = scrapingService.parseHomeScreen()

            if (home.httpStatusCode == 404)
                throw Exception("Failed to parse the page")
            if (home.questionLinks.isEmpty() || home.questions.isEmpty())
                throw Exception("No questions were found.")
            if (home.questions.size != home.questionLinks.size)
                throw Exception("Failed to get answers for some questions")

            val questionAnswer = mutableListOf<Question>()
            home.questions.forEachIndexed { index, question ->
                val answerLink = home.questionLinks[index]
                questionAnswer.add(Question(index, question, answerLink))
            }

            questionAnswer.ifEmpty { throw Exception("Failed to parse links of the questions") }
        }
    }

    suspend fun getDetailedQuestionAndAnswer(url: String): QuestionDetail {

        return withContext(Dispatchers.IO) {

            Timber.d("Loading question answer of $url")

            val questionList = mutableListOf<Question>()

            for (i in 1..8) {
                val question = Question(
                    id = Random.nextInt(10),
                    "Is zakat wajib on a student getting scholarship?",
                    url
                )
                questionList.add(question)
            }

            delay(2000)

            val dto = scrapingService.parseQuestionDetailScreen(url)

            val detail = QuestionDetail(
                questionTitle = dto.questionTitle,
                detailedQuestion = dto.detailedQuestion,
                detailedAnswer = dto.detailedAnswer,
                fiqh = dto.fiqh,
                source = dto.source,
                originalLink = dto.originalLink,
                nextQuestionLink = dto.nextQuestionLink,
                previousQuestionLink = dto.previousQuestionLink,
                relevantQuestions = dto.relevantQuestions,
            )

            Timber.d(detail.toString())

            detail
        }
    }
}