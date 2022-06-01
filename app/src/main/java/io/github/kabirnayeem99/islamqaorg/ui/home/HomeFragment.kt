package io.github.kabirnayeem99.islamqaorg.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentHomeBinding
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

    private fun handleUiState(uiState: HomeScreenUiState) {
        uiState.apply {
            if (isLoading) loading.show() else loading.hide()
            questionAdapter.submitQuestionList(questionAnswers)
            messages.firstOrNull()?.let { userMessage ->
                Toast.makeText(requireContext(), userMessage.message, Toast.LENGTH_SHORT).show()
                homeViewModel.userMessageShown(userMessage.id)
            }
        }
    }


    private fun initViews() {
        binding.rvQuestions.apply {
            layoutManager = LinearLayoutManager(context)
            hasFixedSize()
            adapter = questionAdapter
        }

        questionAdapter.setOnClickListener { navigateToQuestionDetailsScreen() }
    }

    private fun navigateToQuestionDetailsScreen() {
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

}