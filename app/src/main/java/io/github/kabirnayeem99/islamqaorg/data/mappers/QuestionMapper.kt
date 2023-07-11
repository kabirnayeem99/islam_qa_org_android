package io.github.kabirnayeem99.islamqaorg.data.mappers

import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question


fun List<QuestionEntity>.toQuestions(): List<Question> {
    return map { it.toQuestion() }
}

fun QuestionEntity.toQuestion(): Question {
    return Question(id, question, url)
}

fun List<Question>.toQuestionEntities(fiqh: Fiqh): List<QuestionEntity> {
    return map { it.toQuestionEntity(fiqh) }
}

fun Question.toQuestionEntity(fiqh: Fiqh): QuestionEntity {
    return QuestionEntity(id = id, question = question, url = url, fiqh = fiqh)
}
