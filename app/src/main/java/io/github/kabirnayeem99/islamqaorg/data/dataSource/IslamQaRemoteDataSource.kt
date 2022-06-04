package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.data.dataSource.service.ScrapingService
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class IslamQaRemoteDataSource @Inject constructor(private val scrapingService: ScrapingService) {

    /**
     * Gets the list of random questions from the IslamQA (Under "Random List of Q&A" heading)
     *
     * @return A list of Question objects.
     */
    suspend fun getRandomQuestionsList(): List<Question> {
        return withContext(Dispatchers.IO) {
            val qList = scrapingService.parseRandomQuestionList()

            Timber.d("Random questions list -> $qList")

            if (qList.httpStatusCode == 404)
                throw Exception(qList.httpStatusMessage.ifBlank { "Failed to parse the random question lists." })
            if (qList.questionLinks.isEmpty() || qList.questions.isEmpty())
                throw Exception("No questions were found.")
            if (qList.questions.size != qList.questionLinks.size)
                throw Exception("Failed to get answers for some questions")

            val questionAnswer = mutableListOf<Question>()
            qList.questions.forEachIndexed { index, question ->
                val answerLink = qList.questionLinks[index]
                questionAnswer.add(Question(index, question, answerLink))
            }

            questionAnswer.ifEmpty { throw Exception("Failed to parse links of the questions") }
        }
    }


    /**
     * Gets the list of questions based on their Fiqh from IslamQA.org
     *
     * @param fiqh Fiqh - under which school of thought the questions are being answered
     * @param pageNumber The page number of the questions list.
     * @return A list of Question objects.
     */
    suspend fun getFiqhBasedQuestionsList(fiqh: Fiqh, pageNumber: Int): List<Question> {
        return withContext(Dispatchers.IO) {
            val qList = scrapingService.parseFiqhBasedQuestionsList(fiqh, pageNumber)

            Timber.d("Fiqh-based questions list -> $qList")


            if (qList.httpStatusCode != 200)
                throw Exception(qList.httpStatusMessage.ifBlank { "Failed to parse the questions." })
            if (qList.questionLinks.isEmpty() || qList.questions.isEmpty())
                throw Exception("No questions were found.")
            if (qList.questions.size != qList.questionLinks.size)
                throw Exception("Failed to get answers for some questions")

            val questionAnswer = mutableListOf<Question>()
            qList.questions.forEachIndexed { index, question ->
                val answerLink = qList.questionLinks[index]
                questionAnswer.add(Question(index, question, answerLink, fiqh.displayName))
            }
            questionAnswer.ifEmpty { throw Exception("Failed to parse links of the questions") }
        }
    }

    /**
     * Takes the URL of the answer as input, and returns a `QuestionDetail` object
     *
     * @param url The url of the question detail page.
     * @return A QuestionDetail object.
     */
    suspend fun getDetailedQuestionAndAnswer(url: String): QuestionDetail {

        return withContext(Dispatchers.IO) {

            Timber.d("Loading question answer of $url")

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

            Timber.d("getDetailedQuestionAndAnswer -> $detail")

            detail
        }
    }
}