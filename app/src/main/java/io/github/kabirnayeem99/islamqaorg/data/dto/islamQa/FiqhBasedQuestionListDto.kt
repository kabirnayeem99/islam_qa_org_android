package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import androidx.annotation.Keep
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

/**
 * @property {Int} httpStatusCode - The HTTP status code of the response.
 * @property {String} httpStatusMessage - This is the message that the server sends back to the client.
 * @property {List<String>} questions - List<String>
 * @property {List<String>} questionLinks - List<String>
 * @property {Fiqh} fiqh - The fiqh of the questions.
 */
@Keep
data class FiqhBasedQuestionListDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions
    : List<String>,
    val questionLinks: List<String>,
    val fiqh: Fiqh = Fiqh.HANAFI,
)

