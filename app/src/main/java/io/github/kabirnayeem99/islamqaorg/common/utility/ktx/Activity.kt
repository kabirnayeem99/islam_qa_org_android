package io.github.kabirnayeem99.islamqaorg.common.utility.ktx

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
    startActivity(Intent(this, activity))
    if (finishCurrentActivity) this.finish()
}

/**
 * Navigates to a certain activity.
 *
 * @receiver [AppCompatActivity]
 * @param T Type of the activity
 * @param activity Activity
 * @param finishCurrentActivity Finish current activity or not
 */
fun <T> Activity.gotoActivity(activity: Class<T>, finishCurrentActivity: Boolean = false) {
    startActivity(Intent(this.applicationContext, activity))
    if (finishCurrentActivity) this.finish()
}

fun Activity.openUrlInWebView(url: String) {
    if (url.isBlank()) return

    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    startActivity(browserIntent)
}