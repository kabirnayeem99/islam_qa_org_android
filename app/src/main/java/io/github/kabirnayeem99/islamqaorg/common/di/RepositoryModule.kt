package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.data.repository.QuestionAnswerRepositoryImpl
import io.github.kabirnayeem99.islamqaorg.data.repository.SettingsRepositoryImpl
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import io.github.kabirnayeem99.islamqaorg.domain.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideQuestionAnswerRepository(
        remoteDataSource: IslamQaRemoteDataSource,
        localDataSource: IslamQaLocalDataSource,
        preferenceDataSource: PreferenceDataSource,
    ): QuestionAnswerRepository {
        return QuestionAnswerRepositoryImpl(
            remoteDataSource,
            localDataSource,
            preferenceDataSource
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        preferenceDataSource: PreferenceDataSource
    ): SettingsRepository {
        return SettingsRepositoryImpl(preferenceDataSource)
    }

}