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
        shouldRefresh: Boolean,
    ): Flow<Resource<List<Question>>> {
        return repository.getFiqhBasedQuestionList(pageNumber, shouldRefresh)
            .map { questions -> mapQuestionsToResources(questions) }
            .catch { emit(Resource.Error(it.localizedMessage ?: "Error!!!")) }
            .onStart { if (pageNumber < 1) emit(Resource.Error("Wrong page!")) else emit(Resource.Loading()) }
    }

    private fun mapQuestionsToResources(questions: List<Question>) =
        if (questions.isEmpty()) Resource.Error("No questions!!!")
        else Resource.Success(questions)

}