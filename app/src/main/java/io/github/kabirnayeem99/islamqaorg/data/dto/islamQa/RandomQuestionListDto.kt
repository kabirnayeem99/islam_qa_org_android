package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import androidx.annotation.Keep

@Keep
data class RandomQuestionListDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions: List<String>,
    val questionLinks: List<String>
)

