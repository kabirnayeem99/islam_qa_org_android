package io.github.kabirnayeem99.islamqaorg.data.dataSource.service

import io.github.kabirnayeem99.islamqaorg.data.dto.islamQa.FiqhBasedQuestionListDto
import io.github.kabirnayeem99.islamqaorg.data.dto.islamQa.QuestionDetailScreenDto
import io.github.kabirnayeem99.islamqaorg.data.dto.islamQa.RandomQuestionListDto
import io.github.kabirnayeem99.islamqaorg.data.dto.islamQa.SearchResultQuestionDto
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import it.skrape.core.document
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.Result
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.eachHref
import it.skrape.selects.eachText
import it.skrape.selects.html
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h1
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.h4
import it.skrape.selects.html5.li
import it.skrape.selects.html5.span
import it.skrape.selects.html5.ul
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

private const val baseIslamQaUrl = "https://islamqa.org/"

class ScrapingService {

    /**
     * Fetches the home screen of islamqa.org, parses it, and converts it to a [RandomQuestionListDto] object.
     *
     * @return A `IslamQaHomeDto` object.
     */
    suspend fun parseRandomQuestionList(): RandomQuestionListDto {
        return withContext(Dispatchers.IO) {

            val islamQaHomeDto = skrape(HttpFetcher) {
                request { url = baseIslamQaUrl }
                response { getRandomQuestionListDtoOutOfResponse(this) }
            }


            islamQaHomeDto
        }
    }

    /**
     * Parses the HTML response and returns [RandomQuestionListDto] class.
     */
    fun getRandomQuestionListDtoOutOfResponse(result: Result): RandomQuestionListDto {
        result.apply {
            return RandomQuestionListDto(
                httpStatusCode = status { code },
                httpStatusMessage = status { message },
                questions = findRandomQuestionQuestionTexts(),
                questionLinks = findRandomQuestionQuestionLinks()
            )
        }
    }

    private fun Result.findRandomQuestionQuestionLinks(): List<String> {
        return try {
            val urls = document.h4 {
                findAll { map { it.eachHref.firstOrNull() ?: "" } }
            }
            urls
        } catch (e: Exception) {
            Timber.e(e, "Failed to find RandomQuestion Question Links ")
            emptyList()
        }
    }

