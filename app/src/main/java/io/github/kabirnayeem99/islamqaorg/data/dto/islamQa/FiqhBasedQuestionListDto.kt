package io.github.kabirnayeem99.islamqaorg.data.dto.islamQa

data class FiqhBasedQuestionListDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions: List<String>,
    val questionLinks: List<String>
)

