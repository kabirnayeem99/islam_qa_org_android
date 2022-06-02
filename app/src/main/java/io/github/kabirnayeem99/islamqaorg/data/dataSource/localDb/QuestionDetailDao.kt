package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionDetailEntity
import io.github.kabirnayeem99.islamqaorg.domain.entity.QuestionDetail

@Dao
interface QuestionDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questionDetailEntity: QuestionDetailEntity)

    @Query("SELECT * FROM questiondetailentity WHERE originalLink=:link")
    suspend fun getQuestionByLink(link: String): QuestionDetailEntity

}