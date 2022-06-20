package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import androidx.annotation.Keep
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

/**
 * @property {Int} httpStatusCode - The HTTP status code of the response.
 * @property {String} httpStatusMessage - This is the message that is returned by the server.
 * @property {String} questionTitle - The title of the question
 * @property {String} detailedQuestion - The question that the user clicked on.
 * @property {String} detailedAnswer - The answer to the question
 * @property {String} fiqh - The fiqh of the question.
 * @property {String} source - The source of the question.
 * @property {String} originalLink - The link to the original question on the website.
 * @property {String} nextQuestionLink - This is the link to the next question.
 * @property {String} previousQuestionLink - This is the link to the previous question.
 * @property {List<Question>} relevantQuestions - List<Question> = emptyList()
 */
@Keep
data class QuestionDetailScreenDto(
    val httpStatusCode: Int = 200,
    val httpStatusMessage: String = "",
    val questionTitle: String = "",
    val detailedQuestion: String = "",
    val detailedAnswer: String = "",
    val fiqh: String = "",
    val source: String = "",
    val originalLink: String = "",
    val nextQuestionLink: String = "",
    val previousQuestionLink: String = "",
    val relevantQuestions: List<Question> = emptyList()
)
