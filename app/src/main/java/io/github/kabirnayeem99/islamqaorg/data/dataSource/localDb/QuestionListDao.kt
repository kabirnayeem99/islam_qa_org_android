package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import io.github.kabirnayeem99.islamqaorg.data.dto.room.QuestionEntity

/**
 * DAO class that has methods to insert a list of questions, get all questions, get questions
 * based on fiqh and get random questions
 */
@Dao
interface QuestionListDao {

    /**
     * Takes a list of QuestionEntity objects and inserts them into the database
     *
     * @param list List<QuestionEntity> - The list of questions to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuestions(list: List<QuestionEntity>)

    /**
     * Returns a list of QuestionEntity objects, which are the result of a query that
     * selects all the columns from the question entity table, ordered by the timeInMillis column in
     * descending order, and limited to the first 10 results
     */
    @Query("SELECT * FROM questionentity ORDER BY timeInMillis desc LIMIT 10")
    suspend fun getAllQuestions(): List<QuestionEntity>

    /**
     * Takes the selected Fiqh as input and returns a list of QuestionEntity objects.
     *
     * @param fiqh String
     */
    @Query("SELECT * FROM questionentity WHERE fiqh LIKE '%' || :fiqh || '%' ORDER BY timeInMillis desc LIMIT 10")
    suspend fun getFiqhBasedQuestions(fiqh: String): List<QuestionEntity>

    /**
     * Returns a list of QuestionEntity objects, ordered by timeInMillis in descending order, and
     * limited to 10 items
     */
    @Query("SELECT * FROM questionentity ORDER BY timeInMillis desc LIMIT 10")
    suspend fun getRandomQuestions(): List<QuestionEntity>

//    @Query("SELECT * FROM questionentity WHERE question LIKE '%' || :query || '%' ORDER BY timeInMillis desc LIMIT 10")
//    suspend fun searchQuestions(query: List<String>): List<QuestionEntity>

    @RawQuery(observedEntities = [QuestionEntity::class])
    suspend fun searchQuestions(query: SimpleSQLiteQuery): List<QuestionEntity>
}

fun generateSearchQuery(terms: List<String>): SimpleSQLiteQuery {
    val allFetchGeneralQuery = "SELECT * FROM questionentity"
    val filteringQueryPart = terms.joinToString(
        prefix = " WHERE ", separator = " OR "
    ) { term -> "question LIKE '%$term%'" }
    val searchQuery = allFetchGeneralQuery + filteringQueryPart
    return SimpleSQLiteQuery(searchQuery)
}