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
        setUpTimber()
    }

    /**
     * If the app is in debug mode, plants a debug tree
     */
    private fun setUpTimber() {
        if (BuildConfig.DEBUG) plant(DebugTree())
    }
}