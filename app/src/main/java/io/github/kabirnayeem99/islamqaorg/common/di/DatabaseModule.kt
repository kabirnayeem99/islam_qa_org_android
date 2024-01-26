package io.github.kabirnayeem99.islamqaorg.common.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb.IslamQaDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideIslamQaDatabase(@ApplicationContext context: Context): IslamQaDatabase {
        return Room.databaseBuilder(
            context, IslamQaDatabase::class.java, "islam_qa_local_caching"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideQuestionDetailDao(db: IslamQaDatabase) = db.getQuestionAnswerDao()

}