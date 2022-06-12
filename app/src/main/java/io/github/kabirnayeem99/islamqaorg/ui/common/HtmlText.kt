package io.github.kabirnayeem99.islamqaorg.ui.common

import android.widget.TextView
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import io.github.kabirnayeem99.islamqaorg.R


@Composable
fun HtmlText(
    text: String,
    style: TextStyle = LocalTextStyle.current,
) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT))
            textSize = style.fontSize.value
            val typeface = ResourcesCompat.getFont(context, R.font.cardo_regular)
            setTypeface(typeface)
            val color = style.color.toArgb()
            setTextColor(color)
        }
    })
}