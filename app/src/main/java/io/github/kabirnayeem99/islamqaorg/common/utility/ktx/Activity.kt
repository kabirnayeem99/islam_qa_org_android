package io.github.kabirnayeem99.islamqaorg.common.utility.ktx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * Navigates to a certain activity.
 *
 * @receiver [AppCompatActivity]
 * @param T Type of the activity
 * @param activity Activity
 * @param finishCurrentActivity Finish current activity or not
 */
fun <T> AppCompatActivity.gotoActivity(activity: Class<T>, finishCurrentActivity: Boolean = false) {
    startActivity(Intent(this.applicationContext, activity))
    if (finishCurrentActivity) this.finish()
}