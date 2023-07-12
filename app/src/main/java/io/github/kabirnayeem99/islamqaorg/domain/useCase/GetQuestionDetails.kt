package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetQuestionDetails @Inject constructor(
    private val homeScreenRepository: QuestionAnswerRepository,
) {
    suspend operator fun invoke(url: String): Flow<Resource<QuestionDetail>> {
        return homeScreenRepository
            .getQuestionDetails(url)
            .map { detail -> mapDetailToResource(detail) }
            .catch { e -> emit(Resource.Error(generateErrorMessage(e.localizedMessage ?: ""))) }
            .onStart { emit(validateAndMapToResource(url)) }
    }

    private suspend fun mapDetailToResource(questionDetail: QuestionDetail): Resource<QuestionDetail> {
        return if (questionDetail.questionTitle.isBlank()) Resource.Error(generateErrorMessage("Invalid question"))
        else Resource.Success(questionDetail)
    }

    private suspend fun validateAndMapToResource(url: String): Resource<QuestionDetail> {
        val errorMessage = generateErrorMessage("Wrong question", url)
        if (url.isBlank()) return Resource.Error(errorMessage)
        val isValidUrl = isValidUrl(url)
        if (!isValidUrl) return Resource.Error(errorMessage)
        return Resource.Loading()
    }

    private suspend fun generateErrorMessage(message: String, url: String = ""): String {
        val question = extractQuestionFromUrl(url)
        return "Failed to get answer of $question. $message"
    }

    private suspend fun extractQuestionFromUrl(url: String): String {
        return withContext(Dispatchers.Default) {
            try {
                val regex = Regex("""(?<=/)[^/]*(?=/\z)""")
                val matchResult = regex.find(url)
                val path = matchResult?.value ?: ""
                path.replace("-", " ").take(15)
            } catch (e: Exception) {
                Timber.e(e, "extractQuestionFromUrl: " + e.localizedMessage)
                "this question."
            }
        }
    }

    private suspend fun isValidUrl(url: String): Boolean {
        return withContext(Dispatchers.Default) {
            val regex = Regex("^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?\$")
            regex.matches(url)
        }
    }

}