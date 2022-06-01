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
                fiqh = "Hanafi",
                source = "Darulifta-Deoband.com",
                originalLink = "https://islamqa.org/hanafi/darulifta-deoband/109885/i-am-a-phd-student-in-beijing-china-i-get-scholarship-from-september-2011-for-my-food-and-living-expenses-each-month-i-can-roughly-save-25-30000-pkr-money-from-last-two-years-from-my-scholarship-i/",
                nextQuestionLink = "https://islamqa.org/hanafi/darulifta-deoband/109888/i-had-ihtilam-in-night-and-i-perform-fajr-salah-by-washing-only-my-sharmgah-is-it-right-or-i-have-to-do-full-ghusl/",
                previousQuestionLink = "https://islamqa.org/hanafi/darulifta-deoband/109883/can-a-woman-slaughter-an-animal-on-the-occasion-of-eid-ul-azha-and-other-occasions-please-reply-in-light-of-quran-and-sunnah/",
                relevantQuestions = questionList
            )

            Timber.d(detail.toString())

            detail
        }
    }
}