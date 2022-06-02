package io.github.kabirnayeem99.islamqaorg.data.dto.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionEntity(
    val id: Int = 0,
    val question: String = "",
    @PrimaryKey
    val url: String = "",
)