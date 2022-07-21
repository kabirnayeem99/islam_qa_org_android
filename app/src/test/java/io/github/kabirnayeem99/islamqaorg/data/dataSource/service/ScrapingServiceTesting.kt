package io.github.kabirnayeem99.islamqaorg.data.dataSource.service

import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import it.skrape.fetcher.Result
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class ScrapingServiceTesting {
    lateinit var scrapingService: ScrapingService

    @Before
    fun initialise() {
        scrapingService = ScrapingService()
    }

    @Throws(IOException::class)
    fun readFileWithoutNewLineFromResources(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream =
                javaClass.classLoader?.getResourceAsStream(fileName)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var str: String? = reader.readLine()
            while (str != null) {
                builder.append(str)
                str = reader.readLine()
            }
            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }

    @Test
    fun getRandomQuestionListDtoOutOfResponse_test_given_a_response_body_if_it_can_parse_all() {
        val responseBody = readFileWithoutNewLineFromResources("islam_qa_org_home_page_source.html")
        val responseCode = 200

        val result = Result(
            responseBody = responseBody,
            responseStatus = Result.Status(responseCode, "OK"),
            contentType = "",
            headers = mapOf(),
            cookies = emptyList(),
            baseUri = ""
        )

        val dto = scrapingService.getRandomQuestionListDtoOutOfResponse(result)
        assert(dto.httpStatusCode == responseCode && dto.questionLinks.size == dto.questions.size && dto.questionLinks.size > 10)

    }

    @Test
    fun getFiqhBasedQuestionListDtoOutOfResponse_test_given_a_response_body_if_it_can_parse_all() {
        val responseBody =
            readFileWithoutNewLineFromResources("islam_qa_org_maliki_fiqh_based_page_source.html")
        val responseCode = 200
        val fiqh = Fiqh.MALIKI

        val result = Result(
            responseBody = responseBody,
            responseStatus = Result.Status(responseCode, "OK"),
            contentType = "",
            headers = mapOf(),
            cookies = emptyList(),
            baseUri = ""
        )

        val dto = scrapingService.getFiqhBasedQuestionListDtoOutOfResponse(result, fiqh)
        assert(dto.httpStatusCode == responseCode && dto.fiqh == fiqh && dto.questions.size == dto.questionLinks.size)
    }

    @Test
    fun parseQuestionDetailScreen_test_given_a_link_if_it_can_parse_the_question() {
        runBlocking {
            val detailLink = "https://islamqa.org/hanafi/muftionline/95119/ulema-of-deoband/"
            val dto = scrapingService.parseQuestionDetailScreen(link = detailLink)
            Timber.d(dto.toString())
            assert(dto.questionTitle.isNotBlank())
            assert(dto.relevantQuestions.isNotEmpty())
        }
    }

}