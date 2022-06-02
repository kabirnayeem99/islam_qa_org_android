package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
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
            }
        }
    }

    private val args: QuestionDetailsFragmentArgs by navArgs()

    private fun subscribeToData() {
        viewLifecycleOwner.lifecycleScope.launch {
            questionDetailViewModel.apply {
                getQuestionsDetailsJob(url = args.url)
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    uiState.collect(::handleUiState)
                }
            }
        }
    }


    private fun handleUiState(uiState: QuestionDetailsUiState) {
        uiState.apply {
            if (isLoading) loading.show() else loading.dismiss()
            binding.questionDetail = uiState.questionDetails
            messages.firstOrNull()?.let { userMessage ->
                Toast.makeText(requireContext(), userMessage.message, Toast.LENGTH_SHORT).show()
                questionDetailViewModel.userMessageShown(userMessage.id)
            }

            questionAdapter.submitQuestionList(uiState.questionDetails.relevantQuestions)

            val detailedQuestion = uiState.questionDetails.detailedQuestion
            binding.tvDetailQuestion.text =
                Html.fromHtml(detailedQuestion, Html.FROM_HTML_MODE_COMPACT)
        }
    }


}