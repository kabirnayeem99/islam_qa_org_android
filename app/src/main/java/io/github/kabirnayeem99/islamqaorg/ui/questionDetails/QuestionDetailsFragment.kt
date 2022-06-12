package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.showUserMessage
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.viewVisibility
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentQuestionDetailsBinding
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuestionDetailsFragment : BaseFragment<FragmentQuestionDetailsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_question_details

    private val questionDetailViewModel: QuestionDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeToData()
    }

    @Inject
    lateinit var questionAdapter: QuestionAdapter

    private fun initViews() {
        binding.apply {
            rvRelatedQuestions.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = questionAdapter
                hasFixedSize()
            }

            questionAdapter.setOnClickListener { subscribeToData(it.url) }
        }
    }


    private fun subscribeToData(url: String = "") {
        viewLifecycleOwner.lifecycleScope.launch {
            questionDetailViewModel.apply {
//                getQuestionsDetailsJob(url = url.ifBlank { args.url })
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    uiState.collect(::handleUiState)
                }
            }
        }
    }


    private fun handleUiState(uiState: QuestionDetailsUiState) {
        uiState.apply {

            binding.sflLoading.viewVisibility(if (isLoading) View.VISIBLE else View.GONE)
            binding.llContent.viewVisibility(if (isLoading) View.GONE else View.VISIBLE)

            binding.questionDetail = uiState.questionDetails

            messages.firstOrNull()?.let { userMessage ->
                binding.root.showUserMessage(userMessage.message)
                questionDetailViewModel.userMessageShown(userMessage.id)
            }

            questionAdapter.submitQuestionList(uiState.questionDetails.relevantQuestions)

            val detailedQuestion = uiState.questionDetails.detailedQuestion
            binding.tvDetailQuestion.text =
                Html.fromHtml(detailedQuestion, Html.FROM_HTML_MODE_COMPACT)
        }
    }


}