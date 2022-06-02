package io.github.kabirnayeem99.islamqaorg.data.dataSource.service

import io.github.kabirnayeem99.islamqaorg.data.dto.IslamQaHomeDto
import io.github.kabirnayeem99.islamqaorg.data.dto.QuestionDetailScreenDto
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
        questions = document.findAll("li").filter {
            it.className == "arpw-li arpw-clearfix"
        }.eachText,
        questionLinks = document.li {
            a {
                findAll {
                    filter {
                        it.className == "arpw-title"
                    }.eachHref
                }
            }
        }
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
                        detailedQuestion = document.div {
                            findAll {
                                "<html>" + filter { it.id == "qna_only" }.html.removePrefix("<div id=\"qna_only\">")
                                    .removeSuffix("</div>") + "</html>"
                            }
                        },
                        detailedAnswer = document.div {
                            findAll {
                                filter { it.className == "ddb-answer" }.eachText.joinToString(
                                    "\n"
                                )
                            }
                        },
                        fiqh = document.div { findAll { first { it.className == "et_pb_code_inner" && it.parent.id == "meta-1" }.a { findFirst { text } } } },
                        source = document.div { findAll { first { it.className == "et_pb_code_inner" && it.parent.id == "meta-1" }.eachLink.keys.last() } },
                        originalLink = link,
                        nextQuestionLink = document.div {
                            findAll {
                                span {
                                    findAll {
                                        first { it.className == "nav-next" }.a {
                                            findAll { eachHref.firstOrNull() ?: "" }
                                        }
                                    }
                                }
                            }
                        },
                        previousQuestionLink = document.div {
                            findAll {
                                span {
                                    findAll {
                                        first { it.className == "nav-previous" }.a {
                                            findAll { eachHref.firstOrNull() ?: "" }
                                        }
                                    }
                                }
                            }
                        },
                        relevantQuestions = document.div {
                            // crp_related_widget
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
                    )
                }
            }
            Timber.d(questionDetail.toString())

            questionDetail
        }
    }

}