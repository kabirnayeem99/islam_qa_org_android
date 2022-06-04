package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

data class FiqhBasedQuestionListDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions
    : List<String>,
    val questionLinks: List<String>,
    val fiqh: Fiqh = Fiqh.HANAFI,
)

