package io.github.kabirnayeem99.islamqaorg.common.base

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import dagger.hilt.android.HiltAndroidApp
import io.github.kabirnayeem99.islamqaorg.R
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class IslamQaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpDynamicColors()
        setUpTimber()
    }

    /**
     * Sets up the dynamic colors for the app.
     */
    private fun setUpDynamicColors() {
        val dynamicOptions = DynamicColorsOptions.Builder()
            .setThemeOverlay(R.style.AppTheme)
            .build()
        DynamicColors.applyToActivitiesIfAvailable(this, dynamicOptions)
    }

    /**
     * If the app is in debug mode, plants a debug tree
     */
    private fun setUpTimber() {
        plant(DebugTree())
    }
}