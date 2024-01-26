package io.github.kabirnayeem99.islamqaorg.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaLocalDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.IslamQaRemoteDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.PreferenceDataSource
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.QuestionAnswerDao
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

    @Provides
    @Singleton
    fun provideIslamQaLocalDataSource(detailDao: QuestionAnswerDao) =
        IslamQaLocalDataSource(detailDao)

    @Provides
    @Singleton
    fun providePreferenceDataSource(@ApplicationContext context: Context): PreferenceDataSource {
        return PreferenceDataSource(context)
    }


}