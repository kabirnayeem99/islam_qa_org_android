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

    /**
     * Subscribes to data changes and UI State changes
     */
    private fun subscribeToData() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.apply {
                getRandomQuestions()
                getFiqhBasedQuestions()
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    uiState.collect(::handleUiState)
                }
            }
        }
    }

    @Inject
    lateinit var questionAdapter: QuestionAdapter

    @Inject
    lateinit var questionSliderAdapter: QuestionSliderAdapter


    /**
     * Takes a `HomeScreenUiState` object as a parameter and updates the UI based on the state of
     * the object
     *
     * @param uiState HomeScreenUiState - This is the data class that contains all the data that needs
     * to be displayed on the screen.
     */
    private fun handleUiState(uiState: HomeScreenUiState) {
        uiState.apply {

            questionAdapter.submitQuestionList(fiqhBasedQuestions)
            questionSliderAdapter.submitQuestionList(randomQuestions)

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


    /**
     * Initializes the views.
     */
    private fun initViews() {

        initRandomQuestionSliderView()
        initFiqhBasedQuestionList()
        showRotatingGeometryForever()

        (activity as MainActivity).setOnSyncButtonClickListener {
            homeViewModel.getRandomQuestions(true)
        }

        (activity as MainActivity).setOnSettingButtonClickListener {
            navController.navigate(R.id.action_HomeFragment_to_settingsFragment)
        }

    }

    /**
     * Initializes the RecyclerView for Fiqh-based questions with the adapter
     * and sets the layout manager.
     */
    private fun initFiqhBasedQuestionList() {
        val linearLayoutManager = LinearLayoutManager(context)
        binding.rvLatestQuestions.apply {
            adapter = questionAdapter
            layoutManager = linearLayoutManager
            hasFixedSize()
        }
        questionAdapter.setOnClickListener { q -> navigateToQuestionDetailsScreen(q) }
    }


    /**
     * Initializes the random question slider view.
     */
    private fun initRandomQuestionSliderView() {
        val horizontalLinearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        questionSliderAdapter.setOnClickListener { navigateToQuestionDetailsScreen(it) }
        binding.rvQuestions.apply {
            adapter = questionSliderAdapter
            layoutManager = horizontalLinearLayoutManager
            hasFixedSize()
        }
    }

    /**
     * Rotates (animate) the Islamic geometric design 180 degrees every 4 seconds, forever
     */
    private fun showRotatingGeometryForever() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            while (viewLifecycleOwner.lifecycleScope.isActive) {
                binding.ivDesignGeometry.rotateViewOneEighty(4000)
                delay(3950)
            }
        }
    }

    /**
     * Takes a question object as an argument and navigates to the details fragment by passing the
     * question's url as an argument
     *
     * @param question Question - This is the question object that we want to pass to the details
     * screen.
     */
    private fun navigateToQuestionDetailsScreen(question: Question) {
        val url = question.url
        if (url.isBlank()) {
            binding.root.showUserMessage("Could not find the URL for this \"${question.question}\".")
            return
        }
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(url)
        navController.navigate(action)
    }

}