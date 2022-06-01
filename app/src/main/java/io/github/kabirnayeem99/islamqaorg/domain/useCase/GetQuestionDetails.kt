package io.github.kabirnayeem99.islamqaorg.domain.useCase

import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail
import io.github.kabirnayeem99.islamqaorg.domain.repository.HomeScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetQuestionDetails @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository,
) {

    suspend fun getQuestionDetails(url: String): Flow<Resource<QuestionDetail>> {
        return homeScreenRepository.getQuestionDetails(url)
            .onStart {
                if (url.isBlank()) emit(Resource.Error("Could not get detailed answer."))
            }
    }
}