package io.github.kabirnayeem99.islamqaorg.data.dto.room

import androidx.room.Entity
import androidx.room.PrimaryKey

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