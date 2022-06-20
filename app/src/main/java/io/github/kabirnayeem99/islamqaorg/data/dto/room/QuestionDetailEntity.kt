package io.github.kabirnayeem99.islamqaorg.data.dto.room

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for Room database
 *
 * @property questionTitle String
 * @property detailedQuestion String
 * @property detailedAnswer String
 * @property fiqh String
 * @property source String
 * @property originalLink String
 * @property nextQuestionLink String
 * @property previousQuestionLink String
 * @constructor
 */
@Keep
@Entity
data class QuestionDetailEntity(
    val questionTitle: String = "",
    val detailedQuestion: String = "",
    val detailedAnswer: String = "",
    val fiqh: String = "",
    val source: String = "",
    @PrimaryKey
    val originalLink: String = "",
    val nextQuestionLink: String = "",
    val previousQuestionLink: String = "",
)