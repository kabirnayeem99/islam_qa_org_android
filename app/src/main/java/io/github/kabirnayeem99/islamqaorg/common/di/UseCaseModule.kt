package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.islamqaorg.domain.repository.HomeScreenRepository
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetHomeScreenData
import io.github.kabirnayeem99.islamqaorg.domain.useCase.GetQuestionDetails

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetHomeScreenData(repository: HomeScreenRepository): GetHomeScreenData {
        return GetHomeScreenData(repository)
    }

    @Provides
    fun provideGetQuestionDetails(repository: HomeScreenRepository): GetQuestionDetails {
        return GetQuestionDetails(repository)
    }
}