package io.github.kabirnayeem99.islamqaorg.common.utility.ktx

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.android.material.snackbar.Snackbar

fun View.showUserMessage(userMessage: String) {
    Snackbar.make(this, userMessage, Snackbar.LENGTH_SHORT)
        .show()
}

/**
 * Used to hide or show the view
 *
 * @receiver [View]
 * @param visibility Visibility
 */
fun View.viewVisibility(visibility: Int) {
    this.visibility = visibility
}

fun View.rotateViewOneEighty(duration: Long = 5000L) {
    val rotate = RotateAnimation(
        0F,
        180F,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotate.duration = duration
    rotate.interpolator = AccelerateDecelerateInterpolator()
    startAnimation(rotate)
}