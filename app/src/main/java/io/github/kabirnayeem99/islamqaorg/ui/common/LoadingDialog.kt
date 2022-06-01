package io.github.kabirnayeem99.islamqaorg.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import io.github.kabirnayeem99.islamqaorg.R

/**
 * Dialog that shows a loading animation
 */
class DialogLoading(context: Context) : Dialog(context) {

    init {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }

}