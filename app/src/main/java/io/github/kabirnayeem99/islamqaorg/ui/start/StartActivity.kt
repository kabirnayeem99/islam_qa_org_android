package io.github.kabirnayeem99.islamqaorg.ui.start

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseActivity
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.gotoActivity
import io.github.kabirnayeem99.islamqaorg.databinding.ActivityStartBinding
import io.github.kabirnayeem99.islamqaorg.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartActivity : BaseActivity<ActivityStartBinding>() {
    override val layout: Int
        get() = R.layout.activity_start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.tvAppVersionName.text = BuildConfig.VERSION_NAME
        navigateToOtherScreen()
    }

    private fun navigateToOtherScreen() {
        lifecycleScope.launch {
            delay(3000)
            gotoActivity(MainActivity::class.java, true)
        }
    }
}