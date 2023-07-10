package io.github.kabirnayeem99.islamqaorg.common.utility.ktx

import android.animation.ValueAnimator.REVERSE
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
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
    rotate.repeatMode = REVERSE
    startAnimation(rotate)
}


fun View.slideInRight(duration: Long = 1500) {
    val slideInAnim = TranslateAnimation(
        -500.0f,
        0.0f,
        0.0f,
        0.0f
    )
    slideInAnim.duration = duration
    slideInAnim.interpolator = BounceInterpolator()
    slideInAnim.fillAfter = true
    startAnimation(slideInAnim)

}