package io.github.kabirnayeem99.islamqaorg.common.utility

import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionDetailEntity
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question


/**
 * Creates a list of `Question` objects with the given amount of items
 *
 * @param amount Int - the number of questions to create
 * @return A list of Question objects
 */
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

/**
 * Creates a list of `QuestionEntity` objects with the given amount of items
 *
 * @param amount Int - the number of mock QuestionEntity objects to create
 * @return A list of QuestionEntity objects.
 */
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

/**
 * Creates a list of `QuestionEntity` objects with the given amount of items, and the given `Fiqh`
 * value
 *
 * @param amount The number of mock QuestionEntity objects to create.
 * @param fiqh Fiqh - this is the enum that we created earlier.
 * @return A list of QuestionEntity objects
 */
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

/**
 * Creates a list of `QuestionEntity` objects with the given amount and query
 *
 * @param amount Int - The amount of mock entities to create.
 * @param query The query string that will be used to search for questions.
 * @return List<QuestionEntity>
 */
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

/**
 * Creates a mock `QuestionDetailEntity` object with the given `id` and `randomText` and `fiqh`
 * values
 *
 * @param id The id of the question.
 * @param randomText A random string that will be used to fill in the question and answer text.
 * @param fiqh This is the fiqh that the question belongs to.
 * @return QuestionDetailEntity
 */
fun createMockQuestionDetailEntityWithId(
    id: String,
    randomText: String,
    fiqh: Fiqh
): QuestionDetailEntity {
    return QuestionDetailEntity(
        questionTitle = randomText,
        detailedQuestion = "$randomText $randomText",
        detailedAnswer = "$randomText $randomText",
        fiqh = fiqh.displayName,
        source = randomText,
        originalLink = id,
        nextQuestionLink = "$id+1",
        previousQuestionLink = "$id-1"
    )
}


/**
 * Creates a mock `QuestionDetailEntity` object with a given link
 *
 * @param link The link to the question detail page.
 * @return A QuestionDetailEntity
 */
fun createMockQuestionDetailEntityWithId(
    link: String
): QuestionDetailEntity {
    val randomText = "lorem_ispum"
    return QuestionDetailEntity(
        questionTitle = randomText,
        detailedQuestion = "$randomText $randomText",
        detailedAnswer = "$randomText $randomText",
        fiqh = Fiqh.HANAFI.displayName,
        source = randomText,
        originalLink = link,
        nextQuestionLink = "$link+1",
        previousQuestionLink = "$link-1"
    )
}
