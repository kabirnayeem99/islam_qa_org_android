package io.github.kabirnayeem99.islamqaorg.ui.home

import android.os.Bundle
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
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val layout: Int
        get() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeToData()
    }

    private val homeViewModel: HomeViewModel by viewModels()

    private fun subscribeToData() {
        homeViewModel.apply {
            getHomeScreenData()
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    uiState.collect(::handleUiState)
                }
            }
        }
    }

    @Inject
    lateinit var questionAdapter: QuestionAdapter

    @Inject
    lateinit var questionSliderAdapter: QuestionSliderAdapter

    private fun handleUiState(uiState: HomeScreenUiState) {
        uiState.apply {
            if (isLoading) loading.show() else loading.hide()
            questionAdapter.submitQuestionList(questionAnswers)
            questionSliderAdapter.submitQuestionList(questionAnswers)
            messages.firstOrNull()?.let { userMessage ->
                binding.root.showUserMessage(userMessage.message)
                homeViewModel.userMessageShown(userMessage.id)
            }
        }
    }


    private fun initViews() {
        binding.rvQuestions.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            hasFixedSize()
            adapter = questionSliderAdapter
        }

        binding.rvLatestQuestions.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = questionAdapter
        }

        questionAdapter.setOnClickListener { navigateToQuestionDetailsScreen(it) }
        questionSliderAdapter.setOnClickListener { navigateToQuestionDetailsScreen(it) }

        (activity as MainActivity).setOnSyncButtonClickListener {
            showLoadingForAShortTimePeriod()
            homeViewModel.getHomeScreenData(true)
        }

        (activity as MainActivity).setOnSettingButtonClickListener {
            navController.navigate(R.id.action_HomeFragment_to_settingsFragment)
        }
    }

    private fun showLoadingForAShortTimePeriod() {
        viewLifecycleOwner.lifecycleScope.launch {
            loading.show()
            delay(2000)
            loading.dismiss()
        }
    }

    private fun navigateToQuestionDetailsScreen(question: Question) {
        val url = question.url
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(url)
        navController.navigate(action)
    }

}