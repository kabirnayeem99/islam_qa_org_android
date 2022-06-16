package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh

@Dao
interface QuestionListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuestions(list: List<QuestionEntity>)

    @Query("SELECT * FROM questionentity ORDER BY timeInMillis desc LIMIT 10")
    suspend fun getAllQuestions(): List<QuestionEntity>

    @Query("SELECT * FROM questionentity WHERE fiqh LIKE '%' || :fiqh || '%' ORDER BY timeInMillis desc LIMIT 10")
    suspend fun getFiqhBasedQuestions(fiqh: String): List<QuestionEntity>

    @Query("SELECT * FROM questionentity ORDER BY timeInMillis desc LIMIT 10")
    suspend fun getRandomQuestions(): List<QuestionEntity>
}