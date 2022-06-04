package io.github.kabirnayeem99.islamqaorg.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.rotateViewOneEighty
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.showUserMessage
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.viewVisibility
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.ui.MainActivity
import kotlinx.coroutines.*
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
            getRandomQuestions()
            getFiqhBasedQuestions()
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

            questionAdapter.submitQuestionList(fiqhBasedQuestions)
            questionSliderAdapter.submitQuestionList(randomQuestions)

            if (!isRandomQuestionLoading && !isFiqhBasedQuestionsLoading) {
                showRotatingGeometryForever()
            }

            messages.firstOrNull()?.let { userMessage ->
                binding.root.showUserMessage(userMessage.message)
                homeViewModel.userMessageShown(userMessage.id)
            }

            binding.apply {
                sflRandomQuestionLoading.viewVisibility(if (isRandomQuestionLoading) View.VISIBLE else View.INVISIBLE)
                rvQuestions.viewVisibility(if (isRandomQuestionLoading) View.INVISIBLE else View.VISIBLE)
                sflFiqhBasedQuestionLoading.viewVisibility(if (isFiqhBasedQuestionsLoading) View.VISIBLE else View.INVISIBLE)
                rvLatestQuestions.viewVisibility(if (isFiqhBasedQuestionsLoading) View.INVISIBLE else View.VISIBLE)
            }

            slideShowRandomQuestionList(randomQuestions)
        }
    }


    private val factory by lazy {
        DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
    }

    private var adapterScrollingJob: Job? = null

    /**
     * Smooth scrolls through recyclerview items, to give a feeling of slide show
     *
     * @param randomQuestions List<Question>
     */
    private fun slideShowRandomQuestionList(randomQuestions: List<Question>) {
        adapterScrollingJob?.cancel()
        adapterScrollingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            randomQuestions.forEachIndexed { index, _ ->
                binding.rvQuestions.smoothScrollToPosition(index)
                delay(5000)
            }
        }
    }


    private fun initViews() {

        val horizontalLinearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager = LinearLayoutManager(context)

        binding.rvQuestions.apply {
            adapter = questionSliderAdapter
            layoutManager = horizontalLinearLayoutManager
            hasFixedSize()
        }

        binding.rvLatestQuestions.apply {
            adapter = questionAdapter
            layoutManager = linearLayoutManager
            hasFixedSize()
        }

        questionAdapter.setOnClickListener { navigateToQuestionDetailsScreen(it) }
        questionSliderAdapter.setOnClickListener { navigateToQuestionDetailsScreen(it) }

        (activity as MainActivity).setOnSyncButtonClickListener {
            showRotatingGeometryForever()
            homeViewModel.getRandomQuestions(true)
        }

        (activity as MainActivity).setOnSettingButtonClickListener {
            navController.navigate(R.id.action_HomeFragment_to_settingsFragment)
        }

    }

    private fun showRotatingGeometryForever() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            while (viewLifecycleOwner.lifecycleScope.isActive) {
                binding.ivDesignGeometry.rotateViewOneEighty(4000)
                delay(4000)
            }
        }
    }

    private fun navigateToQuestionDetailsScreen(question: Question) {
        val url = question.url
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(url)
        navController.navigate(action)
    }

}