package io.github.kabirnayeem99.islamqaorg.common.utility.ktx

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showUserMessage(userMessage: String) {
    Snackbar.make(this, userMessage, Snackbar.LENGTH_SHORT)
        .show()
}