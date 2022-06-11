package io.github.kabirnayeem99.islamqaorg.ui.start

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import io.github.kabirnayeem99.islamqaorg.BuildConfig
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseActivity
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.gotoActivity
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.rotateViewOneEighty
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.slideInRight
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.viewVisibility
import io.github.kabirnayeem99.islamqaorg.databinding.ActivityStartBinding
import io.github.kabirnayeem99.islamqaorg.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val SPLASH_SCREEN_DURATION: Long = 1000L

class StartActivity : BaseActivity<ActivityStartBinding>() {
    override val layout: Int
        get() = R.layout.activity_start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            lifecycleScope.launch {
                tvAppVersionName.text = BuildConfig.VERSION_NAME
                delay(SPLASH_SCREEN_DURATION / 3)
                cvVersionName.viewVisibility(View.VISIBLE)
                cvVersionName.slideInRight(SPLASH_SCREEN_DURATION / 4)
                ivBackgroundGeometry.rotateViewOneEighty((SPLASH_SCREEN_DURATION * 2) / 3)
                delay((SPLASH_SCREEN_DURATION * 2) / 3)
                navigateToOtherScreen()
            }
        }
    }


    private fun navigateToOtherScreen() {
        gotoActivity(MainActivity::class.java, true)
    }
}