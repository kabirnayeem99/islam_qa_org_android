package io.github.kabirnayeem99.islamqaorg.domain.entity

import androidx.annotation.Keep

/**
 * `QuestionDetail` is a data class that contains a bunch of strings.
 *
 * Now, let's look at the `Question` type.
 * @property {String} questionTitle - The main title of the question.
 * @property {String} detailedQuestion - The question that the user asked in details.
 * @property {String} detailedAnswer - The detailed answer to the question.
 * @property {String} fiqh - The fiqh on which the answer is based on, such as Hanafi, Hanbali etc.
 * @property {String} source - The source of the answer, such as AskImam or DarulIfta etc.
 * @property {String} originalLink - The link to the original question on the website (islamqa.org)
 * @property {String} nextQuestionLink - This is the link to the next question in the list.
 * @property {String} previousQuestionLink - This is the link to the previous question in the list.
 * @property {List<Question>} relevantQuestions, questions and answers that are also relevant to it.
 */
@Keep
data class QuestionDetail(
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