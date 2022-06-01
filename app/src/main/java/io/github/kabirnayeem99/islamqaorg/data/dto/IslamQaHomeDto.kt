package io.github.kabirnayeem99.islamqaorg.data.dto

data class IslamQaHomeDto(
    val httpStatusCode: Int,
    val httpStatusMessage: String,
    val questions: List<String>,
    val questionLinks: List<String>
)

