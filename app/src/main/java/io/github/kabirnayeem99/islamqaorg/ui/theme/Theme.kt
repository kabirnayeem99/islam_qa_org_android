package io.github.kabirnayeem99.islamqaorg.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightThemeColors = lightColorScheme(
    primary = PurpleLight,
    onPrimary = White,
    primaryContainer = Color(0xffFFD7F2),
    onPrimaryContainer = Color(0xff390034),
    secondary = Color(0xff8C4382),
    onSecondary = White,
    secondaryContainer = Color(0xffFFD7F3),
    onSecondaryContainer = Color(0xff390035),
    tertiary = Color(0xff705D00),
    onTertiary = White,
    tertiaryContainer = YellowLight,
    onTertiaryContainer = Color(0xff221B00),
    error = Color(0xffBA1A1A),
    onError = White,
    errorContainer = Color(0xff221B00),
    onErrorContainer = Color(0xffFFDAD6),
    background = Color(0xffFFFBFF),
    onBackground = Color(0xff1F1A1D),
    surface = Color(0xffFFFBFF),
    onSurface = Color(0xff1F1A1D),
)
private val DarkThemeColors = darkColorScheme(
    primary = PurpleDark,
    onPrimary = Color(0xff5C0454),
    primaryContainer = Color(0xff5C0454),
    onPrimaryContainer = Color(0xffFFD7F2),
    secondary = Color(0xffFFABEE),
    onSecondary = Color(0xff561250),
    secondaryContainer = Color(0xff712B68),
    onSecondaryContainer = Color(0xffFFD7F3),
    tertiary = YellowDark,
    onTertiary = Color(0xff3A3000),
    tertiaryContainer = Color(0xff544600),
    onTertiaryContainer = Color(0xffFFE16E),
    error = Color(0xffFFB4AB),
    onError = Color(0xff690005),
    errorContainer = Color(0xff93000A),
    onErrorContainer = Color(0xffFFDAD6),
    background = Color(0xff1F1A1D),
    onBackground = Color(0xffEAE0E3),
    surface = Color(0xff1F1A1D),
    onSurface = Color(0xffEAE0E3),
)

@Composable
fun IslamQaTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(LocalContext.current)
        }
        dynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        isDarkTheme -> DarkThemeColors
        else -> LightThemeColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}