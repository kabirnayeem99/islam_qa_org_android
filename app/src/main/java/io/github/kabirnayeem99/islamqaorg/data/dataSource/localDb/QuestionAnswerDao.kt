package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface QuestionAnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionEntity: QuestionEntity)


    @Query("SELECT * FROM questionentity WHERE originalLink=:link")
    suspend fun getQuestionByLink(link: String): QuestionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuestions(list: List<QuestionEntity>)

    @Query("SELECT * FROM questionentity WHERE fiqh LIKE '%' || :fiqh || '%' ORDER BY timeInMillis desc LIMIT :limit OFFSET :offset")
    fun getFiqhBasedQuestions(fiqh: String, limit: Int, offset: Int): Flow<List<QuestionEntity>>


    @Query("SELECT * FROM questionentity ORDER BY RANDOM() desc LIMIT 10")
    fun getRandomQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questionentity WHERE fiqh LIKE '%' || :fiqh || '%' ORDER BY RANDOM() desc LIMIT 10")
suspend    fun getRandomQuestionListByFiqhAsync(fiqh: String): List<QuestionEntity>

    @RawQuery(observedEntities = [QuestionEntity::class])
    suspend fun searchQuestions(query: SimpleSQLiteQuery): List<QuestionEntity>

}