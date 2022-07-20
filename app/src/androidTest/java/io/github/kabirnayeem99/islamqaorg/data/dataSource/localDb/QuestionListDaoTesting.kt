@file:Suppress("IllegalIdentifier")

package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.github.kabirnayeem99.islamqaorg.common.utility.createMockQuestionEntityList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class QuestionListDaoTesting {
    private lateinit var db: IslamQaDatabase
    private lateinit var dao: QuestionListDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, IslamQaDatabase::class.java).build()
        dao = db.getQuestionListDao()
    }

    @After
    fun closeDb() {
        db.close()
    }


    @Test
    fun are_questions_are_added_correctly_in_the_db() {
        val questions = createMockQuestionEntityList(3)
        runBlocking {
            dao.insertAllQuestions(questions)
            val allQuestions = dao.getAllQuestions()
            assertThat(allQuestions[0].id == 0)
        }
    }

}