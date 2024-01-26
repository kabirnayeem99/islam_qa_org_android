package io.github.kabirnayeem99.islamqaorg.domain.entity

import androidx.annotation.Keep

/**
 * `Question` is a data class with three properties: `id`, `question`, and `url`.
 *
 * The `question` property is a string with a default value of an empty string.
 * @property {Int} id - The unique identifier for the question.
 * @property {String} question - The question text.
 * @property {String} url - The URL of the answer that is associated with the question.
 */
@Keep
data class Question(
    val question: String = "",
    val url: String = "",
    val fiqh: String = "",
)
