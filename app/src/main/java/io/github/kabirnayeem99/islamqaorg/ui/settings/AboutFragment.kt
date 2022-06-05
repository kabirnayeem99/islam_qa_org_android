package io.github.kabirnayeem99.islamqaorg.ui.settings

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.openUrlInWebView
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.slideInRight
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentAboutBinding


const val SOURCE_CODE_URL = "https://github.com/kabirnayeem99/islam_qa_org_android"
const val ISLAM_QA_ABOUT_URL = "https://islamqa.org/about/"

@AndroidEntryPoint
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    override val layout: Int
        get() = R.layout.fragment_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            tvAppVersionName.text = BuildConfig.VERSION_NAME
            cvLicenses.setOnClickListener { activity?.openUrlInWebView(SOURCE_CODE_URL) }
            cvSourceCode.setOnClickListener { activity?.openUrlInWebView(SOURCE_CODE_URL) }
            cvAbout.setOnClickListener { activity?.openUrlInWebView(ISLAM_QA_ABOUT_URL) }
            cvVersionName.slideInRight()
        }
    }

}