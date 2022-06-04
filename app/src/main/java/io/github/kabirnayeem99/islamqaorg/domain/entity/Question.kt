package io.github.kabirnayeem99.islamqaorg.domain.entity

/**
 * `Question` is a data class with three properties: `id`, `question`, and `url`.
 *
 * The `question` property is a string with a default value of an empty string.
 * @property {Int} id - The unique identifier for the question.
 * @property {String} question - The question text.
 * @property {String} url - The URL of the answer that is associated with the question.
 */
data class Question(
    val id: Int = 0,
    val question: String = "",
    val url: String = "",
    val fiqh: String = "",
)
