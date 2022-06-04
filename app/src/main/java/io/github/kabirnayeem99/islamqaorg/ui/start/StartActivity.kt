package io.github.kabirnayeem99.islamqaorg.ui.start

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseActivity
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.gotoActivity
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.rotateViewOneEighty
import io.github.kabirnayeem99.islamqaorg.databinding.ActivityStartBinding
import io.github.kabirnayeem99.islamqaorg.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SPLASH_SCREEN_DURATION: Long = 3000L

class StartActivity : BaseActivity<ActivityStartBinding>() {
    override val layout: Int
        get() = R.layout.activity_start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            tvAppVersionName.text = BuildConfig.VERSION_NAME
            ivBackgroundGeometry.rotateViewOneEighty(SPLASH_SCREEN_DURATION)
        }
        navigateToOtherScreen()
    }


    private fun navigateToOtherScreen() {
        lifecycleScope.launch {
            delay(SPLASH_SCREEN_DURATION)
            gotoActivity(MainActivity::class.java, true)
        }
    }
}