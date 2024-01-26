package io.github.kabirnayeem99.islamqaorg.common.di

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import io.github.kabirnayeem99.islamqaorg.domain.useCase.*

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

    @Provides
    fun provideGetFiqhBasedQuestions(repository: QuestionAnswerRepository): GetFiqhBasedQuestions {
        return GetFiqhBasedQuestions(repository)
    }

    @Provides
    fun provideSavePreferredFiqh(repository: SettingsRepository): SavePreferredFiqh {
        return SavePreferredFiqh(repository)
    }

    @Provides
    fun provideGetPreferredFiqh(repository: SettingsRepository): GetPreferredFiqh {
        return GetPreferredFiqh(repository)
    }

    @Provides
    fun provideSearchQuestion(repository: QuestionAnswerRepository): SearchQuestion {
        return SearchQuestion(repository)
    }

    @Provides
    fun provideFetchAndSavePeriodically(
        workManager: WorkManager,
        questionAnswerRepository: QuestionAnswerRepository,
    ) = FetchAndSavePeriodically(workManager, questionAnswerRepository)
}
