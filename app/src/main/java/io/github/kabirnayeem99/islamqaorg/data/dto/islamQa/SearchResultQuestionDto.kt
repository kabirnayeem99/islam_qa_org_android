package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import androidx.annotation.Keep
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

@Keep
data class SearchResultQuestionDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions: List<String>,
    val questionLinks: List<String>,
    val fiqh: Fiqh = Fiqh.HANAFI,
)