package io.github.kabirnayeem99.islamqaorg.data.mappers

import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionDetailEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail

fun QuestionDetail.toQuestionDetailEntity(): QuestionDetailEntity {
    return QuestionDetailEntity(
        questionTitle,
        detailedQuestion,
        detailedAnswer,
        fiqh,
        source,
        originalLink,
        nextQuestionLink,
        previousQuestionLink,
    )
}

suspend fun QuestionDetailEntity.toQuestionDetail(getRandomQuestionList: suspend () -> List<Question> = { emptyList() }): QuestionDetail {
    val dto = this
    return QuestionDetail(
        questionTitle = dto.questionTitle,
        detailedQuestion = dto.detailedQuestion,
        detailedAnswer = dto.detailedAnswer,
        fiqh = dto.fiqh,
        source = dto.source,
        originalLink = dto.originalLink,
        nextQuestionLink = dto.nextQuestionLink,
        previousQuestionLink = dto.previousQuestionLink,
        relevantQuestions = getRandomQuestionList(),
    )
}