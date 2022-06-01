package io.github.kabirnayeem99.islamqaorg.domain.entity

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