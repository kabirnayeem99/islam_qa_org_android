package io.github.kabirnayeem99.islamqaorg.common.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.kabirnayeem99.islamqaorg.BuildConfig
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
//        val dynamicOptions = DynamicColorsOptions.Builder()
//            .setThemeOverlay(R.style.Theme_IslamQAOrg_NoActionBar)
//            .build()
//        DynamicColors.applyToActivitiesIfAvailable(this, dynamicOptions)
    }

    /**
     * If the app is in debug mode, plants a debug tree
     */
    private fun setUpTimber() {
        if (BuildConfig.DEBUG) plant(DebugTree())
    }
}