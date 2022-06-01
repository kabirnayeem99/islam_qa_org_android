package io.github.kabirnayeem99.islamqaorg.ui.questionDetails

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentQuestionDetailsBinding

@AndroidEntryPoint
class QuestionDetailsFragment : BaseFragment<FragmentQuestionDetailsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_question_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
    }


}