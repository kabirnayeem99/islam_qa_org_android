package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetQuestionDetails
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetRandomQuestion

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetHomeScreenData(repository: QuestionAnswerRepository): GetRandomQuestion {
        return GetRandomQuestion(repository)
    }

    @Provides
    fun provideGetQuestionDetails(repository: QuestionAnswerRepository): GetQuestionDetails {
        return GetQuestionDetails(repository)
    }
}