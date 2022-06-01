package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.repository.HomeScreenRepositoryImpl
import io.github.kabirnayeem99.islamqaorg.domain.repository.HomeScreenRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideHomeScreenRepository(remoteDataSource: IslamQaRemoteDataSource): HomeScreenRepository {
        return HomeScreenRepositoryImpl(remoteDataSource)
    }
}