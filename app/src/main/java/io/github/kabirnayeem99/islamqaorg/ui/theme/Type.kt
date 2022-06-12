package io.github.kabirnayeem99.islamqaorg.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.kabirnayeem99.islamqaorg.R


val IslamQaFontFamily = FontFamily(
    Font(R.font.cardo_regular, weight = FontWeight.Normal),
    Font(R.font.cardo_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.cardo_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.arabian_onenighlsland, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
    Font(R.font.arabian_onenighlsland, weight = FontWeight.Black, style = FontStyle.Normal)
)

val ArabicFontFamily = FontFamily(
    Font(R.font.arabian_onenighlsland, weight = FontWeight.Normal),
    Font(R.font.arabian_onenighlsland, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.arabian_onenighlsland, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.arabian_onenighlsland, weight = FontWeight.ExtraBold, style = FontStyle.Normal),
    Font(R.font.arabian_onenighlsland, weight = FontWeight.Black, style = FontStyle.Normal)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 46.sp
    ),
    titleMedium = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp
    ),
    titleSmall = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = IslamQaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)