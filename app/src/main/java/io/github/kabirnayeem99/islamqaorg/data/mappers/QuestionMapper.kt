package io.github.kabirnayeem99.islamqaorg.data.mappers

import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail


fun List<QuestionEntity>.toQuestions() = map { it.toQuestion() }

fun QuestionEntity.toQuestion() = Question(
    url = originalLink, question = questionTitle, fiqh = fiqh
)

fun List<QuestionDetail>.toQuestionEntities(fiqh: String): List<QuestionEntity> {
    return map { qd -> qd.copy(fiqh = fiqh).toQuestionEntity() }
}

fun QuestionDetail.toQuestionEntity() = QuestionEntity(
    originalLink = originalLink,
    questionTitle = questionTitle,
    detailedAnswer = detailedAnswer,
    detailedQuestion = detailedQuestion,
    timeInMillis = System.currentTimeMillis(),
    fiqh = fiqh,
    source = source,
)

suspend fun QuestionEntity.toQuestionDetail(getRandomQuestionList: suspend (fiqh: String) -> List<Question> = { emptyList() }): QuestionDetail {
    val dto = this
    return QuestionDetail(
        questionTitle = dto.questionTitle,
        detailedQuestion = dto.detailedQuestion,
        detailedAnswer = dto.detailedAnswer,
        fiqh = dto.fiqh,
        source = dto.source,
        originalLink = dto.originalLink,
        relevantQuestions = getRandomQuestionList(dto.fiqh),
    )
}
