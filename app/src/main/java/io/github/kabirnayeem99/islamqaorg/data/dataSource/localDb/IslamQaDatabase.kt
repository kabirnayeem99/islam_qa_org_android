package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.data.dto.room.Converter
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionDetailEntity
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity

@Database(
    entities = [QuestionEntity::class, QuestionDetailEntity::class],
    version = BuildConfig.VERSION_CODE + 5,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class IslamQaDatabase : RoomDatabase() {
    abstract fun getQuestionListDao(): QuestionListDao
    abstract fun getQuestionDetailDao(): QuestionDetailDao
}