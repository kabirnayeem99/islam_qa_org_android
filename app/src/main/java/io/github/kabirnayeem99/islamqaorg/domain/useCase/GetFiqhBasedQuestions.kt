package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetFiqhBasedQuestions
@Inject constructor(private val repository: QuestionAnswerRepository) {

    suspend operator fun invoke(
        pageNumber: Int,
    ): Flow<Resource<List<Question>>> {
        return repository.getFiqhBasedQuestionList(pageNumber)
            .map { questions -> mapQuestionsToResources(questions) }
            .catch { e -> emit(Resource.Error(e.localizedMessage ?: "Error.")) }
            .onStart { emit(Resource.Loading()) }
    }

    private fun mapQuestionsToResources(questions: List<Question>) =
        if (questions.isEmpty()) Resource.Error("No questions!!!")
        else Resource.Success(questions)

}