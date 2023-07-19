package io.github.kabirnayeem99.islamqaorg.data.dataSource

import io.github.kabirnayeem99.islamqaorg.data.dataSource.service.ScrapingService
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import timber.log.Timber
import javax.inject.Inject

class IslamQaRemoteDataSource @Inject constructor(private val scrapingService: ScrapingService) {

    /**
     * Gets the list of random questions from the IslamQA (Under "Random List of Q&A" heading)
     *
     * @return A list of Question objects.
     */
    suspend fun getRandomQuestionsList(): List<Question> {
        val qList = scrapingService.parseRandomQuestionList()

        if (qList.httpStatusCode == 404) throw Exception(qList.httpStatusMessage.ifBlank { "Failed to parse the random question lists." })
        if (qList.questionLinks.isEmpty() || qList.questions.isEmpty()) throw Exception("No questions were found.")
        if (qList.questions.size != qList.questionLinks.size) throw Exception("Failed to get answers for some questions")

        val questionAnswer = mutableListOf<Question>()
        qList.questions.forEachIndexed { index, question ->
            val answerLink = qList.questionLinks[index]
            questionAnswer.add(Question(index, question, answerLink))
        }

        return questionAnswer.ifEmpty { throw Exception("Failed to parse links of the questions") }
    }


    /**
     * Gets the list of questions based on their Fiqh from IslamQA.org
     *
     * @param fiqh Fiqh - under which school of thought the questions are being answered
     * @param pageNumber The page number of the questions list.
     * @return A list of Question objects.
     */
    suspend fun getFiqhBasedQuestionsList(fiqh: Fiqh, pageNumber: Int): List<Question> {

        val qList = scrapingService.parseFiqhBasedQuestionsList(fiqh, pageNumber)

        if (qList.httpStatusCode != 200) throw Exception(qList.httpStatusMessage.ifBlank { "Failed to parse the questions." })
        if (qList.questionLinks.isEmpty() || qList.questions.isEmpty()) throw Exception("No questions were found.")
        if (qList.questions.size != qList.questionLinks.size) throw Exception("Failed to get answers for some questions")

        val questionAnswer = mutableListOf<Question>()
        qList.questions.forEachIndexed { index, question ->
            val answerLink = qList.questionLinks[index]
            questionAnswer.add(Question(index, question, answerLink, fiqh.displayName))
        }
        return questionAnswer.ifEmpty { throw Exception("Failed to parse links of the questions") }

    }

    /**
     * Takes the URL of the answer as input, and returns a `QuestionDetail` object
     *
     * @param url The url of the question detail page.
     * @return A QuestionDetail object.
     */
    suspend fun getDetailedQuestionAndAnswer(url: String): QuestionDetail {

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

        return detail
    }

    /**
     * Takes a query string, searches for it on IslamQa.Org, parses the search results, and
     * returns a list of questions and their answer links
     *
     * @param query The search query.
     * @return A list of Question objects.
     */
    suspend fun searchRandomQuestionsList(query: String): List<Question> {
        val searchResult = scrapingService.parseSearchResults(query)


        if (searchResult.httpStatusCode == 404) throw Exception(searchResult.httpStatusMessage.ifBlank { "Failed to parse the search result question lists." })
        if (searchResult.questionLinks.isEmpty() || searchResult.questions.isEmpty()) throw Exception(
            "No questions were found."
        )
        if (searchResult.questions.size != searchResult.questionLinks.size) throw Exception("Failed to get answer links for some questions")

        val questionAnswer = mutableListOf<Question>()
        searchResult.questions.forEachIndexed { index, question ->
            val answerLink = searchResult.questionLinks[index]
            questionAnswer.add(Question(index, question, answerLink))
        }

        return questionAnswer.ifEmpty { throw Exception("Failed to find any answers.") }
    }
}