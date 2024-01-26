package io.github.kabirnayeem99.islamqaorg.data.dataSource.localDb

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.github.kabirnayeem99.islamqaorg.common.utility.createMockQuestionDetailEntityWithId
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class QuestionAnswerDaoTesting {

    private lateinit var db: IslamQaDatabase
    private lateinit var dao: QuestionAnswerDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, IslamQaDatabase::class.java).build()
        dao = db.getQuestionAnswerDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertQuestion_given_a_question_check_if_it_is_inserted_correctly() {
        val id = Date().time.toString()
        val text = "lorem_ispum_lorem_${Random().nextInt(100)}"

        val fiqh = Fiqh.MALIKI
        val questionDetail = createMockQuestionDetailEntityWithId(
            id = id,
            randomText = text,
            fiqh = fiqh,
        )
        runBlocking {
            dao.insertQuestion(questionDetail)
            val question = dao.getQuestionByLink(id)
            assert(question != null && question.questionTitle == text && question.fiqh == fiqh.displayName)
        }
    }

    @Test
    fun getQuestionByLink_given_a_question_check_if_it_can_be_fetched_correctly() {
        val link = "https://example.com/${Date().time}"
        val questionDetail = createMockQuestionDetailEntityWithId(link = link)
        runBlocking {
            dao.insertQuestion(questionDetail)
            val question = dao.getQuestionByLink(link)
            assert(question != null && question.originalLink == link)
        }
    }

}