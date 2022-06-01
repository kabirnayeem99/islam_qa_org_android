package io.github.kabirnayeem99.islamqaorg.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.service.ScrapingService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideScrapingService(): ScrapingService = ScrapingService()

    @Provides
    @Singleton
    fun provideIslamQaRemoteDataSource(scrapingService: ScrapingService): IslamQaRemoteDataSource {
        return IslamQaRemoteDataSource(scrapingService)
    }

}