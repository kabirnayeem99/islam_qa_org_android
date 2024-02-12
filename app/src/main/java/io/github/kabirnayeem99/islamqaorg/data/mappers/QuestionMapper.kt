package io.github.kabirnayeem99.islamqaorg.data.mappers

import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import java.util.Locale


fun List<QuestionEntity>.toQuestions() = map { it.toQuestion() }

fun QuestionEntity.toQuestion(): Question {
    val fiqh = Fiqh.entries.first { f -> f.paramName == fiqh }
    return Question(
        url = originalLink,
        question = questionTitle,
        fiqh = fiqh.displayName,
    )
}

fun QuestionDetail.toQuestionEntity() = QuestionEntity(
    originalLink = originalLink,
    questionTitle = questionTitle,
    detailedAnswer = detailedAnswer,
    detailedQuestion = detailedQuestion,
    timeInMillis = System.currentTimeMillis(),
    fiqh = fiqh.replace("'", "").lowercase(Locale.ROOT),
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
