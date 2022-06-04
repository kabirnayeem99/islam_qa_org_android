package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetQuestionDetails @Inject constructor(
    private val homeScreenRepository: QuestionAnswerRepository,
) {

    suspend operator fun invoke(url: String): Flow<Resource<QuestionDetail>> {
        return homeScreenRepository.getQuestionDetails(url)
            .onStart {
                if (url.isBlank()) emit(Resource.Error("Could not get detailed answer."))
            }
    }
}