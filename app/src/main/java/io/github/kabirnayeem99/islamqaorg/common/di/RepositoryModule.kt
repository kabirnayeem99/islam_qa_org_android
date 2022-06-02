package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.common.utility.NetworkUtil
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.data.repository.QuestionAnswerRepositoryImpl
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideHomeScreenRepository(
        remoteDataSource: IslamQaRemoteDataSource,
        localDataSource: IslamQaLocalDataSource,
        preferenceDataSource: PreferenceDataSource,
        networkUtil: NetworkUtil,
    ): QuestionAnswerRepository {
        return QuestionAnswerRepositoryImpl(remoteDataSource, localDataSource, preferenceDataSource,networkUtil)
    }
}