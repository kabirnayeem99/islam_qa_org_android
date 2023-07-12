package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetRandomQuestion
@Inject constructor(private val repository: QuestionAnswerRepository) {
    suspend operator fun invoke(shouldRefresh: Boolean): Flow<Resource<List<Question>>> {
        return repository.getRandomQuestionList(shouldRefresh).map { validateAndMapToResource(it) }
            .catch { e -> emit(Resource.Error(generateErrorMessage(e.localizedMessage ?: ""))) }
            .onStart { emit(Resource.Loading()) }
    }

    private fun validateAndMapToResource(questionList: List<Question>): Resource<List<Question>> {
        return if (questionList.isEmpty()) Resource.Error(generateErrorMessage("Not found."))
        else Resource.Success(questionList)
    }

    private fun generateErrorMessage(message: String) =
        "Failed at getting random questions. $message"
}