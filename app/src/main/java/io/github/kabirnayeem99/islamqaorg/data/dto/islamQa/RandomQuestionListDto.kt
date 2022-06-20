package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import androidx.annotation.Keep

/**
 * `RandomQuestionListDto` is a data class that contains a list of questions and a list of links to
 * those questions.
 *
 * @property {Int} httpStatusCode - The HTTP status code returned by the web site.
 * @property {String} httpStatusMessage - The HTTP status message returned by the web site.
 * @property {List<String>} questions - A list of questions.
 * @property {List<String>} questionLinks - This is a list of links to the questions.
 */
@Keep
data class RandomQuestionListDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions: List<String>,
    val questionLinks: List<String>
)

