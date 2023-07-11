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
import javax.inject.Inject

class GetQuestionDetails @Inject constructor(
    private val homeScreenRepository: QuestionAnswerRepository,
) {
    suspend operator fun invoke(url: String): Flow<Resource<QuestionDetail>> {
        return homeScreenRepository.getQuestionDetails(url)
            .map { detail -> mapDetailToResource(detail) }
            .catch { emit(Resource.Error(it.localizedMessage ?: "Error!!")) }
            .onStart { emit(validateAndMapToResource(url)) }
    }

    private fun mapDetailToResource(questionDetail: QuestionDetail): Resource<QuestionDetail> {
        return if (questionDetail.questionTitle.isBlank()) Resource.Error("Invalid question")
        else Resource.Success(questionDetail)
    }

    private suspend fun validateAndMapToResource(url: String): Resource<QuestionDetail> {
        if (url.isBlank()) return Resource.Error("Could not get detailed answer.")
        val isValidUrl = isValidUrl(url)
        if (!isValidUrl) return Resource.Error("Could not get detailed answer.")
        return Resource.Loading()
    }

    private suspend fun isValidUrl(url: String): Boolean {
        return withContext(Dispatchers.Default) {
            val regex = Regex("^https?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?\$")
            regex.matches(url)
        }
    }

}