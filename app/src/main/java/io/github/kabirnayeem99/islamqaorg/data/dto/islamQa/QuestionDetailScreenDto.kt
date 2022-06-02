package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

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
