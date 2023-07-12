package io.github.kabirnayeem99.islamqaorg.domain.useCase

import androidx.core.text.isDigitsOnly
import io.github.kabirnayeem99.islamqaorg.common.base.Resource
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.domain.repository.QuestionAnswerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.Locale
import javax.inject.Inject

class SearchQuestion
@Inject constructor(private val repository: QuestionAnswerRepository) {

    suspend operator fun invoke(query: String): Flow<Resource<List<Question>>> {
        val queries = splitIntoTerms(query)
        return repository.searchQuestions(queries).distinctUntilChanged()
            .map { validateToResource(it) }
            .catch { e -> emit(Resource.Error(generateErrorMessage(e.localizedMessage ?: ""))) }
            .onStart { emit(validateQueryAndMapToResource(query)) }
    }

    private fun splitIntoTerms(query: String) =
        query.split(" ").filterNot { term -> term.isEmpty() || term.isDigitsOnly() }
            .map { term -> term.lowercase(Locale.getDefault()) }
            .map { term -> term.replace(Regex("[^A-Za-z0-9]"), "") }

    private fun validateToResource(questionList: List<Question>): Resource<List<Question>> {
        return if (questionList.isEmpty()) Resource.Error("Empty")
        else Resource.Success(questionList)
    }

    private fun validateQueryAndMapToResource(query: String): Resource<List<Question>> {
        val errorMessage = validateQuery(query)
        return if (errorMessage == null) Resource.Loading() else Resource.Error(errorMessage)
    }

    private fun validateQuery(query: String): String? {
        if (query.isEmpty()) return generateErrorMessage("Can't be empty.")
        if (query.startsWith(" ")) return generateErrorMessage("Remove space at start.")
        if (query.firstOrNull()?.isDigit() == true) return generateErrorMessage("Remove number.")
        val nonEnglishRegex = Regex("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}]")
        if (nonEnglishRegex.containsMatchIn(query)) return generateErrorMessage("Only in English.")

        return null
    }


    private fun generateErrorMessage(message: String): String = "Failed to search. $message."

}