package io.github.kabirnayeem99.islamqaorg.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            IslamQaRoot()
        }
    }


    private var onSyncButtonClick: (() -> Unit) = {}

    private var onSettingsButtonClick: (() -> Unit) = {}

    fun setOnSyncButtonClickListener(onClickParam: (() -> Unit)) {
        onSyncButtonClick = onClickParam
    }

    fun setOnSettingButtonClickListener(onClickParam: (() -> Unit)) {
        onSettingsButtonClick = onClickParam
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}