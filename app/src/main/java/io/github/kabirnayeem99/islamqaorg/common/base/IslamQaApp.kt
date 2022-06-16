package io.github.kabirnayeem99.islamqaorg.common.base

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import dagger.hilt.android.HiltAndroidApp
import io.github.kabirnayeem99.islamqaorg.R
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant
import javax.inject.Inject

@HiltAndroidApp
class IslamQaApp : Application(), Configuration.Provider {

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

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /**
     * Called by the WorkManager library to get the configuration for the WorkManager instance
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}