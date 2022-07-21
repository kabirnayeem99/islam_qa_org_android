package io.github.kabirnayeem99.islamqaorg.common.utility

import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question


fun createMockQuestionList(amount: Int): List<Question> {
    return List(amount) { id ->
        Question(
            id = id,
            question = "test question $id",
            url = "https://islamqa.org",
            fiqh = Fiqh.HANAFI.displayName,
        )
    }
}

fun createMockQuestionEntityList(amount: Int): List<QuestionEntity> {
    return List(amount) { id ->
        QuestionEntity(
            id = id,
            question = "test question $id",
            url = "https://islamqa.org",
            fiqh = Fiqh.HANAFI,
        )
    }
}

fun createMockQuestionEntityList(amount: Int, fiqh: Fiqh): List<QuestionEntity> {
    return List(amount) { id ->
        QuestionEntity(
            id = id,
            question = "test question $id",
            url = "https://islamqa.org",
            fiqh = fiqh,
        )
    }
}

fun createMockQuestionEntityList(amount: Int, query: String): List<QuestionEntity> {
    return List(amount) { id ->
        QuestionEntity(
            id = id,
            question = "$query test $id",
            url = "https://islamqa.org",
            fiqh = Fiqh.HANAFI,
        )
    }
}
