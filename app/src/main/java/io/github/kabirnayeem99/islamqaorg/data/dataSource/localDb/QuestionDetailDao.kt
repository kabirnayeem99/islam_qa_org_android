package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionDetailEntity

@Dao
interface QuestionDetailDao {

    /**
     * Inserts a question into the database
     *
     * @param questionDetailEntity The object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionDetailEntity: QuestionDetailEntity)

    /**
     * Returns a QuestionDetailEntity object from the database, which has the given link
     *
     * @param link String
     */
    @Query("SELECT * FROM questiondetailentity WHERE originalLink=:link")
    suspend fun getQuestionByLink(link: String): QuestionDetailEntity?

}