    private fun Result.findRandomQuestionQuestionTexts(): List<String> {
        return try {
            val texts = document.h4 {
                findAll { map { it.text } }
            }
            texts
        } catch (e: Exception) {
            Timber.e(e, "Failed to find the questions list")
            emptyList()
        }
    }


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
                        fiqh = getFiqhForQuestionDetails(),
                        source = getSourceForQuestionDetails(),
                        originalLink = link,
                        nextQuestionLink = getPrevOrNextLink(Page.NEXT),
                        previousQuestionLink = getPrevOrNextLink(Page.PREV),
                        relevantQuestions = getRelevantQuestionsFromQuestionDetails()
                    )
                }
            }

            questionDetail
        }
    }

    private fun Result.getSourceForQuestionDetails(): String {
        return try {
            document.div { findAll { first { it.id == "answered-according-to" }.eachLink.keys.toList()[1] } }
        } catch (e: Exception) {
            Timber.e(e, "getSourceForQuestionDetails: " + e.localizedMessage)
            "N/A"
        }
    }

    private fun Result.getFiqhForQuestionDetails(): String {
        return try {
            document.div { findAll { first { it.id == "answered-according-to" }.a { findFirst { text } } } }
        } catch (e: Exception) {
            Timber.e(e, "getFiqhForQuestionDetails: " + e.localizedMessage)
            "N/A"
        }
    }

    /**
     * Parses the detailed answer from IslamQA.org, if it exists
     */
    private fun Result.getDetailedAnswer() = try {
        document.div {
            findAll {
                filter { it.className == "ddb-answer" }.eachText.joinToString(
                    "\n"
                )
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to get detailed answer -> ${e.localizedMessage}")
        ""
    }

    /**
     * Parses the detailed question as an HTML text
     *
     * as <div> has issues in showing Android Text view, it is being removed
     *
     * @receiver Result
     * @return String
     */
    private fun Result.getDetailedQuestionAsHtmlText() = try {
        document.div {
            findAll {
                "<html>" + filter { it.id == "qna_only" }.html.removePrefix("<div id=\"qna_only\">")
                    .removeSuffix("</div>") + "</html>"
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to parse the detailed question -> ${e.message}")
        "<html><p>No Questions found.</p></html>"
    }

    enum class Page {
        PREV, NEXT
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

        try {
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
        } catch (e: Exception) {
            Timber.e(e, "Failed to get $prevOrNext url -> ${e.message}.")
            return ""
        }
    }

    /**
     * Parses the relevant questions with the existing question
     *
     * Finds all the `div` elements with the class name `crp_related_widget` and then find all the
     * `li` elements within the `ul` element and then map the `li` elements to a `Question` object
     */
    private fun Result.getRelevantQuestionsFromQuestionDetails(): List<Question> {
        return try {
            document.div {
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
        } catch (e: Exception) {
            Timber.e(e, "Failed to get relevant questions from $this -> ${e.message}")
            emptyList()
        }
    }


    /**
     * Makes a network call to the IslamQa.org, gets the html response, parses it
     * and converts it into a [FiqhBasedQuestionListDto]
     *
     * @param fiqh Fiqh - The Fiqh for which the questions are to be fetched.
     * @param pageNumber The page number of the questions list.
     * @return A list of questions.
     */
    suspend fun parseFiqhBasedQuestionsList(fiqh: Fiqh, pageNumber: Int): FiqhBasedQuestionListDto {
        return withContext(Dispatchers.Default) {

            val fiqhParamName = if (fiqh == Fiqh.UNKNOWN) Fiqh.HANAFI.paramName else fiqh.paramName

            val fiqhBasedQuestionUrl =
                "https://islamqa.org/category/${fiqhParamName}/page/$pageNumber/"

            val fiqhBasedQuestionListDto = skrape(HttpFetcher) {
                request { url = fiqhBasedQuestionUrl }
                response { getFiqhBasedQuestionListDtoOutOfResponse(this, fiqh) }
            }
            fiqhBasedQuestionListDto
        }
    }

    /**
     * Parses the HTML response and returns [FiqhBasedQuestionListDto] class.
     */
    fun getFiqhBasedQuestionListDtoOutOfResponse(
        result: Result, fiqh: Fiqh
    ): FiqhBasedQuestionListDto {
        result.apply {
            return FiqhBasedQuestionListDto(
                httpStatusCode = status { code },
                httpStatusMessage = status { message },
                questions = getFiqhBasedQuestionsFromResult(),
                questionLinks = getQuestionLinksForFiqhBasedQuestions(),
                fiqh = fiqh
            )
        }
    }

    /**
     * Gets the questions from the result page
     *
     * @receiver [Result]
     */
    private fun Result.getFiqhBasedQuestionsFromResult() = try {
        document.h2 {
            findAll {
                eachText
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to get questions -> ${e.message}.")
        emptyList()
    }

    /**
     * Gets the question links for the fiqh based questions
     *
     * @receiver [Result]
     */
    private fun Result.getQuestionLinksForFiqhBasedQuestions() = try {
        document.h2 {
            findAll {
                map { it.eachHref.joinToString(",") }
            }
        }
    } catch (e: Exception) {
        Timber.e(e, "Failed to get question links -> ${e.message}.")
        emptyList()
    }

    suspend fun parseSearchResults(query: String): SearchResultQuestionDto {
        val spaceLessQuery = query.replace(" ", "+")
        val searchResultUrl = "$baseIslamQaUrl?s=&s=$spaceLessQuery"

        val searchResultQuestionDto = skrape(HttpFetcher) {
            request { url = searchResultUrl }
            response { getSearchResultQuestionListDtoOutOfResponse() }
        }

        return searchResultQuestionDto
    }

    /**
     * Parses the HTML response and returns [SearchResultQuestionDto] class.
     */
    private fun Result.getSearchResultQuestionListDtoOutOfResponse(): SearchResultQuestionDto {
        Timber.d("search results response -> $this")
        return SearchResultQuestionDto(
            httpStatusCode = status { code },
            httpStatusMessage = status { message },
            questions = findSearchResultQuestionQuestionTexts(),
            questionLinks = findSearchResultQuestionLinks(),
        )
    }

    private fun Result.findSearchResultQuestionQuestionTexts(): List<String> {
        return try {

            document.a {
                findAll {
                    Timber.d("list of doc element -> $this\n\n")
                }
            }

            document.findAll("div").filter { it.className == "gs-title" }.eachText
        } catch (e: Exception) {
            Timber.e(e, "Failed to find the questions list")
            emptyList()
        }
    }

    private fun Result.findSearchResultQuestionLinks(): List<String> {
        return try {
            document.li { a { findAll { filter { it.className == "arpw-title" }.eachHref } } }
        } catch (e: Exception) {
            Timber.e(e, "Failed to find RandomQuestion Question Links ")
            emptyList()
        }
    }

}

private const val TAG = "ScrapingService"