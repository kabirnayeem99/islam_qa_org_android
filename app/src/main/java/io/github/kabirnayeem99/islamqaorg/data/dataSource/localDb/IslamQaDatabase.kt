package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity

@Database(
    entities = [QuestionEntity::class],
    version = BuildConfig.VERSION_CODE + 1,
    exportSchema = false
)
abstract class IslamQaDatabase : RoomDatabase() {
    abstract fun getQuestionListDao(): QuestionListDao
}