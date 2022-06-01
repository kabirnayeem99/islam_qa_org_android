package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.data.dataSource.service.ScrapingService
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
}