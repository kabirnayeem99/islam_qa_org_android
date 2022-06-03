package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFiqhBasedQuestions
@Inject
constructor(private val repository: QuestionAnswerRepository) {
    suspend fun getFiqhBasedQuestionList(
        pageNumber: Int,
        shouldRefresh: Boolean,
    ): Flow<Resource<List<Question>>> {
        return repository.getFiqhBasedQuestionList(pageNumber, shouldRefresh)
    }
}