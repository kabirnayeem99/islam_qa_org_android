package io.github.kabirnayeem99.islamqaorg.ui.start

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.gotoActivity
import io.github.kabirnayeem99.islamqaorg.ui.MainActivity
import io.github.kabirnayeem99.islamqaorg.ui.theme.IslamQaTheme


const val SPLASH_SCREEN_DURATION: Long = 2000L

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IslamQaTheme {
                StartScreen(onTimeUp = { navigateToOtherScreen() }, onCloseApp = { closeApp() })
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        makeFullScreen()
    }

    private fun closeApp() {
        finish()
    }

    @Suppress("DEPRECATION")
    private fun makeFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun navigateToOtherScreen() {
        gotoActivity(MainActivity::class.java, true)
    }
}

