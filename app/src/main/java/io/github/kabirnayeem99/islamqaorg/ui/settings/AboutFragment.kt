package io.github.kabirnayeem99.islamqaorg.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentAboutBinding
import timber.log.Timber


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
            cvLicenses.setOnClickListener {
                try {
//                    startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
                } catch (e: Exception) {
                    Timber.e(e, "Failed to open license -> ${e.localizedMessage}.")
                }
            }
            cvSourceCode.setOnClickListener {
                //
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/kabirnayeem99/islam_qa_org_android")
                )
                startActivity(browserIntent)
            }
            cvAbout.setOnClickListener {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://islamqa.org/about/")
                )
                startActivity(browserIntent)
            }
        }
    }
}