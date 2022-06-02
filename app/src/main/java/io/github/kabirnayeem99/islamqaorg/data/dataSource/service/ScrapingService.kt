package io.github.kabirnayeem99.islamqaorg.data.dataSource.service

import io.github.kabirnayeem99.islamqaorg.data.dto.islamQa.IslamQaHomeDto
import io.github.kabirnayeem99.islamqaorg.data.dto.islamQa.QuestionDetailScreenDto
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import it.skrape.core.document
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Result
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.eachHref
import it.skrape.selects.eachText
import it.skrape.selects.html
import it.skrape.selects.html5.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

private const val homeScreenUrl = "https://islamqa.org/"

class ScrapingService {

    /**
     * Fetches the home screen of islamqa.org, parses it, and converts it to a [IslamQaHomeDto] object.
     *
     * @return A `IslamQaHomeDto` object.
     */
    suspend fun parseHomeScreen(): IslamQaHomeDto {
        return withContext(Dispatchers.IO) {

            val islamQaHomeDto = skrape(HttpFetcher) {
                request { url = homeScreenUrl }
                response { getIslamQaHomeDtoOutOfResponse() }
            }

            Timber.d(islamQaHomeDto.toString())

            islamQaHomeDto
        }
    }

    /**
     * Parses the HTML response and returns [IslamQaHomeDto] class.
     */
    private fun Result.getIslamQaHomeDtoOutOfResponse() = IslamQaHomeDto(
        httpStatusCode = status { code },
        httpStatusMessage = status { message },
        questions = document.findAll("li")
            .filter { it.className == "arpw-li arpw-clearfix" }.eachText,
        questionLinks = document.li { a { findAll { filter { it.className == "arpw-title" }.eachHref } } }
    )

    /**
     * Fetches the details of a specific question from islamqa.org, parses it, and converts it to a
     * [QuestionDetailScreenDto] object.
     *
     * @return A `QuestionDetailScreenDto` object.
     */
    suspend fun parseQuestionDetailScreen(link: String): QuestionDetailScreenDto {
        return withContext(Dispatchers.IO) {

            val questionDetail = skrape(HttpFetcher) {
                request { url = link }
                response {
                    QuestionDetailScreenDto(
                        httpStatusCode = status { code },
                        httpStatusMessage = status { message },
                        questionTitle = document.h1 { findFirst { text } },
                        detailedQuestion = getDetailedQuestionAsHtmlText(),
                        detailedAnswer = getDetailedAnswer(),
                        fiqh = document.div { findAll { first { it.className == "et_pb_code_inner" && it.parent.id == "meta-1" }.a { findFirst { text } } } },
                        source = document.div { findAll { first { it.className == "et_pb_code_inner" && it.parent.id == "meta-1" }.eachLink.keys.last() } },
                        originalLink = link,
                        nextQuestionLink = getPrevOrNextLink(Page.NEXT),
                        previousQuestionLink = getPrevOrNextLink(Page.PREV),
                        relevantQuestions = getRelevantQuestionsFromQuestionDetails()
                    )
                }
            }
            Timber.d(questionDetail.toString())

            questionDetail
        }
    }

    /**
     * Parses the detailed answer from IslamQA.org, if it exists
     */
    private fun Result.getDetailedAnswer() = document.div {
        findAll {
            filter { it.className == "ddb-answer" }.eachText.joinToString(
                "\n"
            )
        }
    }

    /**
     * Parses the detailed question as an HTML text
     *
     * as <div> has issues in showing Android Text view, it is being removed
     *
     * @receiver Result
     * @return String
     */
    private fun Result.getDetailedQuestionAsHtmlText() = document.div {
        findAll {
            "<html>" + filter { it.id == "qna_only" }.html.removePrefix("<div id=\"qna_only\">")
                .removeSuffix("</div>") + "</html>"
        }
    }

    enum class Page {
        PREV,
        NEXT
    }

    /**
     * Parses the previous answer link and next answer link based on the parameter
     *
     * Finds the `<span>` with the class name `nav-previous` or `nav-next` and return the `href` of
     * the `<a>` tag inside it
     *
     * @param prevOrNext Page - This is an enum that has two values: PREV and NEXT.
     * @return The link to the previous or next page.
     */
    private fun Result.getPrevOrNextLink(prevOrNext: Page): String {

        val classNameForPrevOrNext = when (prevOrNext) {
            Page.PREV -> "nav-previous"
            Page.NEXT -> "nav-next"
        }

        return document.div {
            findAll {
                span {
                    findAll {
                        first { it.className == classNameForPrevOrNext }.a {
                            findAll { eachHref.firstOrNull() ?: "" }
                        }
                    }
                }
            }
        }
    }

    /**
     * Parses the relevant questions with the existing question
     *
     * Finds all the `div` elements with the class name `crp_related_widget` and then find all the
     * `li` elements within the `ul` element and then map the `li` elements to a `Question` object
     */
    private fun Result.getRelevantQuestionsFromQuestionDetails() = document.div {
        findAll {
            first { it.className == "crp_related_widget" }.ul {
                li {
                    findAll {
                        map {
                            Question(
                                id = Random.nextInt(50),
                                question = it.text,
                                url = it.eachHref.firstOrNull() ?: ""
                            )
                        }
                    }
                }
            }
        }
    }

}