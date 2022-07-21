@file:Suppress("IllegalIdentifier")

package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.github.kabirnayeem99.islamqaorg.common.utility.createMockQuestionEntityList
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

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
    fun insertAllQuestions_given_a_question_list_checks_if_inserting_questions_work_() {
        val questions = createMockQuestionEntityList(3)
        runBlocking {
            dao.insertAllQuestions(questions)
            val allQuestions = dao.getAllQuestions()
            assertThat(allQuestions[0].id == 0)
        }
    }

    @Test
    fun getAllQuestions_given_a_question_list_checks_if_getting_question_list_working() {
        val length = 3
        val questions = createMockQuestionEntityList(length)
        runBlocking {
            dao.insertAllQuestions(questions)
            val allQuestions = dao.getAllQuestions()
            assertThat(allQuestions.size == length)
        }
    }

    @Test
    fun getFiqhBasedQuestions_given_a_question_list_with_selected_fiqh_checks_if_questions_have_same_length_as_added_questions() {
        val length = 3
        val selectedFiqh = Fiqh.SHAFII
        val questions = createMockQuestionEntityList(length, selectedFiqh)
        runBlocking {
            dao.insertAllQuestions(questions)
            val allQuestions = dao.getAllQuestions().filter { it.fiqh == selectedFiqh }
            assertThat(allQuestions.size == length)
        }
    }

    @Test
    fun searchQuestions_given_a_question_list_with_predefined_query_checks_if_search_results_have_the_same_length_and_includes_query() {
        val length = 3
        val query = "test_${Date()}"
        val questions = createMockQuestionEntityList(length, query)
        runBlocking {
            dao.insertAllQuestions(questions)
            val allQuestions = dao.searchQuestions(query).filter { it.question.contains(query) }
            assertThat(allQuestions.size == length)
        }
    }

}